package com.zero.ci.network.okconnect;

import com.zero.ci.network.http.rest.Network;
import com.zero.ci.network.http.rest.NetworkExecutor;
import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.rest.request.BasicRequest;
import com.zero.ci.widget.logger.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class OkHttpNetworkExecutor implements NetworkExecutor {

    @Override
    public Network execute(BasicRequest<?> request) throws Exception {
        URL url = new URL(request.url());
        HttpURLConnection connection = URLConnectionFactory.getInstance().open(url, request.getProxy());
        connection.setConnectTimeout(request.getConnectTimeout());
        connection.setReadTimeout(request.getReadTimeout());
        connection.setInstanceFollowRedirects(false);

        if (connection instanceof HttpsURLConnection) {
            SSLSocketFactory sslSocketFactory = request.getSSLSocketFactory();
            if (sslSocketFactory != null)
                ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
            HostnameVerifier hostnameVerifier = request.getHostnameVerifier();
            if (hostnameVerifier != null)
                ((HttpsURLConnection) connection).setHostnameVerifier(hostnameVerifier);
        }

        connection.setRequestMethod(request.getRequestMethod().getValue());

        connection.setDoInput(true);
        boolean isAllowBody = request.getRequestMethod().allowRequestBody();
        connection.setDoOutput(isAllowBody);

        Headers headers = request.getHeaders();

        List<String> values = headers.getValues(Headers.HEAD_KEY_CONNECTION);
        if (values == null || values.size() == 0)
            headers.add(Headers.HEAD_KEY_CONNECTION, Headers.HEAD_VALUE_CONNECTION_KEEP_ALIVE);

        if (isAllowBody)
            headers.set(Headers.HEAD_KEY_CONTENT_LENGTH, Long.toString(request.getContentLength()));

        Map<String, String> requestHeaders = headers.toRequestHeaders();
      //  StringBuilder builder = new StringBuilder();
       // builder.append("OkHttpNetworkExecutor |.0.|").append("\n");
        for (Map.Entry<String, String> headerEntry : requestHeaders.entrySet()) {
            String headKey = headerEntry.getKey();
            String headValue = headerEntry.getValue();
        //    builder.append("\n").append(headKey).append(" : ").append(headValue).append("\n");
            connection.setRequestProperty(headKey, headValue);
        }
       // Logger.i(builder.toString());

        // 5. Connect
        connection.connect();
        return new OkHttpNetwork(connection);
    }
}
