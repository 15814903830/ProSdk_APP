package com.zero.ci.refresh.api.listener;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 协调监听
 * -------------------------------
 */

public interface CoordinatorLayoutListener {
    void onCoordinatorUpdate(boolean isEnableRefresh, boolean isEnableLoadMore);
}
