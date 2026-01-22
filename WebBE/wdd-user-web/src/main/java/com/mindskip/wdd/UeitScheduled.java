package com.mindskip.wdd;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mindskip.wdd.configuration.ueit.VmWareApiConfig;
import com.mindskip.wdd.configuration.ueit.VmWareConfigKey;
import com.mindskip.wdd.constant.ExamErrorConstants;
import com.mindskip.wdd.domain.enums.ueit.VmStatusEnum;
import com.mindskip.wdd.domain.enums.ueit.VmTypeEnum;
import com.mindskip.wdd.domain.ueit.ExamPaperAnswerError;
import com.mindskip.wdd.domain.ueit.SysConfig;
import com.mindskip.wdd.domain.ueit.VmWare;
import com.mindskip.wdd.repository.SysConfigMapper;
import com.mindskip.wdd.repository.VmWareMapper;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.utility.ueit.HttpUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务
 *
 * @author libl
 * @date 2024-04-10
 */
@Component
@AllArgsConstructor
public class UeitScheduled {

    private final static Logger logger = LoggerFactory.getLogger(UeitScheduled.class);

    /**
     * 定时执行 检查试卷核验记录
     */
//    @Scheduled(cron = "0 0/5 * * * ?")
    private void examErrorTask() {
        try {
            ExamPaperAnswerError select = new ExamPaperAnswerError();
            select.setStatus(ExamErrorConstants.status_init);
            List<ExamPaperAnswerError> list = examPaperAnswerErrorService.selectExamPaperAnswerErrorList(select);
            for (ExamPaperAnswerError error : list) {
                asyncService.checkAnswer(error);
            }
        } catch (Exception e) {
            logger.error("定时任务[{}],业务异常----------------------", "检查试卷核验记录", e);
            e.printStackTrace();
        }
    }

    /**
     * 定时执行 调用虚拟机列表接口查看状态并修改数据库状态
     */
//    @Scheduled(cron = "0 1/2 * * * ?")
    private void getListTask() {
        try {
            HttpResponse response = HttpUtils.sendGet(vmWareApiConfig.getList(), null);
            if (response.isOk()) {
                JSONObject body = JSONObject.parseObject(response.body());
                if (body.containsKey("data")) {
                    JSONObject data = body.getJSONObject("data");
                    if (data.containsKey("items") && data.get("items") instanceof JSONArray) {
                        //虚拟机列表
                        JSONArray items = data.getJSONArray("items");
                        List<VmWare> vmWareList = vmWareMapper.selectList(null);
                        for (VmWare vmWare : vmWareList) {
                            boolean findFlag = false;
                            for (int i = 0; i < items.size(); i++) {
                                //拿到虚拟机名称
                                JSONObject item = items.getJSONObject(i);
                                if (item.containsKey("metadata")) {
                                    JSONObject metadata = item.getJSONObject("metadata");
                                    if (ObjectUtils.isNotEmpty(metadata) && metadata.containsKey("name")) {
                                        String name = metadata.getString("name");
                                        if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(vmWare.getVmName()) && name.equals(vmWare.getVmName())) {
                                            //获取当前虚拟化的状态
                                            if (item.containsKey("status")) {
                                                JSONObject status = item.getJSONObject("status");
                                                String phase = status.getString("phase");
                                                if (StringUtils.isNotEmpty(phase) && ObjectUtils.isNotEmpty(VmStatusEnum.fromEnName(phase))) {
                                                    vmWare.setStatus(VmStatusEnum.fromEnName(phase).getCode());
                                                }
                                                //拿到虚拟化ip
                                                if (status.containsKey("interfaces")) {
                                                    JSONArray interfaces = status.getJSONArray("interfaces");
                                                    if (ObjectUtils.isNotEmpty(interfaces) && interfaces.size() > 0) {
                                                        String ip = interfaces.getJSONObject(0).getString("ipAddress");
                                                        vmWare.setVmIp(ip);
                                                    }
                                                }
                                            }
                                            //保存到数据库
                                            vmWareService.updateVmWare(vmWare);
                                            findFlag = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (!findFlag) {
                                vmWare.setStatus(VmStatusEnum.Shutdown.getCode());
                                vmWareService.updateVmWare(vmWare);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("定时任务[{}],业务异常----------------------", "获取虚拟机列表", e);
            e.printStackTrace();
        }
    }

    /**
     * 定时执行 释放虚拟机
     */
//    @Scheduled(cron = "0 2/5 * * * ?")
    private void releaseTask() {
        VmWare selectDeleted = new VmWare();
        selectDeleted.setVmType(VmTypeEnum.Child.getCode());
        selectDeleted.setDeleted(true);
        List<VmWare> disabledList = vmWareService.selectVmWareList(selectDeleted);
        for (VmWare vmWare : disabledList) {
            vmWareService.releaseVmWare(vmWare);
        }

        VmWare selectExpired = new VmWare();
        selectExpired.setValidEndTime(vmWareService.getExpiredValidEndTime());
        List<VmWare> expiredList = vmWareService.selectExpiredVmWareList(selectExpired);
        for (VmWare vmWare : expiredList) {
            vmWareService.releaseVmWare(vmWare);
        }
    }

    /**
     * 定时执行 获取授权过期时间
     */
//    @Scheduled(cron = "0 2 0 * * ?")
    private void getExpirationTask() {
        try {
            HttpResponse response = HttpUtils.sendGet(vmWareApiConfig.getExpiration(), null);
            if (response.isOk()) {
                JSONObject body = JSONObject.parseObject(response.body());
                if (body.containsKey("license_info")) {
                    JSONObject license_info = body.getJSONObject("license_info");
                    if (license_info.containsKey("expiration_time")) {
                        String expiration_time_str = license_info.getString("expiration_time");
                        String expirationConfigKey = vmWareConfigKey.getExpirationConfigKey();
                        if (StringUtils.isNotEmpty(expiration_time_str) && StringUtils.isNotEmpty(expirationConfigKey)) {
                            SysConfig config = new SysConfig();
                            config.setConfigKey(expirationConfigKey);
                            SysConfig sysConfig = sysConfigMapper.selectConfig(config);
                            if (null != sysConfig) {
                                String configValue = systemService.pairOneEncode(DateUtil.format(DateUtil.parse(expiration_time_str), DatePattern.NORM_DATETIME_PATTERN));
                                sysConfig.setConfigValue(configValue);
                                sysConfigService.updateConfig(sysConfig);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("定时任务[{}],业务异常----------------------", "获取授权过期时间", e);
            e.printStackTrace();
        }
    }

    /**
     * 异步方法
     */
    private final AsyncService asyncService;

    /**
     * 试卷核验
     */
    private final ExamPaperAnswerErrorService examPaperAnswerErrorService;

    /**
     * 虚拟机
     */
    private final VmWareService vmWareService;

    /**
     * 虚拟机
     */
    private final VmWareMapper vmWareMapper;

    /**
     * 参数配置 服务层
     */
    private final SysConfigService sysConfigService;

    /**
     * 参数配置 数据层
     */
    private final SysConfigMapper sysConfigMapper;

    /**
     * 虚拟机接口 配置类
     */
    private final VmWareApiConfig vmWareApiConfig;

    /**
     * 虚拟机缓存键 配置类
     */
    private final VmWareConfigKey vmWareConfigKey;

    /**
     * 系统配置
     */
    private final SystemService systemService;
}
