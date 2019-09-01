package com.zero.ci.network.http.rest.request;

import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.RequestMethod;
import com.zero.ci.network.http.tools.HeaderUtils;
import com.zero.ci.network.http.tools.IOUtils;

public class StringRequest extends Request<String> {

    public StringRequest(String url) {
        this(url, RequestMethod.GET);
    }

    public StringRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    @Override
    public String parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        return parseResponseString(responseHeaders, responseBody);
    }

    /**
     * Parse http response to string.
     *
     * @param responseHeaders header from http response.
     * @param responseBody    byteArray from http response.
     * @return result fro response.
     */
    public static String parseResponseString(Headers responseHeaders, byte[] responseBody) {
        if (responseBody == null || responseBody.length == 0)
            return "";
        String charset = HeaderUtils.parseHeadValue(responseHeaders.getContentType(), "charset", "");
        return IOUtils.toString(responseBody, charset);
    }
}