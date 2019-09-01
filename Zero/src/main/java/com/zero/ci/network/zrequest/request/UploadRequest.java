package com.zero.ci.network.zrequest.request;

import android.content.Context;

import com.zero.ci.widget.logger.Logger;
import com.zero.ci.network.http.RequestMethod;
import com.zero.ci.network.zrequest.NetworkManager;
import com.zero.ci.network.zrequest.response.AbstractUploadResponse;
import com.zero.ci.network.zrequest.response.AdaptResponse;

import io.reactivex.Observable;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:上传请求
 * -------------------------------
 * 1.封装AbstractResponse普通请求
 * 2.封装AdaptResponse适配Rx请求
 */
public class UploadRequest extends BaseRequest<UploadRequest> {
    public <T> UploadRequest(Context context, String url, T params) {
        this.url = url;
        this.context = context;
        this.params = NetworkManager.getInstance().getInitializeConfig().getFromJson().onToJson(params);
    }

    public <T> UploadRequest(Context context, String url) {
        this.url = url;
        this.context = context;
        this.isPostMap = true;
    }

    public <T> UploadRequest(String url) {
        this.url = url;
        this.isPostMap = true;
    }

    public <T> UploadRequest(String url, T params) {
        this.url = url;
        this.params = NetworkManager.getInstance().getInitializeConfig().getFromJson().onToJson(params);

    }

    public <T> void execute(AbstractUploadResponse<T> l) {
        if (isPostMap) {
            this.params = NetworkManager.getInstance().getInitializeConfig().getFromJson().onToJson(mapParams);
        }

        requestMethod(RequestMethod.POST);
        RequestManager.upload(this, l);
    }


    public <T> Observable<T> execute(AdaptResponse<T> l) {
        if (isPostMap) {
            this.params = NetworkManager.getInstance().getInitializeConfig().getFromJson().onToJson(mapParams);
        }
        return RequestManager.upload(this, l);
    }
}