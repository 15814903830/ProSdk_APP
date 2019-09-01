package com.zero.ci.refresh.api;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 内容刷新组件
 * -------------------------------
 */

public interface RefreshContent {
    View getView();

    @NonNull
    View getScrollableView();

    void onActionDown(MotionEvent e);

    void setUpComponent(RefreshKernel kernel, View fixedHeader, View fixedFooter);

    void setScrollBoundaryDecider(ScrollBoundaryDecider boundary);

    void setEnableLoadMoreWhenContentNotFull(boolean enable);

    void moveSpinner(int spinner, int headerTranslationViewId, int footerTranslationViewId);

    boolean isCanRefresh();

    boolean isCanLoadMore();

    ValueAnimator.AnimatorUpdateListener scrollContentWhenFinished(int spinner);
}
