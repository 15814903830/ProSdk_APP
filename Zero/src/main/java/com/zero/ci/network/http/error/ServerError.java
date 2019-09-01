package com.zero.ci.network.http.error;

public class ServerError extends Exception {

    private static final long serialVersionUID = 1854642L;

    private String errorBody;

    public ServerError() {
    }

    public ServerError(String detailMessage) {
        super(detailMessage);
    }

    public ServerError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ServerError(Throwable throwable) {
        super(throwable);
    }

    /**
     * To getList the wrong information.
     *
     * @return the error message.
     */
    public String getErrorBody() {
        return errorBody;
    }

    /**
     * To set the wrong information.
     *
     * @param errorBody the error message.
     */
    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }
}
