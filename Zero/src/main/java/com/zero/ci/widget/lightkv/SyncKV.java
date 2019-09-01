package com.zero.ci.widget.lightkv;


import android.util.SparseArray;

import com.zero.ci.widget.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.concurrent.Executor;

/**
 * SyncKV need to commit after updating data, which like SharePreferences-commit mode.<br/>
 * To enhance the reliability，
 * SyncKV calculates the checksum of all data, and writes data to two files.
 */
public class SyncKV extends LightKV {
    private static final String TAG = "SyncKV";

    private FileChannel mAChannel;
    private FileChannel mBChannel;
    private ByteBuffer mBuffer;
    private boolean invalid = false;

    SyncKV(String path, String name, Class keyDefineClass,
           Executor executor, Encoder encoder) {
        super(path, name, keyDefineClass, executor, encoder, SYNC_MODE);
    }

    @Override
    protected ByteBuffer loadData(String path) throws IOException {
        File aFile = new File(path, mFileName + ".kva");
        File bFile = new File(path, mFileName + ".kvb");
        if (!LightKVUtils.existFile(aFile) || !LightKVUtils.existFile(bFile)) {
            throw new IllegalStateException("can not open file:" + mFileName);
        }

        RandomAccessFile aAccessFile = new RandomAccessFile(aFile, "rw");
        mAChannel = aAccessFile.getChannel();
        ByteBuffer aBuffer = ByteBuffer.allocateDirect((int) alignLength(mAChannel.size()));
        boolean aResult = readData(mAChannel, aBuffer);

        RandomAccessFile bAccessFile = new RandomAccessFile(bFile, "rw");
        mBChannel = bAccessFile.getChannel();
        ByteBuffer bBuffer = ByteBuffer.allocateDirect((int) alignLength(mBChannel.size()));
        boolean bResult = readData(mBChannel, bBuffer);

        if (bResult) {
            if (!aResult || !aBuffer.equals(bBuffer)) {
                writeData(bBuffer, mAChannel);
                bBuffer.rewind();
            }
            mBuffer = bBuffer;
        } else {
            if (aResult) {
                writeData(aBuffer, mBChannel);
                aBuffer.rewind();
            }
            mBuffer = aBuffer;
        }
        mDataEnd = mBuffer.limit();

        return mBuffer;
    }

    private void writeData(ByteBuffer buffer, FileChannel channel) throws IOException {
        buffer.position(0);
        channel.position(0);
        channel.truncate(buffer.limit());
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
        channel.force(false);
    }

    private boolean readData(FileChannel channel, ByteBuffer buffer) throws IOException {
        //noinspection StatementWithEmptyBody
        while (channel.read(buffer) > 0) {
        }
        buffer.flip();
        boolean valid = isValid(buffer);
        if (!valid) {
            buffer.rewind().limit(0);
            channel.position(0);
            channel.truncate(0);
        } else if (buffer.limit() > 8) {
            // remove digest
            buffer.limit(buffer.limit() - 8);
        }
        return valid;
    }

    private boolean isValid(ByteBuffer buffer) {
        int len = buffer.limit();
        if (len == 0) {
            return true;
        }
        if (len <= 8) {
            return false;
        }
        long hash = LightKVUtils.hash64(buffer.array(), len - 8);
        long digest = buffer.getLong(len - 8);
        return hash == digest;
    }

    @Override
    public synchronized void putBoolean(int key, boolean value) {
        Container.BooleanContainer container = (Container.BooleanContainer) mData.get(key);
        if (container == null) {
            invalid = true;
            mData.put(key, new Container.BooleanContainer(0, value));
            mDataEnd += 5;
        } else if (container.value != value) {
            invalid = true;
            container.value = value;
        }
    }

    @Override
    public synchronized void putInt(int key, int value) {
        Container.IntContainer container = (Container.IntContainer) mData.get(key);
        if (container == null) {
            invalid = true;
            mData.put(key, new Container.IntContainer(0, value));
            mDataEnd += 8;
        } else if (container.value != value) {
            invalid = true;
            container.value = value;
        }
    }

    @Override
    public synchronized void putFloat(int key, float value) {
        Container.FloatContainer container = (Container.FloatContainer) mData.get(key);
        if (container == null) {
            invalid = true;
            mData.put(key, new Container.FloatContainer(0, value));
            mDataEnd += 8;
        } else if (container.value != value) {
            invalid = true;
            container.value = value;
        }
    }

