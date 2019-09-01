package com.zero.ci.network.http.rest.download;

import com.zero.ci.network.http.NoHttp;

/**
 * Synchronize File Downloader.
 */
public enum SyncDownloadExecutor {

    INSTANCE, AsyncRequestExecutor;

    private Downloader mDownloader;

    SyncDownloadExecutor() {
        mDownloader = new Downloader(NoHttp.getInitializeConfig().getNetworkExecutor());
    }

    /**
     * Start a download.
     *
     * @param what            what.
     * @param downloadRequest {@link DownloadRequest}.
     * @param listener        accept various download status callback..
     */
    public void execute(int what, DownloadRequest downloadRequest, DownloadListener listener) {
        mDownloader.download(what, downloadRequest, listener);
    }
}
