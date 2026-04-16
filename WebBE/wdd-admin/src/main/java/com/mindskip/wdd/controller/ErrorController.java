package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.SystemCode;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 异常控制器
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
public class ErrorController extends BasicErrorController {

    /**
     * Instantiates a new Error controller.
     */
    public ErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>(2);
        error.put("code", SystemCode.InnerError.getCode());
        error.put("message", SystemCode.InnerError.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }
}