    @Override
    public synchronized void putLong(int key, long value) {
        Container.LongContainer container = (Container.LongContainer) mData.get(key);
        if (container == null) {
            invalid = true;
            mData.put(key, new Container.LongContainer(0, value));
            mDataEnd += 12;
        } else if (container.value != value) {
            invalid = true;
            container.value = value;
        }
    }

    @Override
    public synchronized void putDouble(int key, double value) {
        Container.DoubleContainer container = (Container.DoubleContainer) mData.get(key);
        if (container == null) {
            invalid = true;
            mData.put(key, new Container.DoubleContainer(mDataEnd, value));
            mDataEnd += 12;
        } else if (container.value != value) {
            invalid = true;
            container.value = value;
        }
    }

    /**
     * insert or update string
     *
     * @param key   key
     * @param value should not be null normally, it will be dealt with remove if value is null
     */
    @Override
    public synchronized void putString(int key, String value) {
        if (value == null) {
            remove(key);
        } else {
            Container.StringContainer container = (Container.StringContainer) mData.get(key);
            if (container == null) {
                invalid = true;
                byte[] bytes = value.getBytes();
                bytes = (key & DataType.ENCODE) != 0 ? mEncoder.encode(bytes) : bytes;
                mDataEnd += 8 + bytes.length;
                mData.put(key, new Container.StringContainer(0, value, bytes));
            } else if (!value.equals(container.value)) {
                invalid = true;
                byte[] bytes = value.getBytes();
                bytes = (key & DataType.ENCODE) != 0 ? mEncoder.encode(bytes) : bytes;
                mDataEnd += bytes.length - container.bytes.length;
                container.value = value;
                container.bytes = bytes;
            }
        }
    }

    /**
     * insert or update array
     *
     * @param key   key
     * @param value should not be null normally, it will be dealt with remove if value is null
     */
    @Override
    public synchronized void putArray(int key, byte[] value) {
        if (value == null) {
            remove(key);
        } else {
            Container.ArrayContainer container = (Container.ArrayContainer) mData.get(key);
            if (container == null) {
                invalid = true;
                byte[] bytes = (key & DataType.ENCODE) != 0 ? mEncoder.encode(value) : value;
                mDataEnd += 8 + bytes.length;
                mData.put(key, new Container.ArrayContainer(0, value, bytes));
            } else if (!Arrays.equals(value, container.value)) {
                invalid = true;
                byte[] bytes = (key & DataType.ENCODE) != 0 ? mEncoder.encode(value) : value;
                mDataEnd += bytes.length - container.bytes.length;
                container.value = value;
                container.bytes = bytes;
            }
        }
    }

    @Override
    public synchronized void remove(int key) {
        invalid = true;
        int index = mData.indexOfKey(key);
        if (index >= 0) {
            mData.removeAt(index);
            Container.BaseContainer container = (Container.BaseContainer) mData.valueAt(index);
            mDataEnd -= getContainerLength(key, container);
        }
    }

    @Override
    public synchronized void clear() {
        if (mData.size() > 0) {
            invalid = true;
            mDataEnd = 0;
            mData.clear();
        }
    }

    public synchronized void copy(final LightKV src) {
        if (src == null || this.mMode != src.mMode) {
            return;
        }
        if (mData.size() > 0) {
            mData.clear();
        }
        invalid = true;

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (src) {
            mDataEnd = src.mDataEnd;

            SparseArray<Object> data = src.mData;
            int n = data.size();
            for (int i = 0; i < n; i++) {
                mData.put(data.keyAt(i), data.valueAt(i));
            }
        }

        commit();
    }

    @Override
    public synchronized void commit() {
        if (!invalid) {
            return;
        }
        try {
            if (mData.size() == 0) {
                mAChannel.truncate(0);
                mBChannel.truncate(0);
                mAChannel.force(false);
                mBChannel.force(false);
                invalid = false;
                return;
            }
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }

        int newCapacity = (int) alignLength(mDataEnd + 8);
        int capacity = mBuffer.capacity();
        if (newCapacity != capacity) {
            mBuffer = ByteBuffer.allocateDirect(newCapacity);
        }
        mBuffer.clear();
        Parser.collect(mData, mBuffer);
        long digest = LightKVUtils.hash64(mBuffer.array(), mBuffer.position());
        mBuffer.putLong(digest);
        mBuffer.flip();

        try {
            writeData(mBuffer, mAChannel);
            mBuffer.rewind();
            writeData(mBuffer, mBChannel);
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }
        invalid = false;
    }
}
