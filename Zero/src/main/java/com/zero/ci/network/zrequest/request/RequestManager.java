package com.zero.ci.network.zrequest.request;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.zero.ci.network.http.NoHttp;
import com.zero.ci.network.http.RequestMethod;
import com.zero.ci.network.http.rest.binary.BasicBinary;
import com.zero.ci.network.http.rest.binary.FileBinary;
import com.zero.ci.network.http.rest.binary.InputStreamBinary;
import com.zero.ci.network.http.rest.download.DownloadListener;
import com.zero.ci.network.http.rest.download.DownloadQueue;
import com.zero.ci.network.http.rest.download.DownloadRequest;
import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.rest.request.Request;
import com.zero.ci.network.http.rest.request.RequestQueue;
import com.zero.ci.network.http.rest.request.StringRequest;
import com.zero.ci.network.http.rest.request.SyncRequestExecutor;
import com.zero.ci.network.http.rest.response.OnResponseListener;
import com.zero.ci.network.http.rest.response.Response;
import com.zero.ci.network.http.rest.upload.OnUploadListener;
import com.zero.ci.network.zrequest.NetworkConfig;
import com.zero.ci.network.zrequest.NetworkManager;
import com.zero.ci.network.zrequest.conver.INetDialog;
import com.zero.ci.network.zrequest.response.AbstractResponse;
import com.zero.ci.network.zrequest.response.AbstractUploadResponse;
import com.zero.ci.network.zrequest.response.AdaptResponse;
import com.zero.ci.network.zrequest.response.ResponseEnum;
import com.zero.ci.network.zrequest.response.ResponseModel;
import com.zero.ci.network.zrequest.response.UploadFile;
import com.zero.ci.widget.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 请求管理
 * -------------------------------
 * 1. 请求队列
 * 2. 下载队列
 */
public class RequestManager {


    private static RequestQueue mRequestQueue;
    private static DownloadQueue mDownloadQueue;

    private RequestManager() {

    }

