package com.zero.ci.network.zrequest.request;


import android.content.Context;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:
 * 网络请求入口
 * -----------------------------------
 * 1.封装常见post，get，upload，download
 * 2.封装使用RX及HttpURLConnection
 */

public class HttpRequest {

    /**
     * get请求
     *
     * @param context 上下文
     * @param url     请求地址
     * @return {@link GetRequest#GetRequest(Context, String)}
     */
    public static GetRequest get(Context context, String url) {
        return new GetRequest(context, url);
    }

    /**
     * get请求
     * -----------------------------
     * 用于Rx请求
     *
     * @param url 请求地址
     * @return {@link GetRequest#GetRequest(String)}
     */
    public static GetRequest get(String url) {
        return new GetRequest(url);
    }

    /**
     * post 请求
     *
     * @param context 上下文
     * @param url     请求地址
     * @param params  参数
     * @return {@link PostRequest#PostRequest(Context, String, Object)}
     */
    public static <T> PostRequest post(Context context, String url, T params) {
        return new <T>PostRequest(context, url, params);
    }

    /**
     * post 请求
     *
     * @param context 上下文
     * @param url     请求地址
     * @return {@link PostRequest#PostRequest(Context, String)}
     */
    public static <T> PostRequest post(Context context, String url) {
        return new <T>PostRequest(context, url);
    }

    /**
     * post请求
     * -----------------------------
     * 用于Rx请求
     *
     * @param url 请求地址
     * @return {@link PostRequest#PostRequest(String)}
     */
    public static <T> PostRequest post(String url) {
        return new <T>PostRequest(url);
    }


    /**
     * post请求
     * -----------------------------
     * 用于Rx请求
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return {@link PostRequest#PostRequest(String, Object)}
     */
    public static <T> PostRequest post(String url, T param) {
        return new <T>PostRequest(url, param);
    }


    /**
     * 下载请求
     *
     * @param context 上下文
     * @param url     下载地址
     * @return {@link DownloadRequest#DownloadRequest(Context, String)}
     */
    public static DownloadRequest download(Context context, String url) {
        return new DownloadRequest(context, url);
    }

    /**
     * 上传请求
     *
     * @param context 上下文
     * @param url     上传地址
     * @return {@link UploadRequest#UploadRequest(Context, String)}
     */
    public static <T> UploadRequest upload(Context context, String url) {
        return new <T>UploadRequest(context, url);
    }

    /**
     * 上传请求
     *
     * @param context 上下文
     * @param url     上传地址
     * @param params  上传参数
     * @return {@link UploadRequest#UploadRequest(Context, String, Object)}
     */
    public static <T> UploadRequest upload(Context context, String url, T params) {
        return new <T>UploadRequest(context, url, params);
    }

    /**
     * 上传请求
     * -----------------------------
     * 用于Rx请求
     *
     * @param url 上传地址
     * @return {@link UploadRequest#UploadRequest(String, Object)}
     */
    public static <T> UploadRequest upload(String url, T param) {
        return new <T>UploadRequest(url, param);
    }

    /**
     * 上传请求
     * -----------------------------
     * 用于Rx请求
     *
     * @param url 上传地址
     * @return {@link UploadRequest#UploadRequest(String)}
     */
    public static <T> UploadRequest upload(String url) {
        return new <T>UploadRequest(url);
    }


}
