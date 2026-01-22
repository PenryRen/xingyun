package com.mindskip.wdd.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.ueit.VmWareApiConfig;
import com.mindskip.wdd.configuration.ueit.VmWareConfigKey;
import com.mindskip.wdd.constant.CacheConstants;
import com.mindskip.wdd.constant.HttpStatus;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.ExamPaperAnswerMonitor;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.ExamPaperTypeEnum;
import com.mindskip.wdd.domain.enums.ueit.VmClassesEnum;
import com.mindskip.wdd.domain.enums.ueit.VmStatusEnum;
import com.mindskip.wdd.domain.enums.ueit.VmTypeEnum;
import com.mindskip.wdd.domain.ueit.*;
import com.mindskip.wdd.mapping.VmWareMapping;
import com.mindskip.wdd.repository.ExamPaperAnswerMapper;
import com.mindskip.wdd.repository.ExamPaperAnswerMonitorMapper;
import com.mindskip.wdd.repository.VmWareMapper;
import com.mindskip.wdd.service.SysConfigService;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.service.VmWareService;
import com.mindskip.wdd.utility.ueit.HttpUtils;
import com.mindskip.wdd.viewmodel.ueit.VmUrl;
import com.mindskip.wdd.viewmodel.ueit.VmWareVM;
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
 * 虚拟机 Service业务层处理
 *
 * @author libl
 * @date 2024-04-07
 */
@Service
@AllArgsConstructor
public class VmWareServiceImpl extends ServiceImpl<VmWareMapper, VmWare> implements VmWareService {

    private static final Logger logger = LoggerFactory.getLogger(VmWareServiceImpl.class);

    private final VmWareMapper vmWareMapper;

    private final VmWareMapping vmWareMapping;

    private final ExamPaperAnswerMonitorMapper monitorMapper;

    private final ExamPaperAnswerMapper answerMapper;

    private final SysConfigService sysConfigService;

    private final VmWareConfigKey vmWareConfigKey;

    private final VmWareApiConfig vmWareApiConfig;

    private final SystemService systemService;

    /**
     * 获取实训环境列表
     *
     * @return 结果
     */
    @Override
    public List<VmWare> vmTypeList() {
        return vmWareMapper.vmTypeList();
    }

    /**
     * 获取授权过期时间
     *
     * @return 结果
     */
    @Override
    public RestResponse getExpiration() {
        String expirationConfigKey = vmWareConfigKey.getExpirationConfigKey();
        if (StringUtils.isNotEmpty(expirationConfigKey)) {
            String encryptStr = sysConfigService.selectConfigByKey(expirationConfigKey);
            if (StringUtils.isNotEmpty(encryptStr)) {
                return RestResponse.ok(systemService.pairOneDecode(encryptStr));
            }
        }
        return RestResponse.ok();
    }

    /**
     * 查询虚拟机列表
     *
     * @param vmWare 虚拟机
     * @return 虚拟机
     */
    @Override
    public List<VmWare> selectVmWareList(VmWare vmWare) {
        return vmWareMapper.selectVmWareList(vmWare);
    }

    /**
     * 查询过期虚拟机列表
     *
     * @param vmWare 虚拟机
     * @return 虚拟机
     */
    @Override
    public List<VmWare> selectExpiredVmWareList(VmWare vmWare) {
        return vmWareMapper.selectExpiredVmWareList(vmWare);
    }

    /**
     * 查询虚拟机(子机)
     *
     * @param vmWare 虚拟机
     * @return 虚拟机
     */
    @Override
    public VmWare selectVmWare(VmWare vmWare) {
        return vmWareMapper.selectVmWare(vmWare);
    }

    /**
     * 查询用户已分配的虚拟机 disabled不为1
     *
     * @param vmWare 虚拟机
     * @return 虚拟机
     */
    @Override
    public VmWare selectBindVmWareEnabled(VmWare vmWare) {
        return vmWareMapper.selectBindVmWareEnabled(vmWare);
    }

    /**
     * 根据虚拟机父ID查询未绑定的虚拟机(子机)
     *
     * @param vmParentId 虚拟机父ID
     * @return 虚拟机
     */
    @Override
    public VmWare selectUnBindVmWareByParentId(String vmParentId) {
        return vmWareMapper.selectUnBindVmWareByParentId(vmParentId);
    }

    /**
     * 根据guid查询虚拟机(主机)
     *
     * @param guid 虚拟机guid
     * @return 虚拟机
     */
    @Override
    public VmWare selectParentVmWareByGuid(String guid) {
        return vmWareMapper.selectParentVmWareByGuid(guid);
    }

