package com.zero.ci.network.http.rest.request;

import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.RequestMethod;

import org.json.JSONObject;

/**
 * JsonObject is returned by the server data, using the handle object.
 */
public class JsonObjectRequest extends Request<JSONObject> {

    public JsonObjectRequest(String url) {
        this(url, RequestMethod.GET);
    }

    public JsonObjectRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
        setAccept(Headers.HEAD_VALUE_CONTENT_TYPE_JSON);
    }

    @Override
    public JSONObject parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String jsonStr = StringRequest.parseResponseString(responseHeaders, responseBody);
        return new JSONObject(jsonStr);
    }
}