package com.zero.ci.network.http.rest;

import com.zero.ci.network.http.rest.request.Request;
import com.zero.ci.network.http.rest.request.RequestHandler;
import com.zero.ci.network.http.rest.response.Response;

public interface Interceptor {

    /**
     * When any one request will be launched.
     */
    <T> Response<T> intercept(RequestHandler handler, Request<T> request);
}