    /**
     * 创建虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public int insertVmWare(VmWare vmWare) {
        return vmWareMapper.insertVmWare(vmWare);
    }


    /**
     * 修改虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public int updateVmWare(VmWare vmWare) {
        vmWare.setUpdateTime(new Date());
        return vmWareMapper.updateVmWare(vmWare);
    }

    /**
     * 关闭虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public RestResponse shutdown(VmWare vmWare) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("vmName", vmWare.getVmName());
            params.put("forseRestart", "true");

            HttpResponse httpResponse = HttpUtils.sendPut(vmWareApiConfig.getShutdown(), JSONObject.toJSONString(params));
            String body = httpResponse.body();
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")
                    && jsonObject.containsKey("status") && HttpStatus.SUCCESS == jsonObject.getIntValue("status")) {
                logger.info("关闭虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                VmWare update = new VmWare();
                update.setId(vmWare.getId());
                update.setStatus(VmStatusEnum.Shutdown.getCode());
                this.updateVmWare(update);
                return RestResponse.okMessage("关机成功");
            }
        } catch (Exception e) {
            logger.error("关闭虚拟机,业务异常----------------------", e);
        }
        return RestResponse.failMessage("关机失败");
    }

    /**
     * 开启虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public RestResponse start(VmWare vmWare) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("vmName", vmWare.getVmName());
            params.put("startPaused", "true");

            HttpResponse httpResponse = HttpUtils.sendPut(vmWareApiConfig.getStart(), JSONObject.toJSONString(params));
            String body = httpResponse.body();
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")
                    && jsonObject.containsKey("status") && HttpStatus.SUCCESS == jsonObject.getIntValue("status")) {
                logger.info("开启虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                VmWare update = new VmWare();
                update.setId(vmWare.getId());
                update.setStatus(VmStatusEnum.Running.getCode());
                this.updateVmWare(update);
                return RestResponse.okMessage("开机成功");
            }
        } catch (Exception e) {
            logger.error("开启虚拟机,业务异常----------------------", e);
        }
        return RestResponse.failMessage("开机失败");
    }

    /**
     * 重启虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public RestResponse reStart(VmWare vmWare) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("vmName", vmWare.getVmName());
            params.put("forseRestart", "true");

            HttpResponse httpResponse = HttpUtils.sendPut(vmWareApiConfig.getReStart(), JSONObject.toJSONString(params));
            String body = httpResponse.body();
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")
                    && jsonObject.containsKey("status") && HttpStatus.SUCCESS == jsonObject.getIntValue("status")) {
                logger.info("重启虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                VmWare update = new VmWare();
                update.setId(vmWare.getId());
                update.setStatus(VmStatusEnum.Restart.getCode());
                this.updateVmWare(update);
                return RestResponse.okMessage("重启成功");
            }
        } catch (Exception e) {
            logger.error("重启虚拟机,业务异常----------------------", e);
        }
        return RestResponse.failMessage("重启失败");
    }

    /**
     * 检查虚拟机分配
     * 类型 00实训 01考试
     *
     * @param query 虚拟机
     * @return 结果
     */
    @Override
    public RestResponse checkVmWare(User currentUser, VmWareVM query) {
        try {
            //参数校验
            if (ObjectUtils.isEmpty(currentUser) || StringUtils.isEmpty(currentUser.getUserUuid())) {
                return RestResponse.fail(SystemCode.UNAUTHORIZED.getCode(), SystemCode.UNAUTHORIZED.getMessage());
            }
            if (StringUtils.isEmpty(query.getVmParentId()) || StringUtils.isEmpty(query.getClasses())) {
                return RestResponse.fail(SystemCode.ParameterValidError.getCode(), SystemCode.ParameterValidError.getMessage());
            }
            if (StringUtils.isNotEmpty(query.getClasses()) && query.getClasses().equals(VmClassesEnum.Exam.getCode())) {
                if (ObjectUtils.isEmpty(query.getExamPaperId())) {
                    return RestResponse.fail(SystemCode.ParameterValidError.getCode(), SystemCode.ParameterValidError.getMessage());
                }
            }

            //校验考试是否提交
            if (query.getClasses().equals(VmClassesEnum.Exam.getCode())) {
                ExamPaperAnswer selectAnswer = new ExamPaperAnswer();
                selectAnswer.setCreateUser(currentUser.getId());
                selectAnswer.setPaperType(ExamPaperTypeEnum.OPERATE.getCode());
                selectAnswer.setExamPaperId(query.getExamPaperId());
                List<ExamPaperAnswer> answerList = answerMapper.selectExamPaperAnswerList(selectAnswer);
                if (answerList.size() > 0) {
                    return RestResponse.fail(SystemCode.InnerError.getCode(), "该试卷只能做一次");
                }
            }

            //当前绑定的虚拟机
            VmWare select = new VmWare();
            select.setVmUserId(currentUser.getUserUuid());
            VmWare current = vmWareMapper.selectBindVmWareEnabled(select);

            if (ObjectUtils.isNotEmpty(current)) {
                //检测类别
                if (query.getClasses().equals(VmClassesEnum.Exam.getCode())) {
                    //1.考试
                    if (StringUtils.isNotEmpty(current.getClasses()) && current.getClasses().equals(VmClassesEnum.Exam.getCode())) {
                        //1.1当前为考试
                        if (ObjectUtils.isNotEmpty(current.getExamPaperId()) && ObjectUtils.isNotEmpty(query.getExamPaperId())) {
                            if (current.getExamPaperId().equals(query.getExamPaperId())) {
                                return RestResponse.ok();
                            }  else {
                                return RestResponse.okAlert("您有正在进行的实训考试");
                            }
                        }
                        //校验是否有未提交的考试
                        ExamPaperAnswerMonitor selectExam = new ExamPaperAnswerMonitor();
                        selectExam.setCreateUser(currentUser.getId());
                        selectExam.setPaperType(ExamPaperTypeEnum.OPERATE.getCode());
                        List<ExamPaperAnswerMonitor> list = monitorMapper.selectMonitorList(selectExam);
                        List<ExamPaperAnswerMonitor> differentList = list.stream().filter(item -> !item.getExamPaperId().equals(query.getExamPaperId())).collect(Collectors.toList());
                        if (differentList.size() > 0) {
                            ExamPaperAnswer selectAnswer = new ExamPaperAnswer();
                            selectAnswer.setCreateUser(currentUser.getId());
                            selectAnswer.setPaperType(ExamPaperTypeEnum.OPERATE.getCode());
                            int count = 0;
                            for (ExamPaperAnswerMonitor monitor : differentList) {
                                selectAnswer.setExamPaperId(monitor.getExamPaperId());
                                List<ExamPaperAnswer> answerList = answerMapper.selectExamPaperAnswerList(selectAnswer);
                                if (answerList.size() > 0) {
                                    count++;
                                }
                            }
                            if (count != differentList.size()) {
                                return RestResponse.okAlert("您有正在进行的实训考试");
                            }
                        }
                        return RestResponse.ok();
                    } else if (StringUtils.isNotEmpty(current.getClasses()) && current.getClasses().equals(VmClassesEnum.Train.getCode())) {
                        //1.2当前为实训
                        return RestResponse.okConfirm("您当前分配的实训机器正在进行课程培训，开始考试将会回收实训机器并重新分配");
                    }
                } else if (query.getClasses().equals(VmClassesEnum.Train.getCode())) {
                    //2.实训
                    if (StringUtils.isNotEmpty(current.getClasses()) && current.getClasses().equals(VmClassesEnum.Exam.getCode())) {
                        //2.1当前为考试 默认不进行分配
                        return RestResponse.okAlert("您有正在进行的实训考试");
                    } else if (StringUtils.isNotEmpty(current.getClasses()) && current.getClasses().equals(VmClassesEnum.Train.getCode())) {
                        //2.2当前为实训 是否环境相同
                        if (current.getVmParentId().equals(query.getVmParentId())) {
                            //相同
                            return RestResponse.ok();
                        } else {
                            //不相同 默认解绑并重新分配
                            return RestResponse.okConfirm("进行当前课程将会回收已分配的实训机器");
                        }
                    }
                }
            }
            return RestResponse.ok();
        } catch (Exception e) {
            logger.error("检查虚拟机分配,业务异常----------------------", e);
            e.printStackTrace();
        }
        return RestResponse.fail(SystemCode.InnerError.getCode(), SystemCode.InnerError.getMessage());
    }


