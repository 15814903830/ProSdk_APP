package com.zero.ci.network.zrequest.response;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 上传参数配置
 * -------------------------------
 */
public class UploadFile implements Serializable {
    /**
     * 监听（默认是0）
     */
    private int what = 0;

    /**
     * 文件模式
     * File
     * Bitmap
     * FileInputStream
     */
    private Object mode;

    private File mFile;

    private FileInputStream mFileInputStream;

    Bitmap mBitmap ;

    /**
     * 上传文件key的名字
     */
    private String key;


    public UploadFile(FileInputStream fileInputStream, String key) {
        mFileInputStream = fileInputStream;
        this.key = key;
    }

    public UploadFile(int what, Object mode, String key) {
        this.what = what;
        this.mode = mode;
        this.key = key;
    }

    public FileInputStream getFileInputStream() {
        return mFileInputStream;
    }

    public void setFileInputStream(FileInputStream fileInputStream) {
        mFileInputStream = fileInputStream;
    }

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public Object getMode() {
        return mode;
    }

    public void setMode(Object mode) {
        this.mode = mode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    @Override
    public String toString() {
        return "UploadFile{" +
                "what=" + what +
                ", mode=" + mode +
                ", key='" + key + '\'' +
                '}';
    }
}


