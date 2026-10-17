package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
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
}
