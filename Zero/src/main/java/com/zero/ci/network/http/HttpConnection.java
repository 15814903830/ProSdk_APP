package com.zero.ci.network.http;

import android.text.TextUtils;
import android.webkit.URLUtil;

import com.zero.ci.network.http.error.NetworkError;
import com.zero.ci.network.http.error.TimeoutError;
import com.zero.ci.network.http.error.URLError;
import com.zero.ci.network.http.error.UnKnownHostError;
import com.zero.ci.network.http.rest.Network;
import com.zero.ci.network.http.rest.NetworkExecutor;
import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.rest.headers.RedirectHandler;
import com.zero.ci.network.http.rest.request.BasicRequest;
import com.zero.ci.network.http.rest.request.Request;
import com.zero.ci.network.http.tools.IOUtils;
import com.zero.ci.network.http.tools.NetUtils;
import com.zero.ci.widget.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Responsible for Http network connection and data read and write.
 * 负责HTTP网络连接和数据读写
 */
public class HttpConnection {

    private NetworkExecutor mExecutor;
   // StringBuilder builder = new StringBuilder();

    public HttpConnection(NetworkExecutor executor) {
        this.mExecutor = executor;
    }

    /**
     * Send the handle, send only head, parameters, such as file information.
     *
     * @param request {@link BasicRequest}.
     * @return {@link Connection}.
     */
    public Connection getConnection(BasicRequest<?> request) {

     //   builder.append("<.><.><.> HttpConnection <.><.><.>").append("\n")
      //          .append("---^^^^^^^^^^^^^^---Request start---^^^^^^^^^^^^^^---\n");
        Headers responseHeaders = new Headers();
        InputStream inputStream = null;
        Exception exception = null;

        Network network = null;
        String url = request.url();
        try {
            if (!NetUtils.isNetworkAvailable())
                throw new NetworkError("The network is not available, please check the network. The requested url is:" + url);

            // MalformedURLException, IOException, ProtocolException, UnknownHostException, SocketTimeoutException
            network = createConnectionAndWriteData(request);
            // builder.append("---*************---Response start---*************---\n");
            int responseCode = network.getResponseCode();
            responseHeaders = parseResponseHeaders(new URI(request.url()), responseCode, network.getResponseHeaders());

            // handle body
            if (responseCode == 301 || responseCode == 302 || responseCode == 303 || responseCode == 307) {
                Connection redirectConnection = handleRedirect(request, responseHeaders);
                responseHeaders = redirectConnection.responseHeaders();
                inputStream = redirectConnection.serverStream();
                exception = redirectConnection.exception();
            } else if (hasResponseBody(request.getRequestMethod(), responseCode)) {
                inputStream = network.getServerStream(responseCode, responseHeaders);
            }
            // builder.append("---*************---Response end---*************---\n");
        } catch (MalformedURLException e) {
            exception = new URLError("The url is malformed: " + url + ".");
        } catch (UnknownHostException e) {
            exception = new UnKnownHostError("Hostname can not be resolved: " + url + ".");
        } catch (SocketTimeoutException e) {
            exception = new TimeoutError("Request time out: " + url + ".");
        } catch (Exception e) {
            exception = e;
        } finally {
            if (exception != null)
                Logger.e(exception.getMessage());
        }
    //    builder.append("---^^^^^^^^^^^^^^---Request finish---^^^^^^^^^^^^^^---");
     //   Logger.d(builder.toString());
        return new Connection(network, responseHeaders, inputStream, exception);
    }

    /**
     * Handle retries, and complete the handle network here.
     *
     * @param request {@link BasicRequest}.
     * @return {@link Network}.
     * @throws Exception {@link #createNetwork(BasicRequest)}.
     */
    private Network createConnectionAndWriteData(BasicRequest<?> request) throws Exception {
        Network network = null;
        Exception exception = null;
        int retryCount = request.getRetryCount() + 1;
        boolean failed = true;
        for (; failed && retryCount > 0; retryCount--) {
            try {
                network = createNetwork(request);
                exception = null;
                failed = false;
            } catch (Exception e) {
                exception = e;
            }
        }
        if (failed) {
            throw exception;
        } else if (request.getRequestMethod().allowRequestBody()) {
            writeRequestBody(request, network.getOutputStream());
        }
        return network;
    }

