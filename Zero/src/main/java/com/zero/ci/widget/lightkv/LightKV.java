
package com.zero.ci.widget.lightkv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.zero.ci.widget.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;

/**
 * LightKV is a Lightweight key-value storage component. <br/>
 * It's more efficient than SharePreferences.<br/>
 * <br/>
 * LightKV has two modes: SyncKV & AsyncKV <br/>
 * SyncKV is more reliable, but it will block until data flush to disk after committing.,<br/>
 * AsyncKV is not atomicity(no blocking when writing data), but more faster. <br/>
 */
public abstract class LightKV {
    private static final String TAG = "LightKV";

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * container size, index ref to {@link DataType}
     */
    private static final int[] CONTAINER_SIZE = {0, 5, 8, 8, 12, 12};

    static final int PAGE_SIZE = 4096;

    public static final int ASYNC_MODE = 0;
    public static final int SYNC_MODE = 1;
    public final int mMode;

    final String mFileName;
    final Encoder mEncoder;
    final SparseArray<Object> mData = new SparseArray<>(16);
    int mDataEnd;
    private SparseArray<String> mKeyArray;
    private final Object mWaiter = new Object();

    LightKV(final String path,
            final String name,
            final Class keyDefineClass,
            Executor executor,
            Encoder encoder,
            int mode) {
        mFileName = name;
        mEncoder = encoder;
        mMode = mode;

        if (executor == null) {
            getData(path, keyDefineClass);
        } else {
            synchronized (mWaiter) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        getData(path, keyDefineClass);
                    }
                });
                try {
                    // wait util loadData() get the object (current object) lock
                    mWaiter.wait();
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    protected abstract ByteBuffer loadData(String path) throws IOException;

    private synchronized void getData(String path, Class keyDefineClass) {
        // we got the object lock, notify waiter to continue the procedure on that thread
        synchronized (mWaiter) {
            mWaiter.notify();
        }

        try {
            ByteBuffer buffer = loadData(path);
            getKeyArray(keyDefineClass);

            int invalidBytes;
            try {
                invalidBytes = Parser.parseData(mData, buffer, mKeyArray, mEncoder);
            } catch (Exception e) {
                invalidBytes = -1;
                Logger.e(e.getMessage());
            }

            clean(invalidBytes);
        } catch (Exception e) {
            Logger.e(e.getMessage());
            throw new IllegalStateException("init " + mFileName + "failed", e);
        }
    }

    protected void clean(int invalidBytes) throws IOException {
        // declare for AsyncKV
    }

    int getContainerLength(int key, Container.BaseContainer container) {
        if (container != null) {
            int type = key & DataType.MASK;
            if (type <= DataType.DOUBLE) {
                return CONTAINER_SIZE[type >> DataType.OFFSET];
            } else if (type == DataType.STRING) {
                return 8 + ((Container.StringContainer) container).bytes.length;
            } else if (type == DataType.ARRAY) {
                return 8 + ((Container.ArrayContainer) container).bytes.length;
            }
        }
        return 0;
    }

    private void getKeyArray(Class keyDefineClass) {
        if (keyDefineClass == null) {
            return;
        }
        try {
            Field[] fields = keyDefineClass.getDeclaredFields();
            mKeyArray = new SparseArray<>(fields.length);
            for (Field field : fields) {
                if (field.getType() == int.class
                        && Modifier.isPublic(field.getModifiers())
                        && Modifier.isFinal(field.getModifiers())) {
                    mKeyArray.put(field.getInt(keyDefineClass), field.getName());
                }
            }
        } catch (Exception e) {
            if (mKeyArray != null) {
                mKeyArray.clear();
            }
            Logger.e(new IllegalStateException(mFileName + "key define error.", e).getMessage());
        }
    }

    static long alignLength(long len) {
        if (len <= 0) {
            return PAGE_SIZE;
        } else if ((len & 0xFFF) != 0) {
            return ((len + PAGE_SIZE) >> 12) << 12;
        }
        return len;
    }

    public synchronized boolean getBoolean(int key) {
        Container.BooleanContainer container = (Container.BooleanContainer) mData.get(key);
        return container != null && container.value;
    }

    public synchronized int getInt(int key) {
        Container.IntContainer container = (Container.IntContainer) mData.get(key);
        return container == null ? 0 : container.value;
    }

    public synchronized float getFloat(int key) {
        Container.FloatContainer container = (Container.FloatContainer) mData.get(key);
        return container == null ? 0F : container.value;
    }

    public synchronized long getLong(int key) {
        Container.LongContainer container = (Container.LongContainer) mData.get(key);
        return container == null ? 0L : container.value;
    }

    public synchronized double getDouble(int key) {
        Container.DoubleContainer container = (Container.DoubleContainer) mData.get(key);
        return container == null ? 0D : container.value;
    }

    public synchronized String getString(int key) {
        Container.StringContainer container = (Container.StringContainer) mData.get(key);
        return container == null ? "" : container.value;
    }

    public synchronized byte[] getArray(int key) {
        Container.ArrayContainer container = (Container.ArrayContainer) mData.get(key);
        return container == null ? EMPTY_BYTE_ARRAY : container.value;
    }

    public abstract void putBoolean(int key, boolean value);

    public abstract void putInt(int key, int value);

    public abstract void putFloat(int key, float value);

    public abstract void putLong(int key, long value);

    public abstract void putDouble(int key, double value);

    public abstract void putString(int key, String value);

    public abstract void putArray(int key, byte[] value);

    public abstract void remove(int key);

    public abstract void clear();

    /**
     * copy from src to current
     */
    public abstract void copy(LightKV src);

    public abstract void commit();

    public synchronized boolean contains(int key) {
        return mData.indexOfKey(key) >= 0;
    }

    @Override
    public synchronized String toString() {
        return Parser.toString(mData, mKeyArray, mFileName);
    }


    public interface Encoder {
        byte[] encode(byte[] src);

        byte[] decode(byte[] des);
    }

    public static class Builder {
        private String path;
        private String name;
        private Class keyDefineClass;
        private Executor executor;
        private Encoder encoder;

        public Builder(Context context, String name) {
            if (context == null) {
                throw new IllegalArgumentException("context is null");
            }
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("name is empty");
            }
            this.path = getFilePath(context);
            this.name = name;
        }

        @SuppressLint("SdCardPath")
        private String getFilePath(Context context) {
            File filesDir = context.getFilesDir();
            if (filesDir != null) {
                return filesDir.getAbsolutePath();
            } else {
                return "/data/data/" + context.getPackageName() + "/files";
            }
        }

        /**
         * @param path file path (directory and name) of file to save data
         */
        public Builder(String path, String name) {
            if (TextUtils.isEmpty(path)) {
                throw new IllegalArgumentException("path is empty");
            }
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("name is empty");
            }
            this.path = path;
            this.name = name;
        }

        /**
         * AsyncKV will skip the keys which not defined by keyDefineClass when loading, <br/>
         * and compact the file in appropriately， to save storage and speed up next loading.
         * <p>
         * <p/>
         * If keyDefineClass not set, or keys shrunk by ProGuard when packing,
         * all valid (key>0) key-values will loading into container (in memory), even those no longer used.
         *
         * @param keyDefineClass the class which define the keys
         */
        public Builder keys(Class keyDefineClass) {
            this.keyDefineClass = keyDefineClass;
            return this;
        }

        /**
         * If not set, AsyncKV will load data in current thread
         *
         * @param executor executor to provider thread to load data asynchronously
         */
        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }


        public Builder encoder(Encoder encoder) {
            this.encoder = encoder;
            return this;
        }

        public AsyncKV async() {
            return new AsyncKV(path, name, keyDefineClass, executor, encoder);
        }

        public SyncKV sync() {
            return new SyncKV(path, name, keyDefineClass, executor, encoder);
        }
    }
}