    /**
     * 数据请求的Queue
     *
     * @return 返回请求队列
     */
    private static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            synchronized (RequestManager.class) {
                mRequestQueue = NoHttp.newRequestQueue(NetworkManager.getInstance().getInitializeConfig().getThreadPoolSize());
            }
        }

        return mRequestQueue;
    }

    /**
     * 文件下载的Queue
     *
     * @return 返回下载队列
     */
    private static DownloadQueue getDownloadQueue() {
        if (mDownloadQueue == null) {
            synchronized (RequestManager.class) {
                mDownloadQueue = NoHttp.newDownloadQueue(NetworkManager.getInstance().getInitializeConfig().getThreadPoolSize());
            }
        }
        return mDownloadQueue;
    }

    /**
     * 上传文件
     *
     * @param params                  参数
     * @param tAbstractUploadResponse 回调
     */
    public static <T> void upload(final BaseRequest params, final AbstractUploadResponse<T> tAbstractUploadResponse) {
        final NetworkConfig networkConfig = NetworkManager.getInstance().getInitializeConfig();
        final INetDialog iDialog = networkConfig.getDialog();

        Type type = ((ParameterizedType) tAbstractUploadResponse.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        final Request<T> request;
        if (type == String.class) {
            request = (Request<T>) NoHttp.createStringRequest(params.url, params.requestMethod);
        } else {
            request = new EntityRequest<>(params.url, params.requestMethod, type);
        }

        if (RequestMethod.POST == params.requestMethod) {
            request.setDefineRequestBodyForJson(params.params);
        }
        if (params.jsonString != null) {
            request.setDefineRequestBodyForJson(params.jsonString);
        }

        request.add(params.mapParams);
        SSLContext sslContext = networkConfig.getSSLContext();
        if (sslContext != null) {
            request.setSSLSocketFactory(sslContext.getSocketFactory());
        }
        if (networkConfig.getHeader() != null) {
            networkConfig.getHeader().onHeader(request);
        }
        /**
         * 给上传文件做个监听，可以不需要
         */
        List<UploadFile> files = params.uploadFiles;
        for (UploadFile file : files) {

            /**
             * 需要注意
             * FileBinary这里支持多种上传方式
             * 如：FileBinary
             *    BitmapBinary
             *    InputStreamBinary
             *    这里只处理一种 因为一个项目应该不会出现2种上传类型的方式
             *    如果自己的项目用的BitmapBinary 只需要把 UploadFile里面的类型修改就行了
             *
             * BasicBinary binary3 = new InputStreamBinary(new FileInputStream(file3), file3.getName());
             * BasicBinary binary2 = new BitmapBinary(file2, "userHead.jpg");
             *  或者：BasicBinary binary2 = new BitmapBinary(file2, null);
             *
             */
            BasicBinary basicBinary = null;
            if (file.getMode() instanceof File) {
                basicBinary = new FileBinary((File) file.getMode(), file.getKey());
            } else if (file.getMode() instanceof FileInputStream) {
                basicBinary = new InputStreamBinary((FileInputStream) file.getMode(), file.getKey());
            }
            request.add(file.getKey(), basicBinary);


            basicBinary.setUploadListener(file.getWhat(), new OnUploadListener() {
                @Override
                public void onStart(int what) {
                    tAbstractUploadResponse.onStart(what);
                }

                @Override
                public void onCancel(int what) {
                    tAbstractUploadResponse.onCancel(what);
                }

                @Override
                public void onProgress(int what, int progress) {
                    tAbstractUploadResponse.onProgress(what, progress);
                }

                @Override
                public void onFinish(int what) {
                    tAbstractUploadResponse.onFinish(what);
                }

                @Override
                public void onError(int what, Exception exception) {
                    tAbstractUploadResponse.onError(what, exception);
                }
            });
        }

        if (iDialog != null) {
            iDialog.init(params.context);
            iDialog.setCancelable(params.isCloseDialog);
            if (!TextUtils.isEmpty(params.loadingTitle)) {
                iDialog.setMessage(params.loadingTitle);
            }
        }
        request.setTag(params.context);
        request.setCancelSign(params.context);

        getRequestQueue().add(params.what, request, new OnResponseListener<T>() {
            @Override
            public void onStart(int what) {
                tAbstractUploadResponse.onResponseState(new ResponseModel(ResponseEnum.BEGIN));
                if (iDialog != null && params.isLoading)
                    iDialog.show();
            }

            @Override
            public void onSucceed(int what, Response<T> response) {
                tAbstractUploadResponse.onResponseState(new ResponseModel(ResponseEnum.SUCCESS));
                tAbstractUploadResponse.onSuccess(response.get());
                if (networkConfig.getIDevelopMode() != null) {
                    networkConfig.getIDevelopMode().onRecord(params, response.get());
                }
            }

            @Override
            public void onFailed(int what, Response<T> response) {
                tAbstractUploadResponse.onResponseState(new ResponseModel(ResponseEnum.FAILED, response.getException()));
                tAbstractUploadResponse.onFailed();

                if (networkConfig.getIDevelopMode() != null) {
                    networkConfig.getIDevelopMode().onRecord(params, response.get());
                }

            }

            @Override
            public void onFinish(int what) {
                if (iDialog != null && params.isLoading)
                    iDialog.dismiss();
                tAbstractUploadResponse.onResponseState(new ResponseModel(ResponseEnum.FINISH));
            }
        });
    }

    /**
     * 上传
     *
     * @param params         请求配置
     * @param tAdaptResponse 回调
     * @return {@link Observable#create(ObservableOnSubscribe)}
     */
    public static <T> Observable<T> upload(final BaseRequest params, final AdaptResponse<T> tAdaptResponse) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                NetworkConfig networkConfig = NetworkManager.getInstance().getInitializeConfig();
                try {
                    Request<String> request = new StringRequest(params.url, RequestMethod.POST);
                    request.setConnectTimeout(params.timeOut);
                    request.setRetryCount(params.retry);
                    if (params.jsonString != null) {
                        request.setDefineRequestBodyForJson(params.jsonString);
                    }
                    if (networkConfig.getHeader() != null) {
                        networkConfig.getHeader().onHeader(request);
                    }
                    request.add(params.mapParams);
                    if (params.headerParam.size() != 0) {
                        request.add(params.headerParam);
                    }
                    SSLContext sslContext = networkConfig.getSSLContext();
                    if (sslContext != null) {
                        request.setSSLSocketFactory(sslContext.getSocketFactory());
                    }

                    /**
                     * 上传文件
                     */
                    List<UploadFile> files = params.uploadFiles;
                    for (UploadFile file : files) {
                        BasicBinary basicBinary = null;
                        if (file.getMode() instanceof File) {
                            basicBinary = new FileBinary((File) file.getMode(), file.getKey());
                        } else if (file.getMode() instanceof FileInputStream) {
                            basicBinary = new InputStreamBinary((FileInputStream) file.getMode(), file.getKey());
                        }
                        request.add(file.getKey(), basicBinary);

//                        byte[] buffer = new byte[(int) basicBinary.getLength()];
//                        String encode64 = android.util.Base64.encodeToString(buffer, android.util.Base64.DEFAULT);
//                        request.add(file.getKey(), encode64);
                    }


                    final Response<String> response = SyncRequestExecutor.INSTANCE.execute(request);
                    if (response.isSucceed()) {
                        String json = response.get();

                        Type type = ((ParameterizedType) tAdaptResponse.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        if (type == String.class) {
                            e.onNext((T) json);
                        } else {
                            e.onNext((T) networkConfig.getFromJson().onFromJson(json, type));
                        }

                    } else {
                        e.onError(response.getException());
                    }
                    if (networkConfig.getIDevelopMode() != null) {
                        networkConfig.getIDevelopMode().onRecord(params, response.get());
                    }
                } catch (Exception exception) {
                    e.onError(exception);
                }
                e.onComplete();
            }
        });

    }

    /**
     * 加载
     *
     * @param params         请求参数
     * @param tAdaptResponse 回调
     * @return {@link Observable#create(ObservableOnSubscribe)}
     */
    public static <T> Observable<T> load(final BaseRequest params, final AdaptResponse<T> tAdaptResponse) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                NetworkConfig networkConfig = NetworkManager.getInstance().getInitializeConfig();
                try {
                    Request<String> request = new StringRequest(params.url, params.requestMethod);
                    if (RequestMethod.POST == params.requestMethod) {
                        request.setDefineRequestBodyForJson(params.params);
                    }
                    if (params.jsonString != null) {
                        request.setDefineRequestBodyForJson(params.jsonString);
                    }
                    if (params.headerParam.size() != 0) {
                        for (Object key : params.headerParam.keySet()) {
                            String value = String.valueOf(params.headerParam.get(key));
                            request.addHeader(String.valueOf(key), value);

                            Logger.d("request header : " + key + ":" + value);
                        }
                    }
                    request.setCacheKey(params.url);
                    request.setCacheMode(params.cacheMode);
                    request.setConnectTimeout(params.timeOut);
                    request.setRetryCount(params.retry);
                    request.add(params.mapParams);
                    if (networkConfig.getHeader() != null) {
                        networkConfig.getHeader().onHeader(request);
                    }
                    SSLContext sslContext = networkConfig.getSSLContext();
                    if (sslContext != null) {
                        request.setSSLSocketFactory(sslContext.getSocketFactory());
                    }


                    final Response<String> response = SyncRequestExecutor.INSTANCE.execute(request);
                    if (response.isSucceed()) {
                        String json = response.get();

                        Type type = ((ParameterizedType) tAdaptResponse.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        if (type == String.class) {
                            e.onNext((T) json);
                        } else {
                            e.onNext((T) networkConfig.getFromJson().onFromJson(json, type));
                        }


                    } else {
                        e.onError(response.getException());
                    }
                    if (networkConfig.getIDevelopMode() != null) {
                        networkConfig.getIDevelopMode().onRecord(params, response.get());
                    }


                } catch (Exception exception) {
                    e.onError(exception);

                }
                e.onComplete();
            }
        });

    }


    /**
     * 加载
     *
     * @param params            请求参数
     * @param tAbstractResponse 回调
     */
    public static <T> void load(final BaseRequest params, final AbstractResponse<T> tAbstractResponse) {

        final NetworkConfig networkConfig = NetworkManager.getInstance().getInitializeConfig();
        final INetDialog iDialog = networkConfig.getDialog();

        Type type = ((ParameterizedType) tAbstractResponse.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        final Request<T> request;
        if (type == String.class) {
            request = (Request<T>) NoHttp.createStringRequest(params.url, params.requestMethod);
        } else {
            request = new EntityRequest<>(params.url, params.requestMethod, type);
        }
        if (RequestMethod.POST == params.requestMethod) {
            request.setDefineRequestBodyForJson(params.params);
        }

        if (params.jsonString != null) {
            request.setDefineRequestBodyForJson(params.jsonString);
        }
        if (params.headerParam.size() != 0) {
            request.add(params.headerParam);
        }
        if (iDialog != null) {
            iDialog.init(params.context);
            iDialog.setCancelable(params.isCloseDialog);
            if (!TextUtils.isEmpty(params.loadingTitle)) {
                iDialog.setMessage(params.loadingTitle);
            }
        }

        SSLContext sslContext = networkConfig.getSSLContext();
        if (sslContext != null) {
            request.setSSLSocketFactory(sslContext.getSocketFactory());
        }
        request.setConnectTimeout(params.timeOut);
        request.setRetryCount(params.retry);
        request.setCacheMode(params.cacheMode);
        request.setTag(params.context);
        request.setCancelSign(params.context);
        request.add(params.mapParams);
        if (networkConfig.getHeader() != null) {
            networkConfig.getHeader().onHeader(request);
        }


        getRequestQueue().add(params.what, request, new OnResponseListener<T>() {
            @Override
            public void onStart(int what) {
                if (iDialog != null && params.isLoading)
                    iDialog.show();
                tAbstractResponse.onResponseState(new ResponseModel(ResponseEnum.BEGIN));
            }

            @Override
            public void onSucceed(int what, Response<T> response) {
                tAbstractResponse.onResponseState(new ResponseModel(ResponseEnum.SUCCESS));
                tAbstractResponse.onSuccess(response.get());

                Log.e("responsess",response.toString());
                if (networkConfig.getIDevelopMode() != null) {
                    networkConfig.getIDevelopMode().onRecord(params, response.get());
                }
                if (networkConfig.getNetworkListener() != null) {
                    networkConfig.getNetworkListener().onResetToken(params.context, response.get());
                }
            }

            @Override
            public void onFailed(int what, Response<T> response) {
                tAbstractResponse.onResponseState(new ResponseModel(ResponseEnum.FAILED, response.getException()));
                tAbstractResponse.onFailed();
                if (networkConfig.getIDevelopMode() != null) {
                    networkConfig.getIDevelopMode().onRecord(params, response.get());
                }

            }

            @Override
            public void onFinish(int what) {
                if (iDialog != null && params.isLoading)
                    iDialog.dismiss();
                tAbstractResponse.onResponseState(new ResponseModel(ResponseEnum.FINISH));
            }
        });
    }

    /**
     * 下载文件
     *
     * @param context          地址
     * @param url              请求标记
     * @param what             文件路径
     * @param fileFolder       文件名字
     * @param filename         是否续传
     * @param isRange          存在是否删除
     * @param isDeleteOld      回调
     * @param downloadListener 下载监听
     */

    public static void loadDownload(Context context, String url, int what, String fileFolder,
                                    String filename, boolean isRange, boolean isDeleteOld,
                                    final DownloadListener downloadListener) {
        final DownloadRequest request = NoHttp.createDownloadRequest(url, fileFolder, filename, isRange, isDeleteOld);
        request.setTag(context);
        request.setCancelSign(context);

        SSLContext sslContext = NetworkManager.getInstance().getInitializeConfig().getSSLContext();
        if (sslContext != null) {
            request.setSSLSocketFactory(sslContext.getSocketFactory());
        }
        getDownloadQueue().add(what, request, new DownloadListener() {

            @Override
            public void onDownloadError(int what, Exception exception) {
                if (downloadListener != null) {
                    downloadListener.onDownloadError(what, exception);
                }
            }

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {

                if (downloadListener != null) {
                    downloadListener.onStart(what, isResume, rangeSize, responseHeaders, allCount);
                }

            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {

                if (downloadListener != null) {
                    downloadListener.onProgress(what, progress, fileCount, speed);
                }
            }

            @Override
            public void onFinish(int what, String filePath) {
                if (downloadListener != null) {
                    downloadListener.onFinish(what, filePath);
                }
            }

            @Override
            public void onCancel(int what) {
                if (downloadListener != null) {
                    downloadListener.onCancel(what);
                }
            }
        });
    }


    public static void clearRequestAll() {
        getRequestQueue().cancelAll();
    }

    public static void clearRequest(Context context) {
        getRequestQueue().cancelBySign(context);
    }

    public static void clearDownload(Context context) {
        getDownloadQueue().cancelBySign(context);
    }

    public static void clearDownloadAll() {
        getDownloadQueue().cancelAll();
    }
}
