package com.zero.ci.network.http.rest.headers;

import com.zero.ci.network.http.rest.request.BasicRequest;

public interface RedirectHandler {

    /**
     * When the server's response code is 302 or 303 corresponding need to redirect is invoked.
     *
     * @param oldRequest      the old handle.
     * @param responseHeaders the service side head accordingly.
     */
    BasicRequest<?> onRedirect(BasicRequest<?> oldRequest, Headers responseHeaders);

    /**
     * Whether to allow the redirection, if not redirect will not be {@code #onRedirect(Headers)} callback method, at
     * the same time will ban NoHttp automatic redirection.If allowed to redirect, first
     * call {@code #onRedirect(Headers)} method, if {@code #onRedirect(Headers)} method returns null, execute NoHttp
     * default redirect.
     *
     * @param responseHeaders the service side head accordingly.
     * @return returns true said allow redirection, returns false said do not allow the redirection.
     */
    boolean isDisallowedRedirect(Headers responseHeaders);
}
