package com.zero.ci.network.zrequest.request;

import android.content.Context;

import com.zero.ci.network.http.RequestMethod;
import com.zero.ci.network.zrequest.NetworkManager;
import com.zero.ci.network.zrequest.response.AbstractResponse;
import com.zero.ci.network.zrequest.response.AdaptResponse;

import io.reactivex.Observable;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:
 * Post请求
 * ------------------------
 * 1.封装AbstractResponse普通请求
 * 2.封装AdaptResponse适配Rx请求
 */
public class PostRequest extends BaseRequest<PostRequest> {


    public PostRequest(String url) {
        this.url = url;
        this.isPostMap = true;
    }

    public <T> PostRequest(String url, T params) {
        this.url = url;
        this.params = NetworkManager.getInstance().getInitializeConfig().getFromJson().onToJson(params);
    }


    public PostRequest(Context context, String url) {
        this.url = url;
        this.context = context;
        this.isPostMap = true;
    }

    public <T> PostRequest(Context context, String url, T params) {
        this.url = url;
        this.context = context;
        this.params = NetworkManager.getInstance().getInitializeConfig().getFromJson().onToJson(params);
    }


    public <T> Observable<T> execute(AbstractResponse<T> l) {
        if (isPostMap) {
            this.params = NetworkManager.getInstance().getInitializeConfig().getFromJson().onToJson(params);
        }
        requestMethod(RequestMethod.POST);
        RequestManager.load(this, l);
        return null;
    }


    public <T> Observable<T> execute(AdaptResponse<T> l) {
        if (isPostMap) {
            this.params = NetworkManager.getInstance().getInitializeConfig().getFromJson().onToJson(mapParams);
        }
        requestMethod(RequestMethod.POST);
        return RequestManager.load(this, l);
    }

}
