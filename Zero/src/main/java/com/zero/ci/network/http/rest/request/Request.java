package com.zero.ci.network.http.rest.request;

import android.text.TextUtils;

import com.zero.ci.network.http.RequestMethod;
import com.zero.ci.network.http.rest.CacheMode;
import com.zero.ci.network.http.rest.headers.Headers;

/**
 * Support the characteristics of the queue.
 * 支持队列的特性
 */
public abstract class Request<Result> extends BasicRequest<Request> {
    /**
     * Cache key.
     */
    private String mCacheKey;
    /**
     * If just read from cache.
     */
    private CacheMode mCacheMode = CacheMode.DEFAULT;

    /**
     * Create a handle, handle method is {@link RequestMethod#GET}.
     *
     * @param url handle address
     */
    public Request(String url) {
        this(url, RequestMethod.GET);
    }

    /**
     * Create a handle
     *
     * @param url           handle address
     * @param requestMethod handle method, like {@link RequestMethod#GET}, {@link RequestMethod#POST}.
     */
    public Request(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    /**
     * Set the handle cache primary key, it should be globally unique.
     * 设置唯一缓存key（注：全局唯一）
     *
     * @param key unique key.
     */
    public Request setCacheKey(String key) {
        this.mCacheKey = key;
        return this;
    }

    /**
     * Get key of cache data.
     * 获取缓存key
     *
     * @return cache key.
     */
    public String getCacheKey() {
        return TextUtils.isEmpty(mCacheKey) ? url() : mCacheKey;
    }

    /**
     * Set the cache mode.
     * 设置缓存模式
     *
     * @param cacheMode The value from {@link CacheMode}. 缓存模式
     */
    public Request setCacheMode(CacheMode cacheMode) {
        this.mCacheMode = cacheMode;
        return this;
    }

    /**
     * He got the handle cache mode.
     * 获取缓存模式
     *
     * @return value from {@link CacheMode}. 缓存模式
     */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /**
     * Parse handle results for generic objects.
     * 解析通过句柄对象的结果
     *
     * @param responseHeaders response headers of server.
     *                        服务器响应标头
     * @param responseBody    response data of server.
     *                        服务器响应数据
     * @return your response result.返回数据结果
     * @throws Exception parse error. 解析出错
     */
    public abstract Result parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception;

}
