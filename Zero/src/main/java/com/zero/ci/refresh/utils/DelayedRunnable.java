package com.zero.ci.refresh.utils;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 延时执行
 * -------------------------------
 */

public class DelayedRunnable implements Runnable {
    public long delayMillis;
    private Runnable mRunnable = null;

    public DelayedRunnable(Runnable runnable, long delayMillis) {
        this.mRunnable = runnable;
        this.delayMillis = delayMillis;
    }
    @Override
    public void run() {
        try {
            if (mRunnable != null) {
                mRunnable.run();
                mRunnable = null;
            }
        } catch (Throwable e) {
            if (!(e instanceof NoClassDefFoundError)) {
                e.printStackTrace();
            }
        }
    }
}