    /**
     * 分配虚拟机
     * 类型 00实训 01考试
     *
     * @param currentUser 当前用户
     * @param query       虚拟机
     * @return 结果
     */
    @Override
    public RestResponse getVmWare(User currentUser, VmWareVM query) {
        try {
            //参数校验
            if (ObjectUtils.isEmpty(currentUser) || StringUtils.isEmpty(currentUser.getUserUuid())) {
                return RestResponse.fail(SystemCode.UNAUTHORIZED.getCode(), SystemCode.UNAUTHORIZED.getMessage());
            }
            if (StringUtils.isEmpty(query.getVmParentId()) || StringUtils.isEmpty(query.getClasses())) {
                return RestResponse.fail(SystemCode.ParameterValidError.getCode(), SystemCode.ParameterValidError.getMessage());
            }
            if (StringUtils.isNotEmpty(query.getClasses()) && query.getClasses().equals(VmClassesEnum.Exam.getCode())) {
                if (ObjectUtils.isEmpty(query.getExamPaperId())) {
                    return RestResponse.fail(SystemCode.ParameterValidError.getCode(), SystemCode.ParameterValidError.getMessage());
                }
            }

            //校验考试是否提交
            if (query.getClasses().equals(VmClassesEnum.Exam.getCode())) {
                ExamPaperAnswer selectAnswer = new ExamPaperAnswer();
                selectAnswer.setCreateUser(currentUser.getId());
                selectAnswer.setPaperType(ExamPaperTypeEnum.OPERATE.getCode());
                selectAnswer.setExamPaperId(query.getExamPaperId());
                List<ExamPaperAnswer> answerList = answerMapper.selectExamPaperAnswerList(selectAnswer);
                if (answerList.size() > 0) {
                    return RestResponse.fail(SystemCode.InnerError.getCode(), "该试卷只能做一次");
                }
            }

            //当前绑定的虚拟机
            VmWare select = new VmWare();
            select.setVmUserId(currentUser.getUserUuid());
            VmWare current = vmWareMapper.selectBindVmWareEnabled(select);


            /**
             * 检测类别
             * 1考试
             * 1.1>>>>>>当前为考试 校验是否有未提交的考试
             * ------------------------没有 直接返回
             * ------------------------有 不分配
             * 1.2>>>>>>当前为实训 默认解绑并重新分配
             *
             * 2实训
             * 2.1>>>>>>当前为考试 默认不进行分配
             * 2.2>>>>>>当前为实训 是否环境相同
             * ------------------------相同 直接返回
             * ------------------------不相同 默认解绑并重新分配
             * 3当前什么都不是 直接分配
             */
            if (ObjectUtils.isNotEmpty(current)) {
                //检测类别
                if (query.getClasses().equals(VmClassesEnum.Exam.getCode())) {
                    //1.考试
                    if (StringUtils.isNotEmpty(current.getClasses()) && current.getClasses().equals(VmClassesEnum.Exam.getCode())) {
                        //1.1当前为考试 直接返回
                        if (ObjectUtils.isNotEmpty(current.getExamPaperId()) && ObjectUtils.isNotEmpty(query.getExamPaperId())) {
                            if (current.getExamPaperId().equals(query.getExamPaperId())) {
                                if (ObjectUtils.isEmpty(current.getValidCreateTime())) {
                                    current.setValidCreateTime(DateUtil.date());
                                    this.updateVmWare(current);
                                }
                                VmUrl vmUrl = getVmUrl(current);
                                return RestResponse.ok(vmUrl);
                            }  else {
                                return RestResponse.okAlert("您有正在进行的实训考试");
                            }
                        }
                        //校验是否有未提交的考试
                        ExamPaperAnswerMonitor selectExam = new ExamPaperAnswerMonitor();
                        selectExam.setCreateUser(currentUser.getId());
                        selectExam.setPaperType(ExamPaperTypeEnum.OPERATE.getCode());
                        List<ExamPaperAnswerMonitor> list = monitorMapper.selectMonitorList(selectExam);
                        List<ExamPaperAnswerMonitor> differentList = list.stream().filter(item -> !item.getExamPaperId().equals(query.getExamPaperId())).collect(Collectors.toList());
                        if (differentList.size() > 0) {
                            ExamPaperAnswer selectAnswer = new ExamPaperAnswer();
                            selectAnswer.setCreateUser(currentUser.getId());
                            selectAnswer.setPaperType(ExamPaperTypeEnum.OPERATE.getCode());
                            int count = 0;
                            for (ExamPaperAnswerMonitor monitor : differentList) {
                                selectAnswer.setExamPaperId(monitor.getExamPaperId());
                                List<ExamPaperAnswer> answerList = answerMapper.selectExamPaperAnswerList(selectAnswer);
                                if (answerList.size() > 0) {
                                    count++;
                                }
                            }
                            if (count != differentList.size()) {
                                return RestResponse.okAlert("您有正在进行的实训考试");
                            }
                        }
                        if (ObjectUtils.isEmpty(current.getExamPaperId())) {
                            current.setExamPaperId(query.getExamPaperId());
                            this.updateVmWare(current);
                        }
                        if (ObjectUtils.isEmpty(current.getValidCreateTime())) {
                            current.setValidCreateTime(DateUtil.date());
                            this.updateVmWare(current);
                        }
                        VmUrl vmUrl = getVmUrl(current);
                        return RestResponse.ok(vmUrl);
                    } else if (StringUtils.isNotEmpty(current.getClasses()) && current.getClasses().equals(VmClassesEnum.Train.getCode())) {
                        //1.2当前为实训 默认解绑并重新分配
                        int count = releaseVmWareSync(current);
                        if (count > 0) {
                            return bindVm(currentUser.getUserUuid(), query);
                        }
                        return RestResponse.okAlert("实训机器分配失败，请稍后再试");
                    }
                    return bindVm(currentUser.getUserUuid(), query, current);
                } else if (query.getClasses().equals(VmClassesEnum.Train.getCode())) {
                    //2.实训
                    if (StringUtils.isNotEmpty(current.getClasses()) && current.getClasses().equals(VmClassesEnum.Exam.getCode())) {
                        //2.1当前为考试 默认不进行分配
                        return RestResponse.okAlert("实训机器分配失败，您有正在进行的实训考试");
                    } else if (StringUtils.isNotEmpty(current.getClasses()) && current.getClasses().equals(VmClassesEnum.Train.getCode())) {
                        //2.2当前为实训 是否环境相同
                        if (current.getVmParentId().equals(query.getVmParentId())) {
                            //相同
                            VmUrl vmUrl = getVmUrl(current);
                            return RestResponse.ok(vmUrl);
                        }
                        //不相同 默认解绑并重新分配
                        int count = releaseVmWareSync(current);
                        if (count > 0) {
                            return bindVm(currentUser.getUserUuid(), query);
                        }
                        return RestResponse.okAlert("实训机器分配失败，请稍后再试");
                    }
                    return bindVm(currentUser.getUserUuid(), query, current);
                }
                return RestResponse.okAlert("实训机器分配失败，请稍后再试");
            }
            //新分配
            return bindVm(currentUser.getUserUuid(), query);
        } catch (Exception e) {
            logger.error("分配虚拟机,业务异常----------------------", e);
            e.printStackTrace();
        }
        return RestResponse.okAlert(SystemCode.InnerError.getMessage());
    }


