package com.zero.ci.network.http.able;

/**
 * Cancel interface.
 */
public interface Cancelable {

    /**
     * Cancel handle.
     */
    void cancel();

    /**
     * Whether has been cancelled.
     *
     * @return true: canceled, false: there is no cancel.
     */
    boolean isCanceled();

}
