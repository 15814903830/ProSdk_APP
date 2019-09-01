package com.zero.ci.refresh.api;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:底部刷新
 * -------------------------------
 */

public interface RefreshFooter extends RefreshInternal {
    /**
     * 设置数据是否加载完毕（注：设为true后将不再触发加载功能）
     *
     * @param isNoMoreData 是否有更多数据
     * @return true 加载完成状态且没有更多数据  false 运行加载更多数据
     */
    boolean setNoMoreData(boolean isNoMoreData);
}
