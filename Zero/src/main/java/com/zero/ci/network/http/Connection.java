package com.zero.ci.network.http;

import com.zero.ci.network.http.rest.Network;
import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.tools.IOUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class Connection implements Closeable {

    /**
     * NetworkExecutor
     */
    private Network network;
    /**
     * Server response header.
     */
    private Headers mResponseHeaders;
    /**
     * Server data stream, may be the error or input.
     */
    private InputStream mServerStream;
    /**
     * Exception of network.
     */
    private Exception mException;

    /**
     * Create a response.
     *
     * @param network         {@link Network}.
     * @param responseHeaders response headers.
     * @param serverStream    According to the response code, the incoming data stream server.
     * @param exception       network exceptions that occur in the process.
     */
    public Connection(Network network, Headers responseHeaders, InputStream serverStream, Exception exception) {
        this.network = network;
        this.mResponseHeaders = responseHeaders;
        this.mServerStream = serverStream;
        this.mException = exception;
    }

    /**
     * Get response headers.
     *
     * @return the responseHeaders.
     */
    public Headers responseHeaders() {
        return mResponseHeaders;
    }

    /**
     * Get stream from server.
     *
     * @return the inputStream.
     */
    public InputStream serverStream() {
        return mServerStream;
    }

    /**
     * Get exception for execution.
     *
     * @return the exception.
     */
    public Exception exception() {
        return mException;
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(mServerStream);
        IOUtils.closeQuietly(network);
    }

}
