package com.mindskip.wdd.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.mindskip.wdd.service.UserTokenService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @version 4.1.0
 * @description: 验证码
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/25 10:45
 */
@Controller
@AllArgsConstructor
@RequestMapping(value = "/api/kaptcha")
public class KaptchaController {

    private final Logger logger = LoggerFactory.getLogger(KaptchaController.class);
    private final UserTokenService userTokenService;

    /**
     * 验证码刷新
     */
    @RequestMapping("/refresh")
    public void defaultKaptcha(String code, HttpServletResponse httpServletResponse) {
        if (null == code || !code.matches("([0-9a-fA-F]{8}(-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}?)")) {
            return;
        }
        httpServletResponse.setDateHeader("Expires", 0L);
        httpServletResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        httpServletResponse.addHeader("Cache-Control", "post-check=0, pre-check=0");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setContentType("image/jpeg");
        try (ServletOutputStream out = httpServletResponse.getOutputStream()) {
            LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(93, 37, 0, 10);
            lineCaptcha.setGenerator(new RandomGenerator(4));
            lineCaptcha.setTextAlpha(1f);
            lineCaptcha.write(out);
            userTokenService.saveKaptcha(code, lineCaptcha.getCode());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
