package com.zero.ci.network.http.cache;

import android.content.Context;

import com.zero.ci.network.http.CacheStore;
import com.zero.ci.network.http.tools.Encryption;


public abstract class BasicCacheStore implements CacheStore<CacheEntity> {

    private Context mContext;

    public BasicCacheStore(Context context) {
        mContext = context;
    }

    protected String uniqueKey(String key) {
        key += mContext.getApplicationInfo().packageName;
        return Encryption.getMD5ForString(key);
    }

}