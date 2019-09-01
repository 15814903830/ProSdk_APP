package com.zero.ci.refresh.impl;

import android.view.View;

import com.zero.ci.refresh.api.RefreshHeader;
import com.zero.ci.refresh.internal.InternalAbstract;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 头部刷新
 * -------------------------------
 */

public class RefreshHeaderWrapper  extends InternalAbstract implements RefreshHeader{
    public RefreshHeaderWrapper(View wrapperView) {
        super(wrapperView);
    }

}
