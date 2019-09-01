package com.zero.ci.network.http.error;

public class NotFoundCacheError extends Exception {

    private static final long serialVersionUID = 115481468L;

    public NotFoundCacheError() {
    }

    public NotFoundCacheError(String detailMessage) {
        super(detailMessage);
    }

    public NotFoundCacheError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NotFoundCacheError(Throwable throwable) {
        super(throwable);
    }
}
