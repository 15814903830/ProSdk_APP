package com.zero.ci.network.http.able;

/**
 * Finish interface.
 */
public interface Finishable {

    /**
     * Finish handle.
     */
    void finish();

    /**
     * Whether they have been completed.
     *
     * @return true: finished, false: unfinished.
     */
    boolean isFinished();
}
