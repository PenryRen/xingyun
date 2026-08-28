package com.mindskip.wdd.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.aliyun.AliyunEcdProperties;
import com.mindskip.wdd.configuration.ueit.VmWareConfigKey;
import com.mindskip.wdd.constant.CacheConstants;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.ExamPaperAnswerMonitor;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.ExamPaperTypeEnum;
import com.mindskip.wdd.domain.enums.ueit.VmClassesEnum;
import com.mindskip.wdd.domain.enums.ueit.VmStatusEnum;
import com.mindskip.wdd.domain.enums.ueit.VmTypeEnum;
import com.mindskip.wdd.domain.ueit.*;
import com.mindskip.wdd.repository.ExamPaperAnswerMapper;
import com.mindskip.wdd.repository.ExamPaperAnswerMonitorMapper;
import com.mindskip.wdd.repository.VmWareMapper;
import com.mindskip.wdd.service.SysConfigService;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.service.VmWareService;
import com.mindskip.wdd.service.aliyun.AliyunEcdDesktopClient;
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
 * 无影云桌面（按需创建）业务处理；库表仍使用 t_vmware，模板行 vm_type=00，url 存 BundleId。
 *
 * @author libl
 * @date 2025-04-07
 */
@Service
@AllArgsConstructor
public class VmWareServiceImpl extends ServiceImpl<VmWareMapper, VmWare> implements VmWareService {

    private static final Logger logger = LoggerFactory.getLogger(VmWareServiceImpl.class);

    private final VmWareMapper vmWareMapper;

    private final ExamPaperAnswerMonitorMapper monitorMapper;

    private final ExamPaperAnswerMapper answerMapper;

    private final SysConfigService sysConfigService;

    private final VmWareConfigKey vmWareConfigKey;

    private final SystemService systemService;

    private final AliyunEcdDesktopClient aliyunEcdDesktopClient;

