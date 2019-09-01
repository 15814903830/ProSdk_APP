package com.zero.ci.network.http.rest.request;

import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.RequestMethod;

public class ByteArrayRequest extends Request<byte[]> {

    public ByteArrayRequest(String url) {
        this(url, RequestMethod.GET);
    }

    public ByteArrayRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    @Override
    public byte[] parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        return responseBody == null ? new byte[0] : responseBody;
    }
}