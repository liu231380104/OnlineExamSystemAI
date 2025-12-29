package com.onlineexam.common;

/**
 * 统一接口响应结果类
 */
public class ApiResult {
    // 响应状态码：200成功，400参数错误，500服务器错误
    private int code;
    // 响应提示消息
    private String msg;
    // 响应数据（可选）
    private Object data;

    /**
     * 成功响应（无数据）
     */
    public static ApiResult success() {
        ApiResult result = new ApiResult();
        result.code = 200;
        result.msg = "操作成功";
        return result;
    }

    /**
     * 成功响应（带提示消息）
     */
    public static ApiResult success(String msg) {
        ApiResult result = new ApiResult();
        result.code = 200;
        result.msg = msg;
        return result;
    }

    /**
     * 成功响应（带数据）
     */
    public static ApiResult success(Object data) {
        ApiResult result = new ApiResult();
        result.code = 200;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    /**
     * 失败响应
     */
    public static ApiResult error(String msg) {
        ApiResult result = new ApiResult();
        result.code = 500;
        result.msg = msg;
        return result;
    }

    /**
     * 参数错误响应
     */
    public static ApiResult paramError(String msg) {
        ApiResult result = new ApiResult();
        result.code = 400;
        result.msg = msg;
        return result;
    }

    // Getter和Setter方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}