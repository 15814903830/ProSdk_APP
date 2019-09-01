package com.zero.ci.network.zrequest;


import com.zero.ci.network.http.InitializationConfig;
import com.zero.ci.network.http.NoHttp;
import com.zero.ci.network.zrequest.conver.IDevelopMode;
import com.zero.ci.network.zrequest.conver.IFromJson;
import com.zero.ci.network.zrequest.conver.IHeader;
import com.zero.ci.network.zrequest.conver.INetDialog;
import com.zero.ci.network.zrequest.conver.INetworkListener;
import com.zero.ci.network.zrequest.conver.IToastFailed;

import javax.net.ssl.SSLContext;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:网络配置
 * -------------------------------
 * 1.配置解析数据方式
 * 2.配置开发者模式
 * 3.配置网络监听
 */

public class NetworkConfig {

    private IFromJson mIFromJson;
    private INetDialog mIDialog;
    private IDevelopMode mIDevelopMode;
    private IToastFailed mIToastFailed;
    private SSLContext mSSLContext;
    private INetworkListener mINetworkListener;
    private IHeader mIHeader;
    private int mThreadPoolSize;

    public NetworkConfig(final Builder builder) {
        NoHttp.initialize(builder.mNoHttpConfig);

        if (builder.mFromJson == null) {
            throw new ExceptionInInitializerError("Please invoke  IFromJson~");
        }
        mIFromJson = builder.mFromJson;
        mIDialog = builder.mDialog;
        mIDevelopMode = builder.mDevelopMode;
        mSSLContext = builder.mSSLContext;
        mIToastFailed = builder.mToastFailed;
        mINetworkListener = builder.mNetworkListener;
        mThreadPoolSize = builder.mThreadPoolSize;
        mIHeader = builder.mHeader;
    }

    public static final class Builder {


        /**
         * 对话框接口
         */
        private INetDialog mDialog;

        /**
         * 初始化http
         */
        private InitializationConfig mNoHttpConfig;

        /**
         * 数据解析相关
         */
        private IFromJson mFromJson;


        /**
         * 开发者模式相关
         */
        private IDevelopMode mDevelopMode;

        /**
         * https 证书
         */
        private SSLContext mSSLContext;

        /**
         * 网络失败统一处理
         */
        private IToastFailed mToastFailed;

        /**
         * 统一需要处理相关事件
         */
        private INetworkListener mNetworkListener;

        /**
         * 头部
         */
        private IHeader mHeader;

        /**
         * 队列总数
         */
        private int mThreadPoolSize = 5;


        public Builder dialog(INetDialog dialog) {
            this.mDialog = dialog;
            return this;
        }

        public Builder noHttpConfig(InitializationConfig config) {
            mNoHttpConfig = config;
            return this;
        }


        public Builder fromJson(IFromJson fromJson) {
            mFromJson = fromJson;
            return this;
        }

        public Builder developMode(IDevelopMode developMode) {
            mDevelopMode = developMode;
            return this;
        }

        public Builder SSLContext(SSLContext sslContext) {
            mSSLContext = sslContext;
            return this;
        }

        public Builder toastFailed(IToastFailed toastFailed) {
            mToastFailed = toastFailed;
            return this;
        }

        public Builder networkListener(INetworkListener listener) {
            this.mNetworkListener = listener;
            return this;
        }

        public Builder threadPoolSize(int threadPoolSize) {
            this.mThreadPoolSize = threadPoolSize;
            return this;
        }

        public Builder header(IHeader header) {
            this.mHeader = header;
            return this;
        }


        public NetworkConfig build() {
            return new NetworkConfig(this);
        }
    }

    public IFromJson getFromJson() {
        return mIFromJson;
    }


    public INetDialog getDialog() {
        return mIDialog;
    }


    public IDevelopMode getIDevelopMode() {
        return mIDevelopMode;
    }

    public SSLContext getSSLContext() {
        return mSSLContext;
    }

    public IToastFailed getToastFailed() {
        return mIToastFailed;
    }

    public INetworkListener getNetworkListener() {
        return mINetworkListener;
    }

    public IHeader getHeader() {
        return mIHeader;
    }

    public int getThreadPoolSize() {
        return mThreadPoolSize;
    }
}
