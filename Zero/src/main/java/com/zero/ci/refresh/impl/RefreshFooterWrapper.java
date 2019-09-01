package com.zero.ci.refresh.impl;

import android.view.View;

import com.zero.ci.refresh.api.RefreshFooter;
import com.zero.ci.refresh.internal.InternalAbstract;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 底部刷新
 * -------------------------------
 */

public class RefreshFooterWrapper extends InternalAbstract implements RefreshFooter {
    public RefreshFooterWrapper(View wrapperView) {
        super(wrapperView);
    }

    @Override
    public boolean setNoMoreData(boolean isNoMoreData) {
        return mWrapperView instanceof RefreshFooter && ((RefreshFooter) mWrapperView).setNoMoreData(isNoMoreData);
    }
}
