package com.unisound.media.example.okhttp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * okhttp请求工具
 */
public class OkHttpUtils {

    /**
     * 懒汉 安全 加同步
     * 私有的静态成员变量 只声明不创建
     * 私有的构造方法
     * 提供返回实例的静态方法
     */
    private static OkHttpClient okHttpClient = null;

    private OkHttpUtils() {
    }

    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            //加同步安全
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * get请求
     * 参数1 url
     * 参数2 回调Callback
     */

    public static void doGet(String url, final HttpCallBack callback, final int requestId) {
        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = new Request.Builder().url(url).build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onResponse(e.toString(), requestId);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response.body().string(), requestId);
            }
        });
    }

    /**
     * get请求
     * 参数1 url
     * 参数2 回调Callback
     */

    public static void doGet(String url, List<RequestParams> params, final HttpCallBack callback, final int requestId) {
        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //遍历集合
        for (int i = 0; i < params.size(); i++) {
            String key = params.get(i).getKey();
            String value = params.get(i).getValue();
            if (i == 0) {
                url = url + "?" + key + "=" + value;
            } else {
                url = url + "&" + key + "=" + value;
            }
        }
        //创建Request
        Request request = new Request.Builder().url(url).build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onResponse(e.toString(), requestId);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response.body().string(), requestId);
            }
        });
    }

    /**
     * post请求
     * 参数1 url
     * 参数2 回调Callback
     */

    public static void doPost(String url, List<RequestParams> params, final HttpCallBack callback, final int requestId) {
        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //3.x版本post请求换成FormBody 封装键值对参数
        FormBody.Builder builder = new FormBody.Builder();
        //遍历集合
        for (RequestParams param : params) {
            builder.add(param.getKey(), param.getValue());
        }
        //创建Request
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onResponse(e.toString(), requestId);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response.body().string(), requestId);
            }
        });

    }

    /**
     * post请求上传文件
     * 参数1 url
     * 参数2 回调Callback
     */
    public static void uploadFile(String url, File file, String fileName, final HttpCallBack callback, final int requestId) {
        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建RequestBody 封装file参数
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //创建RequestBody 设置类型等
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", fileName, fileBody).build();
        //创建Request
        Request request = new Request.Builder().url(url).post(requestBody).build();

        //得到Call
        Call call = okHttpClient.newCall(request);
        //执行请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onResponse(e.toString(), requestId);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response.body().string(), requestId);
            }
        });

    }

    /**
     * Post请求发送JSON数据
     * 参数一：请求Url
     * 参数二：请求的JSON
     * 参数三：请求回调
     */
    public static void doPostJson(String url, String jsonParams, final HttpCallBack callback, final int requestId) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onResponse(e.toString(), requestId);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response.body().string(), requestId);
            }
        });
    }

}
