package com.zero.ci.network.http.error;

/**
 * The URL specified is incorrect.
 */
public class URLError extends Exception {

    private static final long serialVersionUID = 114946L;

    public URLError() {
    }

    public URLError(String detailMessage) {
        super(detailMessage);
    }

    public URLError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public URLError(Throwable throwable) {
        super(throwable);
    }

}
