package com.zero.ci.network.http.rest;

import com.zero.ci.network.http.rest.request.BasicRequest;

public interface NetworkExecutor {

    /**
     * Perform network connection.
     *
     * @param request {@link BasicRequest}.
     * @return {@link Network}.
     * @throws Exception maybe.
     */
    Network execute(BasicRequest<?> request) throws Exception;

}
