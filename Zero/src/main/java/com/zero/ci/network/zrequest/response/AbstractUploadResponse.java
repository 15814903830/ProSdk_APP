package com.zero.ci.network.zrequest.response;


import com.zero.ci.network.zrequest.NetworkManager;
import com.zero.ci.network.http.rest.binary.BasicBinary;
import com.zero.ci.network.http.rest.binary.FileBinary;
import com.zero.ci.network.http.rest.upload.OnUploadListener;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 上传响应
 * -------------------------------
 */
public abstract class AbstractUploadResponse<T> {

    public abstract void onSuccess(T result);

    public void onFailed() {
        NetworkManager.getInstance().getInitializeConfig().getToastFailed().onFailed();
    }

    public void onResponseState(ResponseModel result) {

    }


    //======================下面是文件的回调======================

    /**
     * At the start of the upload is invoked.
     *
     * @param what what of {@link FileBinary#setUploadListener(int, OnUploadListener)}.
     * @see FileBinary#setUploadListener(int, OnUploadListener)
     */
    public void onStart(int what) {
    }


    /**
     * Called when the upload was cancelled.
     *
     * @param what what of {@link FileBinary#setUploadListener(int, OnUploadListener)}.
     * @see FileBinary#setUploadListener(int, OnUploadListener)
     */
    public void onCancel(int what) {
    }


    /**
     * Invoked when the upload progress changes.
     *
     * @param what     what of {@link FileBinary#setUploadListener(int, OnUploadListener)}.
     * @param progress progress
     * @see FileBinary#setUploadListener(int, OnUploadListener)
     */
    public void onProgress(int what, int progress) {
    }


    /**
     * Upload is complete is invoked.
     *
     * @param what what of {@link FileBinary#setUploadListener(int, OnUploadListener)}.
     * @see FileBinary#setUploadListener(int, OnUploadListener)
     */
    public void onFinish(int what) {
    }


    /**
     * Upload error is called.
     *
     * @param what      what of {@link FileBinary#setUploadListener(int, OnUploadListener)}. * @param exception
     *                  upload is called when an error occurs.
     * @param exception error type.
     * @see BasicBinary#setUploadListener(int, OnUploadListener)
     */
    public void onError(int what, Exception exception) {
    }

}

