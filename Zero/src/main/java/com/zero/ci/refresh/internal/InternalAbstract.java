package com.zero.ci.refresh.internal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zero.ci.refresh.ZRefreshLayout;
import com.zero.ci.refresh.api.RefreshInternal;
import com.zero.ci.refresh.api.RefreshKernel;
import com.zero.ci.refresh.api.RefreshLayout;
import com.zero.ci.refresh.api.constant.RefreshState;
import com.zero.ci.refresh.api.constant.SpinnerStyle;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:internal 初步实现
 * -------------------------------
 * 1.  初步实现接口
 */

public abstract class InternalAbstract extends RelativeLayout implements RefreshInternal {
    protected View mWrapperView;
    protected SpinnerStyle mSpinnerStyle;

    protected InternalAbstract(View wrapperView) {
        super(wrapperView.getContext(), null, 0);
        this.mWrapperView = wrapperView;
    }

    public InternalAbstract(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    @Override
    public View getView() {
        return mWrapperView == null ? this : mWrapperView;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        if (mSpinnerStyle != null) {
            return mSpinnerStyle;
        }

        if (mWrapperView instanceof RefreshInternal) {
            return ((RefreshInternal) mWrapperView).getSpinnerStyle();
        }
        if (mWrapperView != null) {
            ViewGroup.LayoutParams params = mWrapperView.getLayoutParams();
            if (params instanceof ZRefreshLayout.LayoutParams) {
                mSpinnerStyle = ((ZRefreshLayout.LayoutParams) params).spinnerStyle;
                if (mSpinnerStyle != null) {
                    return mSpinnerStyle;
                }
            }

            if (params != null) {
                if (params.height == 0 || params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    return mSpinnerStyle = SpinnerStyle.Scale;
                }
            }
        }
        return mSpinnerStyle = SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).setPrimaryColors(colors);
        }
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onInitialized(kernel, height, maxDragHeight);
        } else if (mWrapperView != null) {
            ViewGroup.LayoutParams params = mWrapperView.getLayoutParams();
            if (params instanceof ZRefreshLayout.LayoutParams) {
                kernel.requestDrawBackgroundFor(this, ((ZRefreshLayout.LayoutParams) params).backgroundColor);
            }
        }
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onMoving(isDragging, percent, offset, height, maxDragHeight);
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onReleased(refreshLayout, height, maxDragHeight);
        }
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onStartAnimator(refreshLayout, height, maxDragHeight);
        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean isSuccess) {
        if (mWrapperView instanceof RefreshInternal) {
            return ((RefreshInternal) mWrapperView).onFinish(refreshLayout, isSuccess);
        }
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onHorizontalDrag(percentX, offsetX, offsetMax);
        }
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return mWrapperView instanceof RefreshInternal && ((RefreshInternal) mWrapperView).isSupportHorizontalDrag();
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onStateChanged(refreshLayout, oldState, newState);
        }
    }
}
