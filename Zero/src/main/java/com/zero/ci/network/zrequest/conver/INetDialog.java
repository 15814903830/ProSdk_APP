package com.zero.ci.network.zrequest.conver;

import android.content.Context;


public interface INetDialog {
    /**
     * 初始化
     *
     * @param context
     */
    void init(Context context);

    /**
     * 显示对话框
     */
    void show();

    /**
     * 关闭对话框
     */

    void dismiss();

    /**
     * 对话框当前状态
     *
     * @return
     */
    boolean isShowing();

    /**
     * 设置该对话框是否是可撤销的
     *
     * @param flag
     */
    void setCancelable(boolean flag);

    /**
     * 对话框文本
     *
     * @param msg
     */
    void setMessage(String... msg);
}
