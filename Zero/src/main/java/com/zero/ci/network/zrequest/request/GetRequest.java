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
 * Get请求
 * ------------------------
 * 1.封装AbstractResponse普通请求
 * 2.封装AdaptResponse适配Rx请求
 */
public class GetRequest extends BaseRequest<GetRequest> {

    public GetRequest(String url) {
        this.url = url;
    }

    public GetRequest(Context context, String url) {
        this.url = url;
        this.context = context;
    }




    public <T> void execute(AbstractResponse<T> l) {
        requestMethod(RequestMethod.GET);
        if (this.mapParams.size() != 0) {
            this.url = Joint(this.url, this.mapParams);
        }
        RequestManager.load(this, l);
    }


    public <T> Observable<T> execute(AdaptResponse<T> l) {
        requestMethod(RequestMethod.GET);
        if (this.mapParams.size() != 0) {
            this.url = Joint(this.url, this.mapParams);
        }
        return RequestManager.load(this, l);
    }




}
