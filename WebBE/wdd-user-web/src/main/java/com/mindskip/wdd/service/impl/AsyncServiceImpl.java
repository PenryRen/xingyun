package com.mindskip.wdd.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.constant.ExamErrorConstants;
import com.mindskip.wdd.domain.enums.ueit.VmClassesEnum;
import com.mindskip.wdd.domain.frame.*;
import com.mindskip.wdd.domain.ueit.*;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareQuestionVM;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 异步方法 Service业务层处理
 *
 * @author libl
 * @date 2024-04-10
 */
@AllArgsConstructor
@Service
public class AsyncServiceImpl implements AsyncService {

    private final static Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    /**
     * 异步核验实训答案
     *
     * @param error 试卷核验对象
     */
    @Async
    @Override
    public void checkAnswer(ExamPaperAnswerError error) {
        try {
            if (null == error.getVmGuid()) {
                setExamPaperAnswerError(error, ExamErrorConstants.msg_vm_lost, false);
                return;
            }

            //获取用户信息
            User user = userService.getById(error.getUserId());
            if (null != user && StringUtils.isNotEmpty(user.getUserUuid())) {
                //获取虚拟机
                VmWare select = new VmWare();
                select.setVmUserId(user.getUserUuid());
                select.setGuid(error.getVmGuid());
                select.setVmParentId(error.getVmParentId());
                select.setClasses(VmClassesEnum.Exam.getCode());
                //查询已绑定的虚拟机
                VmWare vmWare = vmWareService.selectVmWare(select);
                if (null != vmWare) {
                    //获取答卷信息
                    ExamPaperAnswer examPaperAnswer = examPaperAnswerMapper.selectById(error.getAnswerId());
                    if (null == examPaperAnswer) {
                        setExamPaperAnswerError(error, ExamErrorConstants.msg_answer_lost, false);
                        return;
                    }
                    //设置不可分配
                    vmWare.setDisabled(true);
                    vmWareService.updateVmWare(vmWare);
                    //获取试卷
                    ExamPaper examPaper = examPaperMapper.selectById(error.getExamPaperId());
                    if (null == examPaper) {
                        setExamPaperAnswerError(error, ExamErrorConstants.msg_paper_lost, false);
                        return;
                    }
                    //获取试卷详情
                    String paperFrameId = examPaper.getPaperFrameId();
                    ExamPaperFrame examPaperFrame = examPaperJsonMapper.selectById(paperFrameId).getContent();
                    if (null == examPaperFrame) {
                        setExamPaperAnswerError(error, ExamErrorConstants.msg_paper_json_lost, false);
                        return;
                    }
                    List<String> questionFrameIdList = examPaperFrame.getExamPaperItemFrames().stream()
                            .flatMap(pt -> pt.getExamPaperItemQuestionFrames().stream()
                                    .map(ExamPaperItemQuestionFrame::getQuestionFrameId))
                            .collect(Collectors.toList());
                    //获取题目列表
                    List<QuestionFrame> questionFrameList = questionFrameIdList.size() > 0
                            ? questionJsonMapper.selectBatchIds(questionFrameIdList).stream().map(QuestionJson::getContent).collect(Collectors.toList())
                            : new ArrayList<>(0);


                    Integer resultScore = 0;
                    Integer questionCorrect = 0;
                    //获取答卷内容
                    ExamPaperAnswerJson examPaperAnswerJson = examPaperAnswerJsonMapper.selectById(error.getAnswerFrameId());
                    if (null == examPaperAnswerJson) {
                        setExamPaperAnswerError(error, ExamErrorConstants.msg_answer_json_lost, false);
                        return;
                    }
                    //获取用户答案列表
                    List<QuestionAnswerFrame> oldQuestionAnswerFrameList = examPaperAnswerJson.getContent().getQuestionAnswerFrameList();

                    int errorCount = 0;
                    for (QuestionAnswerFrame questionAnswerFrame : oldQuestionAnswerFrameList) {
                        QuestionFrame questionFrame = questionFrameList.stream().filter(qf -> qf.getQuestionId().equals(questionAnswerFrame.getQuestionId())).findFirst().get();
                        boolean checked = questionAnswerCheck(questionAnswerFrame, questionFrame, vmWare);
                        if (checked) {
                            if (null != questionAnswerFrame.getDoRight() && questionAnswerFrame.getDoRight()) {
                                ++questionCorrect;
                            }
                            resultScore += questionAnswerFrame.getCustomerScore();
                        } else {
                            errorCount++;
                        }
                    }

                    //更新答卷内容
                    examPaperAnswerJson.getContent().setQuestionAnswerFrameList(oldQuestionAnswerFrameList);
                    examPaperAnswerJsonMapper.updateById(examPaperAnswerJson);

                    //更新答卷基本信息
                    examPaperAnswer.setUserScore(resultScore);
                    examPaperAnswer.setQuestionCorrect(questionCorrect);
                    examPaperAnswer.setStatus(errorCount > 0 ? ExamPaperAnswerStatusEnum.CheckError.getCode() : ExamPaperAnswerStatusEnum.Complete.getCode());
                    if (errorCount > 0) {
                        //核验失败
                        setExamPaperAnswerError(error, ExamErrorConstants.msg_vm_link_error, false);
                    } else {
                        //核验通过
                        setExamPaperAnswerError(error, null, true);
                        if (resultScore >= examPaperAnswer.getPassScore()) {
                            examPaperAnswer.setPassed(true);
                            if (null != examPaperAnswer.getCredentialTemplateId()) {
                                UserCredential userCredential = new UserCredential();
                                userCredential.setDeleted(false);
                                userCredential.setCreateTime(new Date());
                                userCredential.setCredentialBuildTime(examPaperAnswer.getCreateTime());
                                userCredential.setUserId(examPaperAnswer.getCreateUser());
                                userCredential.setCreateDepartmentId(examPaperAnswer.getCreateDepartmentId());
                                userCredential.setCredentialTemplateId(examPaperAnswer.getCredentialTemplateId());
                                userCredential.setExamPaperAnswerId(examPaperAnswer.getId());
                                userCredential.setExamPaperName(examPaperAnswer.getPaperName());
                                userCredential.setExamPaperId(examPaperAnswer.getExamPaperId());
                                userCredential.setExamPaperBuildId(examPaperAnswer.getExamPaperBuildId());
                                userCredentialMapper.insert(userCredential);
                            }
                        } else {
                            examPaperAnswer.setPassed(false);
                        }

                        //删除虚拟机
                        vmWareService.releaseVmWare(vmWare);
                    }
                    examPaperAnswerMapper.updateById(examPaperAnswer);
                } else {
                    setExamPaperAnswerError(error, ExamErrorConstants.msg_vm_bind_lost, false);
                }
            } else {
                setExamPaperAnswerError(error, ExamErrorConstants.msg_user_lost, false);
            }
        } catch (Exception e) {
            logger.error("异步核验实训答案,业务异常----------------------", e);
            e.printStackTrace();
        }
    }