    /**
     * 获取虚拟机url
     *
     * @param vmWare 虚拟机
     * @return 虚拟机url
     */
    private VmUrl getVmUrl(VmWare vmWare) {
        VmUrl vmUrl = new VmUrl();
        String url = vmWare.getUrl();
        String vmName = vmWare.getVmName();
        vmUrl.setUrl(url.replace("/?/", "/" + vmName + "/"));
        vmUrl.setVmGuid(vmWare.getGuid());
        vmUrl.setStatus(vmWare.getStatus());
        if (ObjectUtils.isNotEmpty(vmWare.getStatus()) && !vmWare.getStatus().equals(VmStatusEnum.Running.getCode())) {
            if (StringUtils.isNotEmpty(vmWare.getClasses()) && vmWare.getClasses().equals(VmClassesEnum.Train.getCode())) {
                vmUrl.setMsg("您分配的实训机器正在启动中，是否在当前页面继续等待");
            } else {
                vmUrl.setMsg("您分配的实训机器正在启动中，请耐心等待");
            }
            if (vmWare.getStatus().equals(VmStatusEnum.Shutdown.getCode())) {
                //启动虚拟机
//                start(vmWare);
            }
        }
        return vmUrl;
    }

    /**
     * 绑定虚拟机
     *
     * @param userUuid 用户ID
     * @param query    虚拟机参数
     * @return 结果
     */
    private RestResponse bindVm(String userUuid, VmWareVM query) {
        return bindVm(userUuid, query, null);
    }

