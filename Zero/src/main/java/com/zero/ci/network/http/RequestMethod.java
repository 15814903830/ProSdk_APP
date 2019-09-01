package com.zero.ci.network.http;

import android.text.TextUtils;

import java.util.Locale;

/**
 * HTTP handle method.
 */
public enum RequestMethod {

    GET("GET"),

    POST("POST"),

    PUT("PUT"),

    DELETE("DELETE"),

    HEAD("HEAD"),

    PATCH("PATCH"),

    OPTIONS("OPTIONS"),

    TRACE("TRACE");

    private final String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean allowRequestBody() {
        switch (this) {
            case POST:
            case PUT:
            case PATCH:
            case DELETE:
                return true;
            default:
                return false;
        }
    }

    public static RequestMethod reverse(String method) {
        if (TextUtils.isEmpty(method)) {
            method = "GET";
        }

        method = method.toUpperCase(Locale.ENGLISH);
        switch (method) {
            case "GET": {
                return GET;
            }
            case "POST": {
                return POST;
            }
            case "PUT": {
                return PUT;
            }
            case "DELETE": {
                return DELETE;
            }
            case "HEAD": {
                return HEAD;
            }
            case "PATCH": {
                return PATCH;
            }
            case "OPTIONS": {
                return OPTIONS;
            }
            case "TRACE": {
                return TRACE;
            }
            default: {
                return GET;
            }
        }
    }

}