    /**
     * 核验实训考试题目答案
     *
     * @param questionAnswerFrame 用户答案
     * @param questionFrame       题目
     * @param vmWare              虚拟机
     */
    private boolean questionAnswerCheck(QuestionAnswerFrame questionAnswerFrame, QuestionFrame questionFrame, VmWare vmWare) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        if (questionTypeEnum.getCode() == QuestionTypeEnum.TrainOperate.getCode()) {
            SshParam sshParam = new SshParam();
            sshParam.setHost(vmWare.getVmIp());
            sshParam.setUsername(vmWare.getVmUsername());
            sshParam.setPassword(vmWare.getVmPassword());
            sshParam.setCommand(questionFrame.getCommand());
            logger.info("请求参数:"+ JSONObject.toJSONString(sshParam));
            SshResult sshResult = sshService.executeCommand(sshParam);
            logger.info("返回结果:"+ JSONObject.toJSONString(sshResult));
            if (sshResult.getErrCode() == 0) {
                String result = sshResult.getResult().toString();
                questionAnswerFrame.setContent(result);
                if (result.contains(questionFrame.getCorrect())) {
                    questionAnswerFrame.setCustomerScore(questionAnswerFrame.getQuestionScore());
                    questionAnswerFrame.setCustomerScoreVM(questionAnswerFrame.getQuestionScoreVM());
                    questionAnswerFrame.setDoRight(true);
                    questionAnswerFrame.setDoCheck(true);
                } else {
                    questionAnswerFrame.setCustomerScore(0);
                    questionAnswerFrame.setCustomerScoreVM(ExamUtil.scoreToVM(0));
                    questionAnswerFrame.setDoRight(false);
                    questionAnswerFrame.setDoCheck(false);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 记录试卷核验情况
     *
     * @param error    试卷核验对象
     * @param errorMsg 错误信息
     */
    private void setExamPaperAnswerError(ExamPaperAnswerError error, String errorMsg, boolean passed) {
        try {
            if (passed) {
                //第二次核验通过
                if (ObjectUtils.isNotEmpty(error.getId())) {
                    error.setStatus(ExamErrorConstants.status_success);
                    error.setDeleted(true);
                    examPaperAnswerErrorService.updateExamPaperAnswerError(error);
                }
            } else {
                //第二次核验未通过
                if (ObjectUtils.isNotEmpty(error.getId())) {
                    //更新
                    error.setStatus(ExamErrorConstants.status_fail);
                    error.setErrorMsg(error.getErrorMsg() + errorMsg);
                    examPaperAnswerErrorService.updateExamPaperAnswerError(error);
                } else {
                    //第一次核验未通过
                    List<ExamPaperAnswerError> errorList = examPaperAnswerErrorService.selectExamPaperAnswerErrorList(error);
                    if (errorList.size() == 0) {
                        error.setErrorMsg(errorMsg);
                        error.setStatus(ExamErrorConstants.status_init);
                        examPaperAnswerErrorService.insertExamPaperAnswerError(error);
                    } else if (errorList.size() == 1) {
                        ExamPaperAnswerError update = errorList.get(0);
                        update.setStatus(ExamErrorConstants.status_fail);
                        update.setErrorMsg(update.getErrorMsg() + errorMsg);
                        examPaperAnswerErrorService.updateExamPaperAnswerError(update);
                    } else {
                        for (int i = 0; i < errorList.size(); i++) {
                            ExamPaperAnswerError update = errorList.get(i);
                            if (i == 0) {
                                update.setStatus(ExamErrorConstants.status_fail);
                                update.setErrorMsg(update.getErrorMsg() + errorMsg);
                                examPaperAnswerErrorService.updateExamPaperAnswerError(update);
                            } else {
                                examPaperAnswerErrorService.deleteExamPaperAnswerErrorById(update.getId());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("异步核验实训答案,记录试卷核验情况,业务异常----------------------", e);
            e.printStackTrace();
        }
    }

    /**
     * 异步核验课程培训检查点
     *
     * @param query       用户课件功能点
     * @param currentUser 当前用户
     */
    @Override
    public void checkTrain(TrainItemUserQuestion query, User currentUser) {
        try {
            if (ObjectUtils.isNotEmpty(query) && ObjectUtils.isNotEmpty(query.getTrainId()) && ObjectUtils.isNotEmpty(query.getCourseWareId())) {
                if (ObjectUtils.isNotEmpty(currentUser) && StringUtils.isNotEmpty(currentUser.getUserUuid())) {
                    CourseWare courseWare = courseWareService.getById(query.getCourseWareId());
                    //获取虚拟机
                    VmWare select = new VmWare();
                    select.setVmUserId(currentUser.getUserUuid());
                    select.setVmParentId(courseWare.getVmType());
                    select.setClasses(VmClassesEnum.Train.getCode());
                    //查询已绑定的虚拟机
                    VmWare vmWare = vmWareService.selectVmWare(select);
                    if (ObjectUtils.isNotEmpty(vmWare)) {
                        //获取题目列表
                        List<CourseWareQuestionVM> courseWareQuestionVMList = courseWareService.getCourseWareQuestion(query.getCourseWareId());
                        for (CourseWareQuestionVM courseWareQuestionVM : courseWareQuestionVMList) {
                            QuestionFrame questionFrame = courseWareQuestionVM.getQuestionFrame();
                            boolean checked = questionTrainCheck(questionFrame, vmWare);
                            TrainItemUserQuestion userQuestion = new TrainItemUserQuestion();
                            userQuestion.setTrainId(query.getTrainId());
                            userQuestion.setUserId(currentUser.getId());
                            userQuestion.setCourseWareId(query.getCourseWareId());
                            userQuestion.setQuestionId(questionFrame.getQuestionId());
                            List<TrainItemUserQuestion> list = trainItemUserQuestionMapper.selectTrainItemUserQuestionList(userQuestion);
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    TrainItemUserQuestion item = list.get(i);
                                    if (i == 0) {
                                        //保留第一条
                                        item.setCompletion(checked);
                                        trainItemUserQuestionMapper.updateTrainItemUserQuestion(item);
                                    } else {
                                        //删除多余的
                                        trainItemUserQuestionMapper.deleteTrainItemUserQuestionById(item.getId());
                                    }
                                }
                            } else {
                                //新增一条
                                userQuestion.setCompletion(checked);
                                trainItemUserQuestionMapper.insertTrainItemUserQuestion(userQuestion);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("异步核验课程培训检查点,业务异常----------------------", e);
            e.printStackTrace();
        }
    }

    /**
     * 核验培训课程题目答案
     *
     * @param questionFrame 题目
     * @param vmWare        虚拟机
     */
    private boolean questionTrainCheck(QuestionFrame questionFrame, VmWare vmWare) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        if (questionTypeEnum.getCode() == QuestionTypeEnum.TrainOperate.getCode()) {
            SshParam sshParam = new SshParam();
            sshParam.setHost(vmWare.getVmIp());
            sshParam.setUsername(vmWare.getVmUsername());
            sshParam.setPassword(vmWare.getVmPassword());
            sshParam.setCommand(questionFrame.getCommand());
            logger.info("请求参数:"+ JSONObject.toJSONString(sshParam));
            SshResult sshResult = sshService.executeCommand(sshParam);
            logger.info("返回结果:"+ JSONObject.toJSONString(sshResult));
            if (sshResult.getErrCode() == 0) {
                String result = sshResult.getResult().toString();
                return result.contains(questionFrame.getCorrect());
            }
        }
        return false;
    }


    /**
     * 用户
     */
    private final UserService userService;

    /**
     * 虚拟机
     */
    private final VmWareService vmWareService;

    /**
     * 试卷基本信息
     */
    private final ExamPaperMapper examPaperMapper;

    /**
     * 试卷信息
     */
    private final ExamPaperJsonMapper examPaperJsonMapper;

    /**
     * 答卷基本信息
     */
    private final ExamPaperAnswerMapper examPaperAnswerMapper;

    /**
     * 答卷内容
     */
    private final ExamPaperAnswerJsonMapper examPaperAnswerJsonMapper;

    /**
     * 题目内容
     */
    private final QuestionJsonMapper questionJsonMapper;

    /**
     * 用户合格证书
     */
    private final UserCredentialMapper userCredentialMapper;

    /**
     * ssh模块
     */
    private final SshService sshService;

    /**
     * 试卷核验
     */
    private final ExamPaperAnswerErrorService examPaperAnswerErrorService;

    /**
     * 培训课件
     */
    private final CourseWareService courseWareService;

    /**
     * 用户课件功能点
     */
    private final TrainItemUserQuestionMapper trainItemUserQuestionMapper;
}
