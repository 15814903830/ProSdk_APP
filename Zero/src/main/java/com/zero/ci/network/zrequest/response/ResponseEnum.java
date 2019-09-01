package com.zero.ci.network.zrequest.response;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:响应状态
 * -------------------------------
 */
public enum ResponseEnum {
    BEGIN(0),
    SUCCESS(1),
    FAILED(2),
    FINISH(3);

    public int state;

    ResponseEnum(int state) {
        this.state = state;
    }
}