    /**
     * 绑定虚拟机
     *
     * @param userUuid 用户ID
     * @param query    虚拟机参数
     * @param current  当前分配的虚拟机
     * @return 结果
     */
    private RestResponse bindVm(String userUuid, VmWareVM query, VmWare current) {
        if (ObjectUtils.isEmpty(current)) {
            //新分配
            current = vmWareMapper.selectUnBindVmWareByParentId(query.getVmParentId());
        }
        if (ObjectUtils.isNotEmpty(current)) {
            current.setVmUserId(userUuid);
            current.setClasses(query.getClasses());
            current.setDisabled(false);
            current.setExamPaperId(query.getExamPaperId());
            current.setDeleted(false);
            DateTime now = DateUtil.date();
            current.setValidCreateTime(now);
            if (query.getClasses().equals(VmClassesEnum.Train.getCode())) {
                DateTime end = DateUtil.offsetMinute(now, getTrainTimeLimit());
                current.setValidEndTime(end);
            } else {
                current.setValidEndTime(null);
            }
            int count = this.updateVmWare(current);
            if (count > 0) {
                VmUrl vmUrl = getVmUrl(current);
                return RestResponse.ok(vmUrl);
            }
            return RestResponse.okAlert("实训机器分配失败，请稍后再试");
        }
        //核验实训机器台数限制
        String encryptStr = sysConfigService.selectConfigByKey(vmWareConfigKey.getMaxCountConfigKey());
        if (StringUtils.isEmpty(encryptStr)) {
            return RestResponse.okAlert("当前暂无可分配的实训机器，请稍后再试");
        }
        String maxCount = systemService.pairOneDecode(encryptStr);
        int count = vmWareMapper.selectCount(null).intValue();
        if (count >= Integer.parseInt(maxCount)) {
            return RestResponse.okAlert("当前暂无可分配的实训机器，请稍后再试");
        }
        //克隆虚拟机
        cloneVmWare(userUuid, query);
        return RestResponse.okAlert("正在为您分配实训机器，请稍后再试");
    }

