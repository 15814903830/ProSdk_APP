package com.zero.ci.network.zrequest.response;

import java.io.Serializable;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:响应结果
 * -------------------------------
 */
public class ResponseModel implements Serializable {

    private ResponseEnum state;
    private Throwable exception;

    public ResponseModel(ResponseEnum state) {
        this.state = state;
    }

    public ResponseModel(ResponseEnum state, Throwable exception) {
        this.state = state;
        this.exception = exception;
    }

    public ResponseEnum getState() {
        return state;
    }

    public void setState(ResponseEnum state) {
        this.state = state;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "state=" + state +
                ", exception=" + exception +
                '}';
    }
}
