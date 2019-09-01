package com.zero.ci.network.http;

import android.os.Handler;
import android.os.Looper;

/**
 * Poster.
 */
public final class HandlerDelivery {

    private static HandlerDelivery instance;

    public static HandlerDelivery getInstance() {
        if (instance == null)
            synchronized (HandlerDelivery.class) {
                if (instance == null)
                    instance = new HandlerDelivery(new Handler(Looper.getMainLooper()));
            }
        return instance;
    }

    private Handler mHandler;

    private HandlerDelivery(Handler handler) {
        this.mHandler = handler;
    }

    public boolean post(Runnable r) {
        return mHandler.post(r);
    }

    public boolean postDelayed(Runnable r, long delayMillis) {
        return mHandler.postDelayed(r, delayMillis);
    }

    public boolean postAtFrontOfQueue(Runnable r) {
        return mHandler.postAtFrontOfQueue(r);
    }

    public boolean postAtTime(Runnable r, long uptimeMillis) {
        return mHandler.postAtTime(r, uptimeMillis);
    }

    public boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return mHandler.postAtTime(r, token, uptimeMillis);
    }

    public void removeCallbacks(Runnable r) {
        mHandler.removeCallbacks(r);
    }

    public void removeCallbacks(Runnable r, Object token) {
        mHandler.removeCallbacks(r, token);
    }
}
