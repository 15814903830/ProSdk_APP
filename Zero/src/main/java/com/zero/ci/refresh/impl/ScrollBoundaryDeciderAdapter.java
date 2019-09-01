package com.zero.ci.refresh.impl;

import android.graphics.PointF;
import android.view.View;

import com.zero.ci.refresh.api.ScrollBoundaryDecider;
import com.zero.ci.refresh.utils.ScrollBoundaryUtil;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:滚动边界
 * -------------------------------
 */

public class ScrollBoundaryDeciderAdapter implements ScrollBoundaryDecider {
    public PointF mActionEvent;
    public ScrollBoundaryDecider mBoundaryDecider;
    public boolean isEnableLoadMoreWhenContentNotFull = true;

    @Override
    public boolean isCanRefresh(View content) {
        if (mBoundaryDecider != null)
            return mBoundaryDecider.isCanRefresh(content);
        return ScrollBoundaryUtil.canRefresh(content, mActionEvent);
    }

    @Override
    public boolean isCanLoadMore(View content) {
        if (mBoundaryDecider != null)
            return mBoundaryDecider.isCanLoadMore(content);
        return ScrollBoundaryUtil.canLoadMore(content, mActionEvent, isEnableLoadMoreWhenContentNotFull);
    }
}