    /**
     * The connection is established, including the head and send the handle body.
     *
     * @param request {@link BasicRequest}.
     * @return {@link HttpURLConnection} Have been established and the server connection, and send the complete data,
     * you can directly determine the response code and read the data.
     * @throws Exception can happen when the connection is established and send data.
     */
    private Network createNetwork(BasicRequest<?> request) throws Exception {
        // Pre operation notice.
        request.onPreExecute();

        String url = request.url();
        Headers headers = request.getHeaders();
        headers.set(Headers.HEAD_KEY_CONTENT_TYPE, request.getContentType());

        // Connection.
        List<String> values = headers.getValues(Headers.HEAD_KEY_CONNECTION);
        if (values == null || values.size() == 0)
            headers.add(Headers.HEAD_KEY_CONNECTION, Headers.HEAD_VALUE_CONNECTION_KEEP_ALIVE);

        // Content-Length.
        RequestMethod requestMethod = request.getRequestMethod();
        if (requestMethod.allowRequestBody())
            headers.set(Headers.HEAD_KEY_CONTENT_LENGTH, Long.toString(request.getContentLength()));

        // Cookie.
        headers.addCookie(new URI(url), NoHttp.getInitializeConfig().getCookieManager());

        // print
//        builder.append("\nRequest address: ").append(url)
//                .append("\nRequest  method:").append(request.getRequestMethod())
//                .append("\n");



        return mExecutor.execute(request);
    }

    /**
     * Write handle params.
     *
     * @param request      {@link BasicRequest}.
     * @param outputStream {@link OutputStream}.
     * @throws IOException io exception.
     */
    private void writeRequestBody(BasicRequest<?> request, OutputStream outputStream) throws IOException {
        // 6. Write handle body
        OutputStream realOutputStream = IOUtils.toBufferedOutputStream(outputStream);
        request.onWriteRequestBody(realOutputStream);
        IOUtils.closeQuietly(realOutputStream);
    }

    /**
     * The redirection process any response.
     *
     * @param oldRequest      need to redirect the {@link Request}.
     * @param responseHeaders need to redirect the handle of the responding head.
     * @return {@link Connection}.
     */
    private Connection handleRedirect(BasicRequest<?> oldRequest, Headers responseHeaders) {
        // redirect handle
        BasicRequest<?> redirectRequest = null;
        RedirectHandler redirectHandler = oldRequest.getRedirectHandler();
        if (redirectHandler != null) {
            if (redirectHandler.isDisallowedRedirect(responseHeaders))
                return new Connection(null, responseHeaders, null, null);
            else {
                redirectRequest = redirectHandler.onRedirect(oldRequest, responseHeaders);
            }
        }
        if (redirectRequest == null) {
            String location = responseHeaders.getLocation();

            if (!URLUtil.isNetworkUrl(location)) {
                String oldUrl = oldRequest.url();
                try {
                    URL url = new URL(oldUrl);
                    location = location.startsWith("/") ? location : "/" + location;
                    location = url.getProtocol() + "://" + url.getHost() + location;
                } catch (MalformedURLException ignored) {
                }
            }

            redirectRequest = new BasicRequest(location, oldRequest.getRequestMethod());
            redirectRequest.setRedirectHandler(oldRequest.getRedirectHandler());
            redirectRequest.setSSLSocketFactory(oldRequest.getSSLSocketFactory());
            redirectRequest.setHostnameVerifier(oldRequest.getHostnameVerifier());
            redirectRequest.setParamsEncoding(oldRequest.getParamsEncoding());
            redirectRequest.setProxy(oldRequest.getProxy());
        }
        return getConnection(redirectRequest);
    }

    /**
     * Parse server response headers, here will save cookies.
     *
     * @param uri             according to the requested URL generated uris.
     * @param responseCode    responseCode.
     * @param responseHeaders responseHeaders of server.
     * @return response headers of server.
     */
    private Headers parseResponseHeaders(URI uri, int responseCode, Map<String, List<String>> responseHeaders) {
        // handle cookie
        try {
            NoHttp.getInitializeConfig().getCookieManager().put(uri, responseHeaders);
        } catch (IOException e) {
            Logger.e(e + "Save cookie filed: " + uri.toString() + ".");
        }

        // handle headers
        Headers headers = new Headers();
        for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }
        headers.set(Headers.HEAD_KEY_RESPONSE_CODE, Integer.toString(responseCode));
//        for (String headKey : headers.keySet()) {
//            List<String> headValues = headers.getValues(headKey);
//
//            for (String headValue : headValues) {
//                if (!TextUtils.isEmpty(headKey))
//                    builder.append("\n").append(headKey).append(" : ");
//                if (!TextUtils.isEmpty(headValue))
//                    builder.append(headValue).append("\n");
//            }
//
//        }
        return headers;
    }

    ////////// Read response body //////////

    /**
     * This requestMethod and responseCode has responseBody ?
     *
     * @param requestMethod it's come from {@link RequestMethod}.
     * @param responseCode  responseCode from server.
     * @return true: there is data, false: no data.
     */
    public static boolean hasResponseBody(RequestMethod requestMethod, int responseCode) {
        return requestMethod != RequestMethod.HEAD && hasResponseBody(responseCode);
    }

    /**
     * According to the response code to judge whether there is data.
     *
     * @param responseCode responseCode.
     * @return true: there is data, false: no data.
     */
    public static boolean hasResponseBody(int responseCode) {
        return !(100 <= responseCode && responseCode < 200) &&
                responseCode != 204 &&
                responseCode != 205 &&
                !(300 <= responseCode && responseCode < 400);
    }

}
