package com.ren.factoring.flow.models.response;


import lombok.Data;

/**
 * 业务异常类json
 *
 * @author xuhua
 * @since 1.0.0
 */
@Data
public class JsonResponse {
    private int code;
    private int level;
    private String message;
    private Object data;

    public JsonResponse(Object data) {
        this.code = 200;
        this.message = "success";
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
