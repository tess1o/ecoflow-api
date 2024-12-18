package com.github.tess1o.ecoflow.client.http.response;

public class EcoflowApiResponse<T> {

    private String code;
    private String message;
    private T data;
    private String eagleEyeTraceId;
    private String tid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getEagleEyeTraceId() {
        return eagleEyeTraceId;
    }

    public void setEagleEyeTraceId(String eagleEyeTraceId) {
        this.eagleEyeTraceId = eagleEyeTraceId;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
