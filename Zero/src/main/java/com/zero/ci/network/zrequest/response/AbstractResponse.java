package com.zero.ci.network.zrequest.response;


import com.zero.ci.network.zrequest.NetworkManager;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:响应
 * -------------------------------
 */
public abstract class AbstractResponse<T> {
    public abstract void onSuccess(T result);

    public void onFailed() {
        NetworkManager.getInstance().getInitializeConfig().getToastFailed().onFailed();
    }

    public void onResponseState(ResponseModel result) {

    }

}
