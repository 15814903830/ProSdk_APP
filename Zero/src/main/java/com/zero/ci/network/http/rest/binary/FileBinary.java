package com.zero.ci.network.http.rest.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A default implementation of Binary.
 * All the methods are called in Son thread.
 */
public class FileBinary extends BasicBinary {

    private File mFile;

    /**
     * File binary.
     *
     * @param file a file.
     */
    public FileBinary(File file) {
        this(file, file.getName(), null);
    }

    /**
     * File binary.
     *
     * @param file     a file.
     * @param fileName file name.
     */
    public FileBinary(File file, String fileName) {
        this(file, fileName, null);
    }

    /**
     * File binary.
     *
     * @param file     a file.
     * @param fileName file name.
     * @param mimeType content type.
     */
    public FileBinary(File file, String fileName, String mimeType) {
        super(fileName, mimeType);
        if (file == null)
            throw new IllegalArgumentException("File is null: " + fileName);
        if (!file.exists())
            throw new IllegalArgumentException("File not found: " + fileName);
        mFile = file;
    }



    @Override
    public long getBinaryLength() {
        return mFile.length();
    }

    @Override
    protected InputStream getInputStream() throws IOException {
        return new FileInputStream(mFile);
    }
}
