package com.zero.ci.network.http.rest.upload;

public abstract class SimpleUploadListener implements OnUploadListener {
    @Override
    public void onStart(int what) {
    }

    @Override
    public void onProgress(int what, int progress) {
    }

    @Override
    public void onCancel(int what) {
    }

    @Override
    public void onFinish(int what) {
    }

    @Override
    public void onError(int what, Exception exception) {
    }
}
