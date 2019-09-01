package com.zero.ci.network.http.rest.response;

import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.rest.request.Request;

import java.util.List;
import java.util.Set;

/**
 * In response to the class, use generic compatibility with all I to type, and put the parsing operation in
 * {@link Request}.
 */
public class RestResponse<T> implements Response<T> {

    /**
     * Corresponding handle URL.
     */
    private Request<T> request;

    /**
     * Whether from the cache.
     */
    private final boolean isFromCache;

    /**
     * Http response Headers
     */
    private final Headers headers;

    /**
     * Corresponding response results.
     */
    private final T result;
    /**
     * Millisecond of handle.
     */
    private final long mNetworkMillis;
    /**
     * The error message.
     */
    private Exception mException;

    /**
     * Create succeed response.
     *
     * @param request     {@link Request}.
     * @param isFromCache data is come from cache.
     * @param headers     response header.
     * @param result      result.
     * @param millis      handle time.
     * @param e           exception.
     */
    public RestResponse(Request<T> request, boolean isFromCache, Headers headers, T result, long millis, Exception e) {
        this.request = request;
        this.isFromCache = isFromCache;
        this.headers = headers;
        this.result = result;
        this.mNetworkMillis = millis;
        this.mException = e;
    }

    @Override
    public Request<T> request() {
        return request;
    }

    @Override
    public int responseCode() {
        return headers.getResponseCode();
    }

    @Override
    public boolean isSucceed() {
        return this.mException == null;
    }

    @Override
    public boolean isFromCache() {
        return isFromCache;
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    @Override
    public Object getTag() {
        return this.request.getTag();
    }

    @Override
    public T get() {
        return result;
    }

    @Override
    public Exception getException() {
        return mException;
    }

    @Override
    public long getNetworkMillis() {
        return mNetworkMillis;
    }

//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        Headers headers = getHeaders();
//        if (headers != null) {
//            Set<String> keys = headers.keySet();
//            for (String key : keys) {
//                List<String> values = headers.getValues(key);
//                for (String value : values) {
//                    if (key != null) {
//                        builder.append(key).append(": ");
//                    }
//                    builder.append(value).append("\n");
//                }
//            }
//        }
//        T result = get();
//        if (result != null)
//            builder.append(result.toString());
//        myreturn builder.toString();
//    }
}
