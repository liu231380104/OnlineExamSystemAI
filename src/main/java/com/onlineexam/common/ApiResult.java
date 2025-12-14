package com.onlineexam.common;

import lombok.Data;

/**
 * 统一接口响应结果类
 */
@Data
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
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 成功响应（带提示消息）
     */
    public static ApiResult success(String msg) {
        ApiResult result = new ApiResult();
        result.setCode(200);
        result.setMsg(msg);
        return result;
    }

    /**
     * 成功响应（带数据）
     */
    public static ApiResult success(Object data) {
        ApiResult result = new ApiResult();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 失败响应
     */
    public static ApiResult error(String msg) {
        ApiResult result = new ApiResult();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }

    /**
     * 参数错误响应
     */
    public static ApiResult paramError(String msg) {
        ApiResult result = new ApiResult();
        result.setCode(400);
        result.setMsg(msg);
        return result;
    }
}