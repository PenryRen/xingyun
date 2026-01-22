package com.mindskip.wdd.configuration.spring.exception;

import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.utility.ErrorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 系统异常配置
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@ControllerAdvice
public class ExceptionHandle {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    /**
     * 异常返回
     *
     * @param e the e
     * @return the rest response
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestResponse handler(Exception e) {
        logger.error(e.getMessage(), e);
        return new RestResponse<>(SystemCode.InnerError.getCode(), SystemCode.InnerError.getMessage());
    }


    /**
     * 参数异常返回
     *
     * @param e the e
     * @return the rest response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public RestResponse handler(MethodArgumentTypeMismatchException e) {
        logger.error(e.getMessage(), e);
        return new RestResponse<>(SystemCode.ParameterValidError.getCode(), SystemCode.ParameterValidError.getMessage());
    }

    /**
     * 参数序列化异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public RestResponse handler(HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        return new RestResponse<>(SystemCode.ParameterValidError.getCode(), SystemCode.ParameterValidError.getMessage());
    }


    /**
     * 参数异常返回
     *
     * @param e the e
     * @return the rest response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public RestResponse handler(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getAllErrors().stream().map(file -> {
            FieldError fieldError = (FieldError) file;
            return ErrorUtil.parameterErrorFormat(fieldError.getField(), fieldError.getDefaultMessage());
        }).collect(Collectors.joining());
        return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
    }

    /**
     * 参数异常返回
     *
     * @param e the e
     * @return the rest response
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public RestResponse handler(BindException e) {
        String errorMsg = e.getBindingResult().getAllErrors().stream().map(file -> {
            FieldError fieldError = (FieldError) file;
            return ErrorUtil.parameterErrorFormat(fieldError.getField(), fieldError.getDefaultMessage());
        }).collect(Collectors.joining());
        return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
    }


    /**
     * 文件上传大小超过限制
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public RestResponse handler(MaxUploadSizeExceededException e) {
        return new RestResponse<>(SystemCode.UploadLimit.getCode(), SystemCode.UploadLimit.getMessage());
    }


}
