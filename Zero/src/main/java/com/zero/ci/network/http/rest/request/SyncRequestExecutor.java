package com.zero.ci.network.http.rest.request;

import com.zero.ci.network.http.InitializationConfig;
import com.zero.ci.network.http.NoHttp;
import com.zero.ci.network.http.rest.response.Response;

/**
 * Synchronization handle executor.
 */
public enum SyncRequestExecutor {

    INSTANCE;

    private RequestHandler mRequestHandler;

    SyncRequestExecutor() {
        InitializationConfig initializationConfig = NoHttp.getInitializeConfig();
        mRequestHandler = new RequestHandler(
                initializationConfig.getCacheStore(),
                initializationConfig.getNetworkExecutor(),
                initializationConfig.getInterceptor()
        );
    }

    /**
     * Perform a handle.
     */
    public <T> Response<T> execute(Request<T> request) {
        return mRequestHandler.handle(request);
    }
}
