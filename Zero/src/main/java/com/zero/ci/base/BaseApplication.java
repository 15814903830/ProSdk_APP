package com.zero.ci.base;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.zero.ci.BuildConfig;
import com.zero.ci.R;
import com.zero.ci.network.http.InitializationConfig;
import com.zero.ci.network.http.cache.DBCacheStore;
import com.zero.ci.network.http.cookie.DBCookieStore;
import com.zero.ci.network.okconnect.OkHttpNetworkExecutor;
import com.zero.ci.network.zrequest.NetworkConfig;
import com.zero.ci.network.zrequest.NetworkManager;
import com.zero.ci.network.zrequest.conver.DevelopModeImpl;
import com.zero.ci.network.zrequest.conver.FromJsonImpl;
import com.zero.ci.network.zrequest.conver.NetworkImplListener;
import com.zero.ci.network.zrequest.conver.RequestHeaderImpl;
import com.zero.ci.network.zrequest.conver.ToastFailedImpl;
import com.zero.ci.network.zrequest.conver.loading.ZLoadingDialog;
import com.zero.ci.refresh.ZRefreshLayout;
import com.zero.ci.refresh.api.DefaultRefreshHeaderCreator;
import com.zero.ci.refresh.api.RefreshHeader;
import com.zero.ci.refresh.api.RefreshLayout;
import com.zero.ci.refresh.header.MaterialHeader;
import com.zero.ci.widget.imageloader.base.ImageLoaderConfig;
import com.zero.ci.widget.imageloader.base.ImageLoaderManager;
import com.zero.ci.widget.imageloader.base.LoaderEnum;
import com.zero.ci.widget.imageloader.glide.GlideImageLocader;
import com.zero.ci.widget.logger.Logger;
import com.zero.ci.widget.logger.adapter.AndroidLogAdapter;
import com.zero.ci.widget.logger.strategy.FormatStrategy;
import com.zero.ci.widget.logger.strategy.PrettyFormatStrategy;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:
 * -------------------------------
 * 1.
 */

public class  BaseApplication extends MultiDexApplication {
    public static Context mContext;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//启用矢量图兼容
        ZRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.z_title_color, android.R.color.white);//全局设置主题颜色
                //myreturn new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
                return new MaterialHeader(context);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initLogger();
        initNetWok();
        initImageLoader(getApplicationContext());


    }

    public static void initLogger() {

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("Android-Zero")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.LOG_DEBUG;
            }
        });


    }

    /**
     * 初始化图片加载类配置信息
     **/
    public static void initImageLoader(Context context) {
        ImageLoaderConfig imageLoaderConfig = new ImageLoaderConfig
                .Builder(LoaderEnum.GLIDE, new GlideImageLocader())
                .maxMemory(40 * 1024 * 1024L)
                .build();
        ImageLoaderManager.getInstance().init(context, imageLoaderConfig);

    }

    private void initNetWok() {


        InitializationConfig config = InitializationConfig.newBuilder(mContext)
                .connectionTimeout(50 * 1000)
                .readTimeout(30 * 1000)
                .cacheStore(
                        new DBCacheStore(mContext).setEnable(true)
                )
                .cookieStore(
                        new DBCookieStore(mContext).setEnable(true)
                )
                .networkExecutor(new OkHttpNetworkExecutor())
                .retry(1)
                .build();

        ZLoadingDialog zLoadingDialog = new ZLoadingDialog(getZApplicationContext());
        NetworkManager.getInstance().
                InitializationConfig(new NetworkConfig.Builder()
                        .fromJson(new FromJsonImpl())
                        .developMode(new DevelopModeImpl())
                        .header(new RequestHeaderImpl())
                        .dialog(zLoadingDialog
                                        .setLoadingColor(Color.parseColor("#2DAE98"))
                                        .setHintText("请稍等...")
                                        .setHintTextSize(16) // 设置字体大小
                                        .setHintTextColor(Color.WHITE)  // 设置字体颜色
                        )
                        .noHttpConfig(config)
                        .toastFailed(new ToastFailedImpl())
                        .networkListener(new NetworkImplListener())
                        .build());



    }


    public synchronized static void setZApplicationContext(Context context) {
        mContext = context;
    }

    public static Context getZApplicationContext() {
        return mContext;
    }

}
