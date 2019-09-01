package com.zero.ci.network.http.rest.request;

import com.zero.ci.network.http.RequestMethod;
import com.zero.ci.network.http.rest.headers.Headers;

import org.json.JSONArray;

/**
 * JsonArray is returned by the server data, using the handle object.
 */
public class JsonArrayRequest extends Request<JSONArray> {

    public JsonArrayRequest(String url) {
        this(url, RequestMethod.GET);
    }

    public JsonArrayRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
        setAccept(Headers.HEAD_VALUE_CONTENT_TYPE_JSON);
    }

    @Override
    public JSONArray parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String jsonStr = StringRequest.parseResponseString(responseHeaders, responseBody);
        return new JSONArray(jsonStr);
    }

}
