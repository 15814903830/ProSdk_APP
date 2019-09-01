package com.zero.ci.network.http.rest.response;

/**
 * Simple {@link SimpleResponseListener}.
 */
public abstract class SimpleResponseListener<T> implements OnResponseListener<T> {
    @Override
    public void onStart(int what) {
    }

    @Override
    public void onSucceed(int what, Response<T> response) {
    }

    @Override
    public void onFailed(int what, Response<T> response) {
    }

    @Override
    public void onFinish(int what) {
    }
}
