package com.zero.ci.network.http.rest;

import com.zero.ci.network.http.rest.headers.Headers;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface Network extends Closeable {

    /**
     * Gets output stream for socket.
     *
     * @return {@link OutputStream}.
     * @throws IOException maybe.
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * Gets response code for server.
     *
     * @return int value.
     * @throws IOException maybe.
     */
    int getResponseCode() throws IOException;

    /**
     * Gets response headers for server.
     *
     * @return {@code Map<String, List<String>>}.
     */
    Map<String, List<String>> getResponseHeaders();

    /**
     * Gets input stream for socket.
     *
     * @param responseCode response code for server.
     * @param headers      response headers for server.
     * @return {@link InputStream}.
     * @throws IOException maybe.
     */
    InputStream getServerStream(int responseCode, Headers headers) throws IOException;

}
