package com.zero.ci.network.http.rest.download;

import android.os.Process;

import com.zero.ci.widget.logger.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Download queue polling thread.
 */
class DownloadDispatcher extends Thread {

    private final BlockingQueue<DownloadRequest> mRequestQueue;
    private final List<DownloadRequest> mRequestList;
    private final Map<DownloadRequest, DownloadMessenger> mMessengerMap;

    private boolean mQuit = false;

    public DownloadDispatcher(BlockingQueue<DownloadRequest> requestQueue, List<DownloadRequest> requestList, Map<DownloadRequest, DownloadMessenger> messengerMap) {
        this.mRequestQueue = requestQueue;
        this.mRequestList = requestList;
        this.mMessengerMap = messengerMap;
    }

    /**
     * Quit this thread.
     */
    public void quit() {
        mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (!mQuit) {
            final DownloadRequest request;
            try {
                request = mRequestQueue.take();
            } catch (InterruptedException e) {
                if (mQuit)
                    return;
                continue;
            }

            if (request.isCanceled()) {
                mRequestQueue.remove(request);
                mRequestList.remove(request);
                mMessengerMap.remove(request);
                Logger.d(request.url() + " is canceled.");
                continue;
            }

            request.start();
            SyncDownloadExecutor.INSTANCE.execute(0, request, new ListenerDelegate(request, mMessengerMap));
            request.finish();

            // remove it from queue.
            mRequestQueue.remove(request);
            mRequestList.remove(request);
            mMessengerMap.remove(request);
        }
    }
}