    /**
     * 异步克隆虚拟机
     *
     * @param userUuid 用户Uuid
     * @param query    虚拟机参数
     */
    @Async
    @Override
    public void cloneVmWare(String userUuid, VmWareVM query) {
        try {
            String guid = query.getVmParentId();
            //根据guid查询虚拟机(主机)
            VmWare parentVmWare = vmWareMapper.selectParentVmWareByGuid(guid);
            if (ObjectUtils.isEmpty(parentVmWare)) {
                logger.error("异步克隆虚拟机,获取主机信息失败");
                return;
            }
            String childGuid = UUID.randomUUID().toString();
            String nowStr = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
            String childVmName = nowStr + "-" + parentVmWare.getVmName() + "-" + childGuid;

            //获取配置信息
            String rootConfigKey = vmWareConfigKey.getRootConfigKey();
            String diskConfigKey = vmWareConfigKey.getDiskConfigKey();
            if (StringUtils.isNotEmpty(rootConfigKey) && StringUtils.isNotEmpty(diskConfigKey)) {
                String rootConfig = sysConfigService.selectConfigByKey(rootConfigKey);
                String diskConfig = sysConfigService.selectConfigByKey(diskConfigKey);
                if (StringUtils.isNotEmpty(rootConfig) && StringUtils.isNotEmpty(diskConfig)) {
                    Root root = JSONObject.parseObject(rootConfig, Root.class);
                    Disk disk = JSONObject.parseObject(diskConfig, Disk.class);
                    boolean checked = checkVmConfig(root, disk);
                    if (checked) {
                        //封装请求参数
                        root.setName(childVmName);
                        root.setDataSource(parentVmWare.getDiskName());
                        disk.setName(childVmName);
                        List<Disk> diskList = new ArrayList<>();
                        diskList.add(disk);
                        root.setDisk(diskList);
                        logger.info("异步克隆虚拟机,开始----------------------");
                        HttpResponse httpResponse = HttpUtils.sendPost(vmWareApiConfig.getClone(), JSONObject.toJSONString(root));
                        if (httpResponse.isOk()) {
                            String body = httpResponse.body();
                            JSONObject jsonObject = JSONObject.parseObject(body);
                            if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")
                                    && jsonObject.containsKey("status") && HttpStatus.SUCCESS == jsonObject.getIntValue("status")) {
                                logger.info("异步克隆虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                                //克隆成功保存虚拟化
                                VmWareClone vmWareClone = vmWareMapping.toVmWareClone(parentVmWare);
                                VmWare insert = vmWareMapping.toVmWare(vmWareClone);
                                insert.setVmType(VmTypeEnum.Child.getCode());
                                insert.setGuid(childGuid);
                                insert.setVmParentId(guid);
                                insert.setVmName(childVmName);
                                insert.setVmCpu(root.getCpu());
                                insert.setVmStorage(root.getMem());
                                insert.setDiskName(disk.getName());
                                insert.setDiskSize(disk.getSize());
                                insert.setVmUserId(userUuid);
                                insert.setStatus(VmStatusEnum.Init.getCode());
                                insert.setClasses(query.getClasses());
                                insert.setCreateTime(new Date());
                                insert.setDisabled(false);
                                insert.setExamPaperId(query.getExamPaperId());
                                insert.setDeleted(false);
                                //保存
                                vmWareMapper.insertVmWare(insert);
                            } else {
                                logger.error("异步克隆虚拟机,接口返回失败信息:[{}]", httpResponse.body());
                            }
                        } else {
                            logger.error("异步克隆虚拟机,接口请求失败:[{}]", httpResponse.body());
                        }
                    }
                } else {
                    logger.error("异步克隆虚拟机,获取虚拟机配置信息失败");
                }
            } else {
                logger.error("异步克隆虚拟机,获取虚拟机配置键名失败");
            }
        } catch (Exception e) {
            logger.error("异步克隆虚拟机,业务异常----------------------", e);
            e.printStackTrace();
        }
    }

