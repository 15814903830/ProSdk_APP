package com.zero.ci.network.zrequest.conver;


import com.zero.ci.network.http.rest.request.Request;


public interface IHeader {

    <T> void onHeader(Request<T> request);
}
