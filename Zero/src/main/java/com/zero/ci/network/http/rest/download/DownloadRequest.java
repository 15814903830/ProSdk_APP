package com.zero.ci.network.http.rest.download;

import com.zero.ci.network.http.rest.request.BasicRequest;
import com.zero.ci.network.http.RequestMethod;

import java.io.File;

/**
 * File download handle based on BasicRequest.
 */
public class DownloadRequest extends BasicRequest<DownloadRequest> {
    /**
     * Also didn't download to start download again.
     */
    public static final int STATUS_RESTART = 0;
    /**
     * Part has been downloaded, continue to download last time.
     */
    public static final int STATUS_RESUME = 1;
    /**
     * Has the download is complete, not to download operation.
     */
    public static final int STATUS_FINISH = 2;

    /**
     * File the target folder.
     */
    private final String mFileDir;
    /**
     * The file target name.
     */
    private final String mFileName;
    /**
     * If is to download a file, whether the breakpoint continuing.
     */
    private final boolean isRange;
    /**
     * If there is a old files, whether to delete the old files.
     */
    private final boolean isDeleteOld;

    /**
     * Create download handle.
     *
     * @param url           url.
     * @param requestMethod {@link RequestMethod}.
     * @param fileFolder    file save folder.
     * @param isDeleteOld   find the same when the file is deleted after download, or on behalf of the download is
     *                      complete, not to handle the network.
     * @see #DownloadRequest(String, RequestMethod, String, String, boolean, boolean)
     */
    public DownloadRequest(String url, RequestMethod requestMethod, String fileFolder, boolean isRange, boolean isDeleteOld) {
        this(url, requestMethod, fileFolder, null, isRange, isDeleteOld);
    }

    /**
     * Create a download object.
     *
     * @param url           download address.
     * @param requestMethod {@link RequestMethod}.
     * @param fileFolder    folder to save file.
     * @param filename      filename.
     * @param isRange       whether the breakpoint continuing.
     * @param isDeleteOld   find the same when the file is deleted after download, or on behalf of the download is
     *                      complete, not to handle the network.
     * @see #DownloadRequest(String, RequestMethod, String, boolean, boolean)
     */
    public DownloadRequest(String url, RequestMethod requestMethod, String fileFolder, String filename, boolean isRange, boolean isDeleteOld) {
        super(url, requestMethod);
        this.mFileDir = fileFolder;
        this.mFileName = filename;
        this.isRange = isRange;
        this.isDeleteOld = isDeleteOld;
    }

    /**
     * Return the mFileDir.
     *
     * @return it won't be empty.
     */
    public String getFileDir() {
        return this.mFileDir;
    }

    /**
     * Return the mFileName.
     *
     * @return it won't be empty.
     */
    public String getFileName() {
        return this.mFileName;
    }

    /**
     * Return the isRange.
     *
     * @return true: breakpoint continuing, false: don't need a breakpoint continuing.
     */
    public boolean isRange() {
        return this.isRange;
    }

    /**
     * If there is a old files, whether to delete the old files.
     *
     * @return true: deleted, false: don't delete.
     */
    public boolean isDeleteOld() {
        return this.isDeleteOld;
    }

    /**
     * <p>
     * Query before download status {@link #STATUS_RESTART} representative no download do to download again; Download
     * {@link #STATUS_RESUME} represents a part of, to continue to download; {@link #STATUS_FINISH} representatives
     * have finished downloading.
     * </p>
     *
     * @return int value, compared with the {@value #STATUS_RESTART}, {@value #STATUS_RESUME}, {@value #STATUS_FINISH}.
     * @see #STATUS_RESTART
     * @see #STATUS_RESUME
     * @see #STATUS_FINISH
     */
    public int checkBeforeStatus() {
        if (this.isRange) {
            try {
                File lastFile = new File(mFileDir, mFileName);
                if (lastFile.exists() && !isDeleteOld)
                    return STATUS_FINISH;
                File tempFile = new File(mFileDir, mFileName + ".nohttp");
                if (tempFile.exists())
                    return STATUS_RESUME;
            } catch (Exception ignored) {
            }
        }
        return STATUS_RESTART;
    }
}
