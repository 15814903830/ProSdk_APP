package com.zero.ci.network.http.rest.upload;

import com.zero.ci.network.http.rest.binary.BasicBinary;
import com.zero.ci.network.http.rest.binary.FileBinary;

public interface OnUploadListener {

    /**
     * At the start of the upload is invoked.
     *
     * @param what what of {@link FileBinary#setUploadListener(int, OnUploadListener)}.
     * @see FileBinary#setUploadListener(int, OnUploadListener)
     */
    void onStart(int what);

    /**
     * Called when the upload was cancelled.
     *
     * @param what what of {@link FileBinary#setUploadListener(int, OnUploadListener)}.
     * @see FileBinary#setUploadListener(int, OnUploadListener)
     */
    void onCancel(int what);

    /**
     * Invoked when the upload progress changes.
     *
     * @param what     what of {@link FileBinary#setUploadListener(int, OnUploadListener)}.
     * @param progress progress
     * @see FileBinary#setUploadListener(int, OnUploadListener)
     */
    void onProgress(int what, int progress);

    /**
     * Upload is complete is invoked.
     *
     * @param what what of {@link FileBinary#setUploadListener(int, OnUploadListener)}.
     * @see FileBinary#setUploadListener(int, OnUploadListener)
     */
    void onFinish(int what);

    /**
     * Upload error is called.
     *
     * @param what      what of {@link FileBinary#setUploadListener(int, OnUploadListener)}. * @param exception
     *                  upload is called when an error occurs.
     * @param exception error type.
     * @see BasicBinary#setUploadListener(int, OnUploadListener)
     */
    void onError(int what, Exception exception);
}
