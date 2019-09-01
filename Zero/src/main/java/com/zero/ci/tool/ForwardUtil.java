package com.zero.ci.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA
 * AUTHOR: zero
 * Email : zero_ci@163.com
 * DATE  : 2016/3/4/17:28
 * Description :Activity 跳转工具
 */
public class ForwardUtil {
    static ForwardUtil mInstance;
    Context mContext;

    public ForwardUtil(Context context) {
        this.mContext = context;
    }

    public static ForwardUtil getInstance(Context context) {
        if (mInstance == null)
            mInstance = new ForwardUtil(context);
        return mInstance;
    }

    /**
     * 跳转至某一个activity
     *
     * @param cls
     */
    public final void forward(Class<?> cls) {
        forward(cls, null, -1);
    }

    /**
     * 带参跳转至某一个activity
     *
     * @param cls
     * @param bundle
     */
    public final void forward(Class<?> cls, Bundle bundle) {
        forward(cls, bundle, -1);
    }

    /**
     * 带参和Intent.FLAG跳转至某一个activity
     *
     * @param cls
     * @param bundle
     * @param flags
     */
    public final void forward(Class<?> cls, Bundle bundle, int flags) {
        if (cls == null) {
            return;
        }
        Intent intent = new Intent(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (flags > 0) {
            intent.addFlags(flags);
        }
        mContext.startActivity(intent);
    }



}
