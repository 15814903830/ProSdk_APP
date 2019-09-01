package com.zero.ci.refresh.api;

import android.content.Context;
import android.support.annotation.NonNull;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:  默认Footer创建器
 * -------------------------------
 */

public interface DefaultRefreshFooterCreator {
    @NonNull
    RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout);
}
