package com.zero.ci.network.zrequest.conver;


import com.zero.ci.network.zrequest.request.BaseRequest;

/**
 * 开发者模式
 */
public interface IDevelopMode {

    void onRecord(BaseRequest params, Object response);
}
