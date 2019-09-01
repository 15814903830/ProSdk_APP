package com.zero.ci.network.zrequest.conver;


import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.rest.request.Request;
import com.zero.ci.widget.logger.Logger;

import java.util.Map;

public class RequestHeaderImpl implements IHeader {
    @Override
    public <T> void onHeader(Request<T> request) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n---^^^^^^^^^^^^^^---Request start---^^^^^^^^^^^^^^---\n")
                .append("\nRequest address : ").append(request.url()).append("\n")
                .append("\nRequest Method  : ").append(request.getRequestMethod()).append("\n")
                .append("\nConnectTimeout  : ").append(request.getConnectTimeout()).append("\n")
                .append("\nContentLength   : ").append(request.getContentLength()).append("\n");

        Headers headers = request.getHeaders();

        Map<String, String> requestHeaders = headers.toRequestHeaders();
        for (Map.Entry<String, String> headerEntry : requestHeaders.entrySet()) {
            String headKey = headerEntry.getKey();
            String headValue = headerEntry.getValue();
            builder.append("\n").append(headKey).append(" : ").append(headValue).append("\n");
        }


        Logger.d(request.getDefineRequestBody() + "\n");

        builder.append("\n---^^^^^^^^^^^^^^---Request finish---^^^^^^^^^^^^^^---").append("\n");
        Logger.d(builder.toString());
    }
}
