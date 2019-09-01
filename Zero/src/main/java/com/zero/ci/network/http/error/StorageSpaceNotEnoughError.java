package com.zero.ci.network.http.error;

/**
 * Specify the location of the file space is not enough.
 */
public class StorageSpaceNotEnoughError extends Exception {

    private static final long serialVersionUID = 11786348L;

    public StorageSpaceNotEnoughError() {
    }

    public StorageSpaceNotEnoughError(String detailMessage) {
        super(detailMessage);
    }

    public StorageSpaceNotEnoughError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public StorageSpaceNotEnoughError(Throwable throwable) {
        super(throwable);
    }

}
