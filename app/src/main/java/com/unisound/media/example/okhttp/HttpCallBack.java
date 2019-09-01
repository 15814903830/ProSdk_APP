package com.unisound.media.example.okhttp;

public interface HttpCallBack {

    void onResponse(String response, int requestId);

    void onHandlerMessageCallback(String response, int requestId);

}
