package com.zero.ci.network.zrequest.conver;


import com.zero.ci.tool.ToastUtil;
import com.zero.ci.widget.logger.Logger;


public class ToastFailedImpl implements IToastFailed {
    @Override
    public void onFailed() {
        ToastUtil.show("网络不给力啊······");
        Logger.d("网络不给力啊······");
    }
}
