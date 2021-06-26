package org.snowzen.model;

import lombok.Getter;

/**
 * @author snow-zen
 */
@Getter
public class ApiResult {

    /**
     * 默认成功响应代码
     */
    private static final int DEFAULT_SUCCESS_CODE = 200;

    /**
     * 默认成功响应消息
     */
    private static final String DEFAULT_SUCCESS_MSG = "成功";

    /**
     * 响应代码
     */
    private final int code;

    /**
     * 响应消息
     */
    private final String msg;

    /**
     * 响应数据结果
     */
    private final Object result;

    private ApiResult(int code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }


    public static ApiResult success(Object result) {
        return success(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG, result);
    }

    /**
     * 成功响应
     *
     * @param result 返回结果
     * @return 成功响应对象
     */
    public static ApiResult success(int code, String msg, Object result) {
        return new ApiResult(code, msg, result);
    }

    /**
     * 失败响应
     *
     * @param code 响应代码
     * @param msg  响应失败消息
     * @return 失败响应对象
     */
    public static ApiResult fail(int code, String msg) {
        return new ApiResult(code, msg, null);
    }
}
