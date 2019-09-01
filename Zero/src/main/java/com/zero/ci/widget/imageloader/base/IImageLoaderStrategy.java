package com.zero.ci.widget.imageloader.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;



public interface IImageLoaderStrategy {
    void showImage(@NonNull ImageLoaderOptions options);
    void hideImage(@NonNull View view, int visiable);
    void cleanMemory(Context context);
    void pause(Context context);
    void resume(Context context);
    // 在application的onCreate中初始化
    void init(Context context, ImageLoaderConfig config);
}
