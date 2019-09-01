package com.zero.ci.network.zrequest.request;

import android.text.TextUtils;

import com.zero.ci.network.http.RequestMethod;
import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.rest.request.Request;
import com.zero.ci.network.http.rest.request.StringRequest;
import com.zero.ci.network.zrequest.NetworkManager;

import java.lang.reflect.Type;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 实体请求
 * -----------------------------------
 */

public class EntityRequest<T> extends Request<T> {
    private Type mType;


    public EntityRequest(String url, RequestMethod requestMethod, Type type) {
        super(url, requestMethod);
        this.mType = type;
    }

    @Override
    public T parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String result = StringRequest.parseResponseString(responseHeaders, responseBody);
        if (TextUtils.isEmpty(result)) {
            throw new NullPointerException("EntityRequest------result  null");
        } else {
            return NetworkManager.getInstance().getInitializeConfig().getFromJson().onFromJson(result, mType);
        }
    }
}
