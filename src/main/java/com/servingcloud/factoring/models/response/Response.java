package com.servingcloud.factoring.models.response;

import com.servingcloud.factoring.models.enmu.IReturnCode;
import com.servingcloud.factoring.models.enmu.ResultCode;

public class Response<T> {
    public static final Integer OK = 1;
    public static final Integer Fail = -1;

    private int code;
    private String msg;
    private T data;

    public static Response ok(String msg) {
        return new Response(OK, msg);
    }

    public static <T> Response ok(String msg, T data) {
        return new Response(OK, msg, data);
    }

    public static <T> Response ok(T data) {
        return new Response(OK, "操作成功", data);
    }

    public static Response failed(String message) {
        return new Response(Fail, message);
    }

    public static Response failed(IReturnCode returnCode) {
        return new Response(returnCode.getCode(), returnCode.getMsg());
    }

    public Response() {
        this(ResultCode.SUCCESS, null);
    }

    public Response(int code) {
        this(code, "", null);
    }

    public Response(T data) {
        this(ResultCode.SUCCESS, data);
    }

    public Response(int code, String message) {
        this(code, message, null);
    }

    public Response(IReturnCode resultCode) {
        this(resultCode, null);
    }

    public Response(IReturnCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = data;
    }

    public Response(int code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Response{code=" + this.code + ", msg='" + this.msg + '\'' + ", result=" + this.data + '}';
    }
}
