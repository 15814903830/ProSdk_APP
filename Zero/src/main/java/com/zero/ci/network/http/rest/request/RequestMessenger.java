package com.zero.ci.network.http.rest.request;

import com.zero.ci.network.http.HandlerDelivery;
import com.zero.ci.network.http.rest.response.OnResponseListener;
import com.zero.ci.network.http.rest.response.Response;

class RequestMessenger<T> {

    private final int what;
    private final OnResponseListener<T> listener;

    private RequestMessenger(int what, OnResponseListener<T> listener) {
        this.what = what;
        this.listener = listener;
    }

    static <T> RequestMessenger<T> newInstance(int what, OnResponseListener<T> listener) {
        return new RequestMessenger<>(what, listener);
    }

    void start() {
        HandlerDelivery.getInstance().post(Poster.newInstance(what, listener).start());
    }

    void response(Response<T> response) {
        HandlerDelivery.getInstance().post(Poster.newInstance(what, listener).response(response));
    }

    void finish() {
        HandlerDelivery.getInstance().post(Poster.newInstance(what, listener).finish());
    }

    private static class Poster<T> implements Runnable {

        private static <T> Poster<T> newInstance(int what, OnResponseListener<T> listener) {
            return new Poster<>(what, listener);
        }

        private final int what;
        private final OnResponseListener<T> listener;

        private int command;
        private Response<T> response;

        private Poster(int what, OnResponseListener<T> listener) {
            this.what = what;
            this.listener = listener;
        }

        Poster<T> start() {
            this.command = -1;
            return this;
        }

        Poster<T> response(Response<T> response) {
            this.command = -2;
            this.response = response;
            return this;
        }

        Poster<T> finish() {
            this.command = -3;
            return this;
        }

        @Override
        public void run() {
            if (listener == null) return;
            switch (command) {
                case -1: {
                    listener.onStart(what);
                    break;
                }
                case -2: {
                    if (response.isSucceed())
                        listener.onSucceed(what, response);
                    else
                        listener.onFailed(what, response);
                    break;
                }
                case -3: {
                    listener.onFinish(what);
                    break;
                }
            }
        }
    }
}
