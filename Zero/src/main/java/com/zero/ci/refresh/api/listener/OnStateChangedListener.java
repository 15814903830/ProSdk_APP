package com.zero.ci.refresh.api.listener;

import android.support.annotation.NonNull;

import com.zero.ci.refresh.api.RefreshLayout;
import com.zero.ci.refresh.api.constant.RefreshState;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 刷新状态改变监听器
 * -------------------------------
 */

public interface OnStateChangedListener {

    /**
     * 状态改变{@link RefreshState}
     *
     * @param oldState 改变之前的状态
     * @param newState 改变之后的状态
     */
    void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState);

}
