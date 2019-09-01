package com.zero.ci.refresh.api.listener;


import android.support.annotation.NonNull;

import com.zero.ci.refresh.api.RefreshFooter;
import com.zero.ci.refresh.api.RefreshHeader;
import com.zero.ci.refresh.api.RefreshLayout;
import com.zero.ci.refresh.api.constant.RefreshState;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 监听器
 * -------------------------------
 */

public class SimpleMultiPurposeListener implements OnMultiPurposeListener {

    @Override
    public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {

    }

    @Override
    public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {

    }

    @Override
    public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {

    }

    @Override
    public void onHeaderFinish(RefreshHeader header, boolean success) {

    }

    @Override
    public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

    }

    @Override
    public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

    }

    @Override
    public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {

    }

    @Override
    public void onFooterFinish(RefreshFooter footer, boolean success) {

    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {

    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

    }
}
