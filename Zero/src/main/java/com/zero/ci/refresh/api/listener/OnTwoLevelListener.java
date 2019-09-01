package com.zero.ci.refresh.api.listener;


import android.support.annotation.NonNull;

import com.zero.ci.refresh.api.RefreshLayout;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:二级刷新监听器
 * -------------------------------
 */

public interface OnTwoLevelListener {
    /**
     * 触发二级刷新
     *
     * @param refreshLayout 刷新布局
     * @return true 将会展开二楼状态 false 关闭刷新
     */
    boolean onTwoLevel(@NonNull RefreshLayout refreshLayout);
}
