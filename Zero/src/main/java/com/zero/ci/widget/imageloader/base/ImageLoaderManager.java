package com.zero.ci.widget.imageloader.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.HashMap;
import java.util.Map;



public class ImageLoaderManager {

    private static class ImageLoaderHolder {
        private static final ImageLoaderManager INSTANCE = new ImageLoaderManager();
    }

    private IImageLoaderStrategy loaderstrategy;
    private HashMap<LoaderEnum, IImageLoaderStrategy> imageLoaderMap = new HashMap<>();
    private LoaderEnum curLoader = null;

    private ImageLoaderManager() {
    }

    public static ImageLoaderManager getInstance() {
        return ImageLoaderHolder.INSTANCE;
    }


    /*
     *   可创建默认的Options设置，假如不需要使用ImageView ，
     *    请自行new一个ImageView传入即可
     *  内部只需要获取Context
     */
    private static ImageLoaderOptions getDefaultOptions(@NonNull View container, @NonNull String url) {
        return new ImageLoaderOptions.Builder(container, url).isCrossFade(true).build();
    }

    public void showImage(View container, String url) {
        showImage(getDefaultOptions(container, url));
    }

    public void showImage(@NonNull ImageLoaderOptions options) {
        if (getLoaderStrategy(curLoader) != null) {
            getLoaderStrategy(curLoader).showImage(options);
        }
    }

    public void showImage(@NonNull ImageLoaderOptions options, LoaderEnum loaderEnum) {
        if (getLoaderStrategy(loaderEnum) != null) {
            getLoaderStrategy(loaderEnum).showImage(options);
        }
    }

    public void hideImage(@NonNull View view, int visiable) {
        if (getLoaderStrategy(curLoader) != null) {
            getLoaderStrategy(curLoader).hideImage(view, visiable);
        }
    }


    public void cleanMemory(Context context) {
        getLoaderStrategy(curLoader).cleanMemory(context);
    }

    public void pause(Context context) {
        if (getLoaderStrategy(curLoader) != null) {
            getLoaderStrategy(curLoader).pause(context);
        }
    }

    public void resume(Context context) {
        if (getLoaderStrategy(curLoader) != null) {
            getLoaderStrategy(curLoader).resume(context);
        }
    }

    public void setCurImageLoader(LoaderEnum loader) {
        curLoader = loader;
    }

    // 在application的onCreate中初始化
    public void init(Context context, ImageLoaderConfig config) {
        imageLoaderMap = config.getImageloaderMap();
        for (Map.Entry<LoaderEnum, IImageLoaderStrategy> entry : imageLoaderMap.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().init(context, config);
            }

            if (curLoader == null) {
                curLoader = entry.getKey();
            }
        }
    }

    private IImageLoaderStrategy getLoaderStrategy(LoaderEnum loaderEnum) {
        return imageLoaderMap.get(loaderEnum);
    }

}
