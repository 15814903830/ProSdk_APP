package com.zero.ci.network.zrequest.request;

import android.content.Context;

import com.zero.ci.network.http.rest.download.DownloadListener;

/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:
 * 下载请求
 * ------------------------
 * 1.封装AbstractResponse普通请求
 * 2.封装AdaptResponse适配Rx请求
 */
public class DownloadRequest extends BaseRequest<DownloadRequest> {
    public DownloadRequest(Context context, String url) {
        this.url = url;
        this.context = context;
    }

    public void execute(DownloadListener l) {
        RequestManager.loadDownload(context, url, what, fileFolder, fileName, isRange, isDeleteOld, l);
    }
}
