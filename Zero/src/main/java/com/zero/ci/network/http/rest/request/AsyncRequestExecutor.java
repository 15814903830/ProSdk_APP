package com.zero.ci.network.http.rest.request;

import com.zero.ci.widget.logger.Logger;
import com.zero.ci.network.http.rest.response.OnResponseListener;
import com.zero.ci.network.http.rest.response.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Asynchronous handle executor.
 */
public enum AsyncRequestExecutor {

    INSTANCE;

    /**
     * ExecutorService.
     */
    private ExecutorService mExecutorService;

    AsyncRequestExecutor() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    public <T> void execute(int what, Request<T> request, OnResponseListener<T> listener) {
        mExecutorService.execute(new RequestTask<>(request, RequestMessenger.newInstance(what, listener)));
    }

    private static class RequestTask<T> implements Runnable {

        private Request<T> request;
        public RequestMessenger mMessenger;

        private RequestTask(Request<T> request, RequestMessenger messenger) {
            this.request = request;
            this.mMessenger = messenger;
        }

        @Override
        public void run() {
            if (request.isCanceled()) {
                Logger.d(request.url() + " is canceled.");
                return;
            }

            // start.
            request.start();
            mMessenger.start();

            // handle.
            Response<T> response = SyncRequestExecutor.INSTANCE.execute(request);

            if (request.isCanceled()) {
                Logger.d(request.url() + " finish, but it's canceled.");
            } else {
                //noinspection unchecked
                mMessenger.response(response);
            }

            // finish.
            request.finish();
            mMessenger.finish();
        }
    }

}
