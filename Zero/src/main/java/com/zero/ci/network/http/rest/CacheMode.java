package com.zero.ci.network.http.rest;

/**
 * caching pattern, the default value is {@link CacheMode#DEFAULT}, other value may be
 * {@link CacheMode#REQUEST_NETWORK_FAILED_READ_CACHE}, {@link CacheMode#ONLY_READ_CACHE},
 * {@link CacheMode#ONLY_REQUEST_NETWORK}, {@link CacheMode#NONE_CACHE_REQUEST_NETWORK}.
 */
public enum CacheMode {

    /**
     * The default mode, according to the standard HTTP protocol cache, such as response header is 304.
     * 没有设置缓存模式时的默认模式 这个模式实现http协议中的内容，比如响应码是304时，当然还会结合E-Tag和LastModify等头。
     * StringRequest request = new StringRequest(url, method);
     * request.setCacheMode(CacheMode.DEFAULT);
     */
    DEFAULT,

    /**
     * If the handle is successful myreturn data server, if the handle is returned failure cache data, if does not
     * cache the data failed.
     * 当请求服务器失败的时候，读取缓存 请求服务器成功则返回服务器数据，如果请求服务器失败，读取缓存数据返回。
     */
    REQUEST_NETWORK_FAILED_READ_CACHE,

    /**
     * If there is no cache handle, it returns the cache cache exists.
     * 如果发现有缓存直接成功，没有缓存才请求服务器
     */
    NONE_CACHE_REQUEST_NETWORK,

    /**
     * If the cache exists, the handle is successful, other wise is failed.
     * 仅仅读取缓存 无论如何仅仅读取缓存，不会请求网络和其它操作。
     */
    ONLY_READ_CACHE,

    /**
     * Just handle to the server, can't read cache anyway, also won't add cache related to head to the handle.
     * 仅仅请求网络 无论如何也只会请求网络，也不支持http 304这种默认行为。
     */
    ONLY_REQUEST_NETWORK
}