    private final AliyunEcdProperties aliyunEcdProperties;

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
        try {
            String expirationConfigKey = vmWareConfigKey.getExpirationConfigKey();
            if (StringUtils.isNotEmpty(expirationConfigKey)) {
                String encryptStr = sysConfigService.selectConfigByKey(expirationConfigKey);
                if (StringUtils.isNotEmpty(encryptStr)) {
                    return RestResponse.ok(systemService.pairOneDecode(encryptStr));
                }
            }
        } catch (Exception e) {
            logger.warn("getExpiration failed: {}", e.getMessage());
        }
        return RestResponse.ok("2099-12-31T23:59:59Z");
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
        if (!aliyunEcdProperties.isConfigured() || StringUtils.isEmpty(vmWare.getGuid())) {
            return RestResponse.failMessage("关机失败");
        }
        try {
            aliyunEcdDesktopClient.stopDesktops(Collections.singletonList(vmWare.getGuid()));
            VmWare update = new VmWare();
            update.setId(vmWare.getId());
            update.setStatus(VmStatusEnum.Shutdown.getCode());
            this.updateVmWare(update);
            return RestResponse.okMessage("关机成功");
        } catch (Exception e) {
            logger.error("关闭云电脑,业务异常----------------------", e);
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
        if (!aliyunEcdProperties.isConfigured() || StringUtils.isEmpty(vmWare.getGuid())) {
            return RestResponse.failMessage("开机失败");
        }
        try {
            aliyunEcdDesktopClient.startDesktops(Collections.singletonList(vmWare.getGuid()));
            VmWare update = new VmWare();
            update.setId(vmWare.getId());
            update.setStatus(VmStatusEnum.Running.getCode());
            this.updateVmWare(update);
            return RestResponse.okMessage("开机成功");
        } catch (Exception e) {
            logger.error("开启云电脑,业务异常----------------------", e);
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
        if (!aliyunEcdProperties.isConfigured() || StringUtils.isEmpty(vmWare.getGuid())) {
            return RestResponse.failMessage("重启失败");
        }
        try {
            aliyunEcdDesktopClient.rebootDesktops(Collections.singletonList(vmWare.getGuid()));
            VmWare update = new VmWare();
            update.setId(vmWare.getId());
            update.setStatus(VmStatusEnum.Restart.getCode());
            this.updateVmWare(update);
            return RestResponse.okMessage("重启成功");
        } catch (Exception e) {
            logger.error("重启云电脑,业务异常----------------------", e);
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
     * 组装无影 Web SDK 所需连接信息；子机 url 字段存 EndUserId。
     */
    private VmUrl getVmUrl(VmWare vmWare) {
        VmUrl vmUrl = new VmUrl();
        vmUrl.setUseWuyingWebSdk(true);
        vmUrl.setVmGuid(vmWare.getGuid());
        vmUrl.setDesktopName(vmWare.getVmName());
        vmUrl.setRealDesktopId(vmWare.getGuid());
        vmUrl.setLoginRegionId(aliyunEcdProperties.getRegionId());
        vmUrl.setWuyingOpenType(aliyunEcdProperties.getWuyingOpenType());
        if (StringUtils.isNotEmpty(aliyunEcdProperties.getWuyingResourceType())) {
            vmUrl.setWuyingResourceType(aliyunEcdProperties.getWuyingResourceType());
        }
        vmUrl.setEndUserId(vmWare.getUrl());

        String aliStatus = null;
        if (aliyunEcdProperties.isConfigured() && StringUtils.isNotEmpty(vmWare.getGuid())) {
            try {
                aliStatus = aliyunEcdDesktopClient.describeDesktopStatus(vmWare.getGuid());
            } catch (Exception e) {
                logger.warn("DescribeDesktops 失败 desktopId={} {}", vmWare.getGuid(), e.getMessage());
            }
        }
        String localStatus = mapAliyunStatusToLocal(aliStatus, vmWare.getStatus());
        vmUrl.setStatus(localStatus);

        if (aliyunEcdProperties.isConfigured() && StringUtils.isNotEmpty(vmWare.getUrl())
                && VmStatusEnum.Running.getCode().equals(localStatus)) {
            try {
                vmUrl.setAuthCode(aliyunEcdDesktopClient.getWebAuthCode(vmWare.getUrl()));
            } catch (Exception e) {
                logger.warn("GetAuthCode 失败 {}", e.getMessage());
            }
        }

        if (!VmStatusEnum.Running.getCode().equals(localStatus)) {
            if (StringUtils.isNotEmpty(vmWare.getClasses()) && vmWare.getClasses().equals(VmClassesEnum.Train.getCode())) {
                vmUrl.setMsg("云电脑未处于运行中，请稍后刷新或联系管理员");
            } else {
                vmUrl.setMsg("云电脑未处于运行中，请稍后再试");
            }
        }
        return vmUrl;
    }

    private static String mapAliyunStatusToLocal(String aliStatus, String dbFallback) {
        if (StringUtils.isEmpty(aliStatus)) {
            return dbFallback;
        }
        switch (aliStatus) {
            case "Running":
                return VmStatusEnum.Running.getCode();
            case "Stopped":
            case "Stopping":
                return VmStatusEnum.Shutdown.getCode();
            default:
                return VmStatusEnum.Scheduled.getCode();
        }
    }

    private static String toEndUserId(String userUuid) {
        if (StringUtils.isEmpty(userUuid)) {
            return "user-unknown";
        }
        String raw = userUuid.replace("-", "").toLowerCase(Locale.ROOT);
        if (raw.length() > 24) {
            raw = raw.substring(0, 24);
        }
        return "u" + raw;
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
        if (!aliyunEcdProperties.isConfigured()) {
            return RestResponse.okAlert("未配置阿里云无影云桌面，请联系管理员配置 aliyun.ecd");
        }
        if (ObjectUtils.isNotEmpty(current) && VmTypeEnum.Child.getCode().equals(current.getVmType())
                && StringUtils.isNotEmpty(current.getGuid())) {
            current.setVmUserId(userUuid);
            current.setClasses(query.getClasses());
            current.setDisabled(false);
            current.setExamPaperId(query.getExamPaperId());
            current.setDeleted(false);
            DateTime now = DateUtil.date();
            current.setValidCreateTime(now);
            if (query.getClasses().equals(VmClassesEnum.Train.getCode())) {
                current.setValidEndTime(DateUtil.offsetMinute(now, getTrainTimeLimit()));
            } else {
                current.setValidEndTime(null);
            }
            int u = this.updateVmWare(current);
            if (u > 0) {
                return RestResponse.ok(getVmUrl(current));
            }
            return RestResponse.okAlert("实训机器分配失败，请稍后再试");
        }
        VmWare template = vmWareMapper.selectParentVmWareByGuid(query.getVmParentId());
        if (ObjectUtils.isEmpty(template) || StringUtils.isEmpty(template.getUrl())) {
            return RestResponse.okAlert("实训环境模板未配置 BundleId（请在模板行 url 中填写）");
        }
        String bundleId = template.getUrl().trim();

        String encryptStr = sysConfigService.selectConfigByKey(vmWareConfigKey.getMaxCountConfigKey());
        if (StringUtils.isEmpty(encryptStr)) {
            return RestResponse.okAlert("当前暂无可分配资源，请稍后再试");
        }
        String maxCount = systemService.pairOneDecode(encryptStr);
        int exist = vmWareMapper.selectCount(null).intValue();
        if (exist >= Integer.parseInt(maxCount)) {
            return RestResponse.okAlert("当前暂无可分配资源，请稍后再试");
        }

        String endUserId = toEndUserId(userUuid);
        String desktopName = "desk-" + System.currentTimeMillis() + "-" + endUserId;
        try {
            String desktopId = aliyunEcdDesktopClient.createDesktop(bundleId, desktopName, endUserId);
            VmWare row = new VmWare();
            row.setVmType(VmTypeEnum.Child.getCode());
            row.setGuid(desktopId);
            row.setVmParentId(query.getVmParentId());
            row.setVmName(desktopName);
            row.setVmUserId(userUuid);
            row.setUrl(endUserId);
            row.setStatus(VmStatusEnum.Scheduled.getCode());
            row.setClasses(query.getClasses());
            row.setExamPaperId(query.getExamPaperId());
            row.setDisabled(false);
            row.setDeleted(false);
            DateTime now = DateUtil.date();
            row.setValidCreateTime(now);
            if (query.getClasses().equals(VmClassesEnum.Train.getCode())) {
                row.setValidEndTime(DateUtil.offsetMinute(now, getTrainTimeLimit()));
            }
            row.setCreateTime(new Date());
            vmWareMapper.insertVmWare(row);
            return RestResponse.ok(getVmUrl(row));
        } catch (Exception e) {
            logger.error("CreateDesktops 失败", e);
            return RestResponse.okAlert("创建云电脑失败：" + e.getMessage());
        }
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
        logger.warn("cloneVmWare 已废弃：云桌面改为同步 CreateDesktops，忽略调用 userUuid={}", userUuid);
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
            vmWare.setVmUserId("");
            vmWare.setClasses("");
            vmWare.setValidCreateTime(null);
            vmWare.setValidEndTime(null);
            vmWare.setDisabled(true);
            vmWare.setDeleted(true);
            this.updateVmWare(vmWare);

            if (aliyunEcdProperties.isConfigured() && VmTypeEnum.Child.getCode().equals(vmWare.getVmType())
                    && StringUtils.isNotEmpty(vmWare.getGuid())) {
                aliyunEcdDesktopClient.deleteDesktops(Collections.singletonList(vmWare.getGuid()));
            }
            count = vmWareMapper.deleteVmWareById(vmWare.getId());
        } catch (Exception e) {
            logger.error("释放云电脑,业务异常----------------------", e);
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
        releaseVmWareSync(vmWare);
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
