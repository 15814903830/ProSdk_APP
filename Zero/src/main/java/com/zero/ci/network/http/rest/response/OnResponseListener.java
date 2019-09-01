package com.zero.ci.network.http.rest.response;

import com.zero.ci.network.http.rest.request.Request;

/**
 * @param <T> a generic, on behalf of you can accept the result type, .It should be with the {@link Request},
 *            {@link Response}.
 */
public interface OnResponseListener<T> {

    /**
     * When the handle starts.
     *
     * @param what the credit of the incoming handle is used to distinguish between multiple requests.
     */
    void onStart(int what);

    /**
     * Server correct response to callback when an HTTP handle.
     *
     * @param what     the credit of the incoming handle is used to distinguish between multiple requests.
     * @param response successful callback.
     */
    void onSucceed(int what, Response<T> response);

    /**
     * When there was an error correction.
     *
     * @param what     the credit of the incoming handle is used to distinguish between multiple requests.
     * @param response failure callback.
     */
    void onFailed(int what, Response<T> response);

    /**
     * When the handle finish.
     *
     * @param what the credit of the incoming handle is used to distinguish between multiple requests.
     */
    void onFinish(int what);

}