    /**
     * 校验请求参数
     *
     * @param root 虚拟机基础信息
     * @param disk 虚拟机硬盘信息
     * @return 是否通过
     */
    private boolean checkVmConfig(Root root, Disk disk) {
        if (ObjectUtils.isNotEmpty(root) && ObjectUtils.isNotEmpty(disk)) {
            if (StringUtils.isNotEmpty(root.getOsType()) && ObjectUtils.isNotEmpty(root.getCpu())
                    && StringUtils.isNotEmpty(root.getMem())) {
                if (ObjectUtils.isNotEmpty(disk.getBoot_order()) && StringUtils.isNotEmpty(disk.getType())
                        && StringUtils.isNotEmpty(disk.getBus()) && StringUtils.isNotEmpty(disk.getSize())
                        && StringUtils.isNotEmpty(disk.getMountType()) && StringUtils.isNotEmpty(disk.getStorageclassName())
                        && StringUtils.isNotEmpty(disk.getAccessMode())) {
                    return true;
                } else {
                    logger.error("异步克隆新虚拟机,虚拟机硬盘信息不全");
                }
            } else {
                logger.error("异步克隆新虚拟机,虚拟机基础信息不全");
            }
        }
        return false;
    }

    /**
     * 释放虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public int releaseVmWareSync(VmWare vmWare) {
        int count = 0;
        try {
            //设置删除状态
            vmWare.setVmUserId("");
            vmWare.setClasses("");
            vmWare.setValidCreateTime(null);
            vmWare.setValidEndTime(null);
            vmWare.setDisabled(true);
            vmWare.setDeleted(true);
            this.updateVmWare(vmWare);

            //封装请求参数
            VmWareDelete vmWareDelete = new VmWareDelete();
            vmWareDelete.setVmName(vmWare.getVmName());
            List<String> volumes = new ArrayList<>();
            volumes.add(vmWare.getVmStorage());
            vmWareDelete.setVolumes(volumes);

            //调用接口
            HttpResponse httpResponse = HttpUtils.sendDelete(vmWareApiConfig.getDelete(), JSONObject.toJSONString(vmWareDelete));
            if (httpResponse.isOk()) {
                String body = httpResponse.body();
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")) {
                    logger.info("异步删除虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                    count = vmWareMapper.deleteVmWareById(vmWare.getId());
                    if (count > 0) {
                        logger.info("异步删除虚拟机,删除虚拟机成功");
                    }
                } else {
                    logger.error("异步删除虚拟机,接口返回失败信息:[{}]", httpResponse.body());
                }
            } else {
                logger.error("异步删除虚拟机,接口请求失败:[{}]", httpResponse.body());
            }
        } catch (Exception e) {
            logger.error("异步删除虚拟机,业务异常----------------------", e);
        }
        return count;
    }

    /**
     * 异步释放虚拟机
     *
     * @param vmWare 虚拟机
     * @return 结果
     */
    @Override
    public void releaseVmWare(VmWare vmWare) {
        try {
            //设置删除状态
            vmWare.setVmUserId("");
            vmWare.setClasses("");
            vmWare.setValidCreateTime(null);
            vmWare.setValidEndTime(null);
            vmWare.setDisabled(true);
            vmWare.setDeleted(true);
            this.updateVmWare(vmWare);

            //封装请求参数
            VmWareDelete vmWareDelete = new VmWareDelete();
            vmWareDelete.setVmName(vmWare.getVmName());
            List<String> volumes = new ArrayList<>();
            volumes.add(vmWare.getVmStorage());
            vmWareDelete.setVolumes(volumes);

            //调用接口
            HttpResponse httpResponse = HttpUtils.sendDelete(vmWareApiConfig.getDelete(), JSONObject.toJSONString(vmWareDelete));
            if (httpResponse.isOk()) {
                String body = httpResponse.body();
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (jsonObject.containsKey("code") && HttpStatus.SUCCESS == jsonObject.getIntValue("code")) {
                    logger.info("异步删除虚拟机,接口返回成功信息:[{}]", httpResponse.body());
                    int count = vmWareMapper.deleteVmWareById(vmWare.getId());
                    if (count > 0) {
                        logger.info("异步删除虚拟机,删除虚拟机成功");
                    }
                } else {
                    logger.error("异步删除虚拟机,接口返回失败信息:[{}]", httpResponse.body());
                }
            } else {
                logger.error("异步删除虚拟机,接口请求失败:[{}]", httpResponse.body());
            }
        } catch (Exception e) {
            logger.error("异步删除虚拟机,业务异常----------------------", e);
        }
    }

