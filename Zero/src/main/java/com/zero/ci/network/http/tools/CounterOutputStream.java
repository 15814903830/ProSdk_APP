package com.zero.ci.network.http.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Measure the length of the flow.
 */
public class CounterOutputStream extends OutputStream {

    private final AtomicLong length = new AtomicLong(0L);

    public CounterOutputStream() {
    }

    public void writeLength(long count) {
        length.addAndGet(count);
    }

    public long get() {
        return length.get();
    }

    @Override
    public void write(int oneByte) throws IOException {
        length.addAndGet(1);
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        length.addAndGet(buffer.length);
    }

    @Override
    public void write(byte[] buffer, int offset, int count) throws IOException {
        length.addAndGet(count);
    }

    /**
     * Didn't do anything here.
     *
     * @throws IOException nothing.
     */
    @Override
    public void close() throws IOException {
    }

    /**
     * Didn't do anything here.
     *
     * @throws IOException nothing.
     */
    @Override
    public void flush() throws IOException {
    }
}
