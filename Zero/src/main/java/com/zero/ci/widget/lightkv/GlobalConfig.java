package com.zero.ci.widget.lightkv;

import android.content.Context;

import com.zero.ci.BuildConfig;


/**
 * @author: Zero Yuan
 * @Email: zero.yuan.xin@gmail.com
 * @DATE : 19/4/9/上午11:05
 * @Description:
 */
public class GlobalConfig {
    public static final boolean DEBUG = BuildConfig.DEBUG;

    private static Context sAppContext;

    public static void setAppContext(Context context) {
        sAppContext = context;
    }

    public static Context getAppContext() {
        return sAppContext;
    }
}