    /**
     * 查询虚拟机剩余时间(实训)
     *
     * @param currentUser 当前用户
     * @param query       虚拟机
     * @return 结果
     */
    @Override
    public RestResponse queryRemainingTime(User currentUser, VmWareVM query) {
        //参数校验
        if (ObjectUtils.isEmpty(currentUser) || StringUtils.isEmpty(currentUser.getUserUuid())) {
            return RestResponse.fail(SystemCode.UNAUTHORIZED.getCode(), SystemCode.UNAUTHORIZED.getMessage());
        }
        VmWare select = new VmWare();
        select.setGuid(query.getGuid());
        select.setVmUserId(currentUser.getUserUuid());
        select.setClasses(VmClassesEnum.Train.getCode());
        VmWare current = vmWareMapper.selectBindVmWareEnabled(select);
        if (ObjectUtils.isNotEmpty(current)) {
            if (ObjectUtils.isEmpty(current.getValidEndTime())) {
                if (ObjectUtils.isEmpty(current.getValidCreateTime())) {
                    current.setValidCreateTime(DateUtil.date());
                }
                current.setValidEndTime(DateUtil.offsetMinute(current.getValidCreateTime(), getTrainTimeLimit()));
                this.updateVmWare(current);
            }
            RemainingTime remainingTime = new RemainingTime();
            remainingTime.setRemainTime(Math.max((int) DateUtil.between(new Date(), current.getValidEndTime(), DateUnit.SECOND, false), 0));
            remainingTime.setSingleSecond(getTrainTimeLimit() * 60);
            return RestResponse.ok(remainingTime);
        }
        return RestResponse.ok();
    }

    /**
     * 虚拟机续期(实训)
     *
     * @param currentUser 当前用户
     * @param query       虚拟机
     * @return 结果
     */
    @Override
    public RestResponse renewal(User currentUser, VmWareVM query) {
        //参数校验
        if (ObjectUtils.isEmpty(currentUser) || StringUtils.isEmpty(currentUser.getUserUuid())) {
            return RestResponse.fail(SystemCode.UNAUTHORIZED.getCode(), SystemCode.UNAUTHORIZED.getMessage());
        }
        VmWare select = new VmWare();
        select.setGuid(query.getGuid());
        select.setVmUserId(currentUser.getUserUuid());
        select.setClasses(VmClassesEnum.Train.getCode());
        VmWare current = vmWareMapper.selectBindVmWareEnabled(select);
        if (ObjectUtils.isNotEmpty(current)) {
            if (ObjectUtils.isEmpty(current.getValidEndTime())) {
                if (ObjectUtils.isEmpty(current.getValidCreateTime())) {
                    current.setValidCreateTime(DateUtil.date());
                }
                current.setValidEndTime(DateUtil.offsetMinute(current.getValidCreateTime(), getTrainTimeLimit()));
            } else {
                DateTime now = DateUtil.date();
                //过期
                long remainTime = DateUtil.between(now, current.getValidEndTime(), DateUnit.SECOND, false);
                if (remainTime < 0) {
                    current.setValidEndTime(DateUtil.offsetMinute(now, getTrainTimeLimit()));
                } else {
                    if (remainTime > getTrainTimeLimit() * 60) {
                        return RestResponse.okAlert("当前剩余时间充足，请勿频繁续期");
                    }
                    current.setValidEndTime(DateUtil.offsetMinute(current.getValidEndTime(), getTrainTimeLimit()));
                }
            }
            int count = this.updateVmWare(current);
            if (count > 0) {
                return RestResponse.okMessage("续期成功");
            }
            return RestResponse.failMessage("续期失败");
        }
        return RestResponse.okConfirm("当前实训机器已回收，是否重新申请资源");
    }

    /**
     * 实训虚拟机时间限制 单位(分钟)
     *
     * @return 分钟
     */
    private int getTrainTimeLimit() {
        try {
            String trainTimeLimitConfigKey = vmWareConfigKey.getTrainTimeLimitConfigKey();
            String trainTimeLimit = sysConfigService.selectConfigByKey(trainTimeLimitConfigKey);
            if (StringUtils.isNotEmpty(trainTimeLimit)) {
                return Integer.parseInt(trainTimeLimit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取实训虚拟机时间限制,业务异常----------------------", e);
        }
        return CacheConstants.TRAIN_TIME_LIMIT_DEFAULT;
    }

    /**
     * 实训虚拟机过期时间
     *
     * @return 分钟
     */
    public DateTime getExpiredValidEndTime() {
        int expiredTimeLimit = getExpiredTimeLimit();
        return DateUtil.offsetMinute(DateUtil.date(), -expiredTimeLimit);
    }

    /**
     * 实训虚拟机过期时间限制 单位(分钟)
     *
     * @return 分钟
     */
    private int getExpiredTimeLimit() {
        try {
            String expiredTimeLimitConfigKey = vmWareConfigKey.getExpiredTimeLimitConfigKey();
            String expiredTimeLimit = sysConfigService.selectConfigByKey(expiredTimeLimitConfigKey);
            if (StringUtils.isNotEmpty(expiredTimeLimit)) {
                return Integer.parseInt(expiredTimeLimit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取实训虚拟机时间限制,业务异常----------------------", e);
        }
        return CacheConstants.EXPIRED_TIME_LIMIT_DEFAULT;
    }
}
