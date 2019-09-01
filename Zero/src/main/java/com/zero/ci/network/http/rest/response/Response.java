package com.zero.ci.network.http.rest.response;

import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.rest.request.Request;

/**
 * Http response, Including header information and response packets.
 *
 * @param <T> The handle data type, it should be with the {@link Request}, {@link OnResponseListener}.
 */
public interface Response<T> {

    /**
     * Get the Request.
     */
    Request<T> request();

    /**
     * Get the response code of handle.
     *
     * @return response code.
     */
    int responseCode();

    /**
     * Request is executed successfully.
     *
     * @return True: Succeed, false: failed.
     */
    boolean isSucceed();

    /**
     * Whether the data returned from the cache.
     *
     * @return True: the data from cache, false: the data from server.
     */
    boolean isFromCache();

    /**
     * Get http response headers.
     *
     * @return {@link Headers}.
     */
    Headers getHeaders();

    /**
     * Get handle results.
     *
     * @return {@link T}.
     */
    T get();

    /**
     * When the handle fail to getList the exception type.
     *
     * @return The exception.
     */
    Exception getException();

    /**
     * Gets the tag of handle.
     *
     * @return {@link Object}.
     */
    Object getTag();

    /**
     * Gets the millisecond of handle.
     *
     * @return {@link Long}.
     */
    long getNetworkMillis();
}