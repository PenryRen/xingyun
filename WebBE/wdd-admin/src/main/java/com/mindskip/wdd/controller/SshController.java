package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.ueit.SshParam;
import com.mindskip.wdd.domain.ueit.SshResult;
import com.mindskip.wdd.service.SshService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description: ssh模块接口
 */

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/ssh")
public class SshController {

    private final SshService sshService;

    /**
     * ssh执行命令测试接口
     * @param param ssh参数
     * @return 执行命令返回结果
     */
    @PostMapping("/test")
    public RestResponse<SshResult> test(@RequestBody SshParam param){
        SshResult sshResult = sshService.executeCommand(param);
        return RestResponse.ok(sshResult);
    }
}
