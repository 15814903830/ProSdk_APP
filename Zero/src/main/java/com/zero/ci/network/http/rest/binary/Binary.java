package com.zero.ci.network.http.rest.binary;

import com.zero.ci.network.http.able.Cancelable;

import java.io.OutputStream;

/**
 * File interface.
 * All the methods are called in Son thread.
 */
public interface Binary extends Cancelable {

    /**
     * Returns the size of the Binary, if size is 0, the Binary BasicSQLHelper will not be sent. The rest of the
     * {@link Binary} method will not be invoked.
     *
     * @return Long length.
     */
    long getLength();

    /**
     * Write your Binary data through flow out.
     *
     * @param outputStream {@link OutputStream}.
     */
    void onWriteBinary(OutputStream outputStream);

    /**
     * Return the fileName, Can be null.
     *
     * @return File name.
     */
    String getFileName();

    /**
     * Return mimeType of binary.
     *
     * @return MimeType.
     */
    String getMimeType();
}
