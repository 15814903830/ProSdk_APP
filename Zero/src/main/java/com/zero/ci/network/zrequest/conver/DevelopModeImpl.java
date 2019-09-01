package com.zero.ci.network.zrequest.conver;


import com.zero.ci.network.zrequest.request.BaseRequest;

import com.zero.ci.widget.logger.Logger;
import com.zero.ci.tool.JsonFormatUtils;

public class DevelopModeImpl implements IDevelopMode {
    @Override
    public void onRecord(BaseRequest params, Object response) {
        Logger.d(String.valueOf(response));
        Logger.d(JsonFormatUtils.formatJson(String.valueOf(response)));
    }
}
