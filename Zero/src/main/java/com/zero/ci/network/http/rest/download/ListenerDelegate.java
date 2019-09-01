package com.zero.ci.network.http.rest.download;

import com.zero.ci.network.http.rest.headers.Headers;

import java.util.Map;

public class ListenerDelegate implements DownloadListener {

    private final DownloadRequest mRequest;
    private final Map<DownloadRequest, DownloadMessenger> mMessengerMap;

    public ListenerDelegate(DownloadRequest request, Map<DownloadRequest, DownloadMessenger> messengerMap) {
        mRequest = request;
        mMessengerMap = messengerMap;
    }

    @Override
    public void onDownloadError(int what, Exception exception) {
        mMessengerMap.get(mRequest).error(exception);
    }

    @Override
    public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
        mMessengerMap.get(mRequest).start(isResume, rangeSize, responseHeaders, allCount);
    }

    @Override
    public void onProgress(int what, int progress, long fileCount, long speed) {
        mMessengerMap.get(mRequest).progress(progress, fileCount, speed);
    }

    @Override
    public void onFinish(int what, String filePath) {
        mMessengerMap.get(mRequest).finish(filePath);
    }

    @Override
    public void onCancel(int what) {
        mMessengerMap.get(mRequest).cancel();
    }
}