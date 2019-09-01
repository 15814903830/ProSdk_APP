package com.zero.ci.base;

import com.zero.ci.network.zrequest.conver.INetDialog;
import com.zero.ci.network.zrequest.NetworkManager;
import com.zero.ci.network.zrequest.request.BaseRequest;
import com.zero.ci.network.zrequest.request.RequestManager;
import com.zero.ci.widget.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


import android.widget.Toast;


import java.io.IOException;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 统一处理网络请求结果
 * -------------------------------
 * 1. 实例化presenter
 * 2. 判断是否现实loading
 */
public abstract class NetObserver<T> implements Observer<T> {

    private boolean isLoading;
    private BasePresenter mPresenter;
    private INetDialog mDialog;

    public NetObserver(BasePresenter presenter, boolean loading) {
        isLoading = loading;
        mPresenter = presenter;
    }

    public NetObserver(BasePresenter presenter) {
        mPresenter = presenter;
        isLoading = false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        showLoading();
    }

    @Override
    public void onNext(T t) {
        onNetNext(t);
    }

    @Override
    public void onError(Throwable e) {
        //统一处理请求异常的情况
        if (e instanceof IOException) {
            Toast.makeText(mPresenter.mContext, "网络链接异常...", Toast.LENGTH_SHORT).show();
        } else {
           // Toast.makeText(mPresenter.mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            Logger.e("NetObserver ==>> " + e.getMessage());
        }
        onNetError(e);
        cancelLoading();
    }

    @Override
    public void onComplete() {
        cancelLoading();
    }

    /**
     * 可在此处统一显示loading view
     */
    private void showLoading() {
        if (isLoading) {
            mDialog = NetworkManager.getInstance().getInitializeConfig().getDialog();
            mDialog.init(mPresenter.mContext);
            mDialog.show();
        }
    }

    private void cancelLoading() {
        if (mDialog != null)
            mDialog.dismiss();
    }


    public abstract void onNetNext(T result);

    public abstract void onNetError(Throwable e);

}
