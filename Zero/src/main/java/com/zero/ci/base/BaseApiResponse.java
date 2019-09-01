package com.zero.ci.base;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:
 * -------------------------------
 * 1.
 */

public class BaseApiResponse<T> {


    // public class BaseApiResponse<T extends Parcelable> implements Parcelable {


    private T data;
    private boolean suc;
    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuc() {
        return suc;
    }

    public void setSuc(boolean suc) {
        this.suc = suc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
