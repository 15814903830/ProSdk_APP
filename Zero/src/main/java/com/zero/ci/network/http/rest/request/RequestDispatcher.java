package com.zero.ci.network.http.rest.request;

import android.os.Process;

import com.zero.ci.widget.logger.Logger;

import com.zero.ci.network.http.rest.response.Response;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Request queue polling thread.
 */
public class RequestDispatcher extends Thread {

    private final BlockingQueue<Request<?>> mRequestQueue;
    private final BlockingQueue<Request<?>> mUnFinishQueue;
    private final Map<Request<?>, RequestMessenger<?>> mMessengerMap;


    /**
     * Whether the current handle queue polling thread is out of.
     */
    private volatile boolean mQuit = false;

    public RequestDispatcher(BlockingQueue<Request<?>> requestQueue,BlockingQueue<Request<?>> unFinishQueue, Map<Request<?>, RequestMessenger<?>> messengerMap) {
        this.mRequestQueue = requestQueue;
        this.mUnFinishQueue = unFinishQueue;
        this.mMessengerMap = messengerMap;
    }

    /**
     * Exit polling thread.
     */
    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (!mQuit) {
            final Request<?> request;
            try {
                request = mRequestQueue.take();
            } catch (InterruptedException e) {
                if (mQuit) {
                    Logger.w("Queue exit, stop blocking.");
                    break;
                }
                Logger.e(e.getMessage() );
                continue;
            }

            if (request.isCanceled()) {
                mRequestQueue.remove(request);
                mUnFinishQueue.remove(request);
                mMessengerMap.remove(request);
                Logger.d(request.url() + " is canceled.");
                continue;
            }

            // start.
            request.start();
            mMessengerMap.get(request).start();

            // handle.
            Response response = SyncRequestExecutor.INSTANCE.execute(request);

            // response.
            if (request.isCanceled()) {
                Logger.d(request.url() + " finish, but it's canceled.");
            } else {
                // noinspection unchecked
                mMessengerMap.get(request).response(response);
            }

            // finish.
            request.finish();
            mMessengerMap.get(request).finish();

            // remove it from queue.
            mRequestQueue.remove(request);
            mUnFinishQueue.remove(request);
            mMessengerMap.remove(request);
        }
    }
}
