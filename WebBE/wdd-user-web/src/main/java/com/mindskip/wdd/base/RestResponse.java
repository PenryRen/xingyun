package com.mindskip.wdd.base;

/**
 * @version 1.7.0
 * @description: 接口返回基类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public class RestResponse<T> {
    private int code;
    private String message;
    private T response;

    public RestResponse() {
    }

    /**
     * Instantiates a new Rest response.
     *
     * @param code    the code
     * @param message the message
     */
    public RestResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Instantiates a new Rest response.
     *
     * @param code     the code
     * @param message  the message
     * @param response the response
     */
    public RestResponse(int code, String message, T response) {
        this.code = code;
        this.message = message;
        this.response = response;
    }

    /**
     * 失败返回
     *
     * @param code the code
     * @param msg  the msg
     * @return the rest response
     */
    public static RestResponse<String> fail(Integer code, String msg) {
        return new RestResponse<>(code, msg);
    }

    public static RestResponse<String> failMessage(String message) {
        SystemCode systemCode = SystemCode.InnerError;
        return new RestResponse<>(systemCode.getCode(), message);
    }

    public static RestResponse<String> okMessage(String message) {
        SystemCode systemCode = SystemCode.OK;
        return new RestResponse<>(systemCode.getCode(), message);
    }

    /**
     * 正确返回
     *
     * @return the rest response
     */
    public static RestResponse<String> ok() {
        SystemCode systemCode = SystemCode.OK;
        return new RestResponse<>(systemCode.getCode(), systemCode.getMessage());
    }

    /**
     * 正确返回
     *
     * @return the rest response
     */
    public static RestResponse<String> ok(int code, String message) {
        return new RestResponse<>(code, message);
    }

    public static RestResponse<String> okAlert(String message) {
        return new RestResponse<>(2, message);
    }

    public static RestResponse<String> okConfirm(String message) {
        return new RestResponse<>(3, message);
    }

    /**
     * Ok rest response.
     *
     * @param <F>      the type parameter
     * @param response the response
     * @return the rest response
     */
    public static <F> RestResponse<F> ok(F response) {
        SystemCode systemCode = SystemCode.OK;
        return new RestResponse<>(systemCode.getCode(), systemCode.getMessage(), response);
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public T getResponse() {
        return response;
    }

    /**
     * Sets response.
     *
     * @param response the response
     */
    public void setResponse(T response) {
        this.response = response;
    }
}
