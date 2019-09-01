package com.zero.ci.network.http.error;

/**
 * The target host not found.
 */
public class UnKnownHostError extends Exception {

    private static final long serialVersionUID = 1149646L;

    public UnKnownHostError() {
    }

    public UnKnownHostError(String detailMessage) {
        super(detailMessage);
    }

    public UnKnownHostError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnKnownHostError(Throwable throwable) {
        super(throwable);
    }

}
