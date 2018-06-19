package com.mvpframe.capabilities.http.cacahe;

import android.content.Context;

import okhttp3.Cache;

/**
 * CJY
 */
public class CacheProvide {
    Context mContext;

    public CacheProvide(Context context) {
        mContext = context;
    }

    public Cache provideCache() {
        return new Cache(mContext.getCacheDir(), 50*1024 * 1024);
    }
}
