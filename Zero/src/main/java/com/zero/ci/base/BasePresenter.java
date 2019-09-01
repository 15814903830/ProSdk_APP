package com.zero.ci.base;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 基础逻辑类
 * -------------------------------
 * 1.实现view添加
 * 2.CompositeDisposable 生命周期控制
 * 3.绑定事件
 */

public class BasePresenter<V> {
    protected V mView;
    private CompositeDisposable mCompositeDisposable;
    public Context mContext;

    public void onAttach(Context context, V v) {
        this.mView = v;
        this.mContext = context;
        mCompositeDisposable = new CompositeDisposable();
    }


    public void onDetached() {
        mView = null;
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable.dispose();
        }
    }

    public <T> void append(Observable<T> observable, Observer<T> observer) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
