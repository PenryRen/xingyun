package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.ueit.SshParam;
import com.mindskip.wdd.domain.ueit.SshResult;
import com.mindskip.wdd.domain.ueit.TrainItemUserQuestion;
import com.mindskip.wdd.service.AsyncService;
import com.mindskip.wdd.service.SshService;
import com.mindskip.wdd.service.VmWareService;
import com.mindskip.wdd.viewmodel.ueit.VmWareVM;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 虚拟机 Controller
 *
 * @author libl
 * @date 2025-04-08
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/vmWare")
public class VmWareController extends BaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(VmWareController.class);

    private final VmWareService vmWareService;

    private final SshService sshService;

    private final AsyncService asyncService;

    /**
     * 刷新授权过期时间
     * 使用拦截器进行校验
     *
     * @return 结果
     */
    @PostMapping("/refreshExpiration")
    public RestResponse refreshExpiration() {
        return RestResponse.ok();
    }

    /**
     * 检查虚拟机分配
     * 类型 00实训 01考试
     *
     * @param query 虚拟机
     * @return 结果
     */
    @PostMapping("/checkVmWare")
    public RestResponse checkVmWare(@RequestBody VmWareVM query) {
        return vmWareService.checkVmWare(getCurrentUser(), query);
    }

    /**
     * 分配虚拟机
     * 类型 00实训 01考试
     *
     * @param query 虚拟机
     * @return 结果
     */
    @PostMapping("/getVmWare")
    public RestResponse getVmWare(@RequestBody VmWareVM query) {
        return vmWareService.getVmWare(getCurrentUser(), query);
    }

    /**
     * 核验课程培训检查点
     *
     * @param query the userQuestion
     * @return the rest response
     */
    @PostMapping("/checkTrain")
    public RestResponse checkTrain(@RequestBody TrainItemUserQuestion query) {
        asyncService.checkTrain(query,getCurrentUser());
        return RestResponse.ok();
    }

    /**
     * 查询虚拟机剩余时间(实训)
     *
     * @param query 虚拟机
     * @return 结果
     */
    @PostMapping("/queryRemainingTime")
    public RestResponse queryRemainingTime(@RequestBody VmWareVM query) {
        return vmWareService.queryRemainingTime(getCurrentUser(), query);
    }

    /**
     * 虚拟机续期(实训)
     *
     * @param query 虚拟机
     * @return 结果
     */
    @PostMapping("/renewal")
    public RestResponse renewal(@RequestBody VmWareVM query) {
        return vmWareService.renewal(getCurrentUser(), query);
    }

    @PostMapping("/ssh")
    public SshResult ssh(@RequestBody SshParam sshParam) {
        return sshService.executeCommand(sshParam);
    }
}
