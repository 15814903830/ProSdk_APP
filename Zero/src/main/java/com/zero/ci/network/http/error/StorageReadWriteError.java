package com.zero.ci.network.http.error;

public class StorageReadWriteError extends Exception {

    private static final long serialVersionUID = 178946465L;

    public StorageReadWriteError() {
    }

    public StorageReadWriteError(String detailMessage) {
        super(detailMessage);
    }

    public StorageReadWriteError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public StorageReadWriteError(Throwable throwable) {
        super(throwable);
    }
}
