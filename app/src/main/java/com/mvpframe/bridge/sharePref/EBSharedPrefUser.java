package com.mvpframe.bridge.sharePref;

import android.content.Context;

import com.mvpframe.capabilities.cache.BaseSharedPreference;


/**
 * <用户信息缓存>
 */
public class EBSharedPrefUser extends BaseSharedPreference {
    /**
     * 登录名
     */
    public static final String USER_NAME = "user_name";

    public EBSharedPrefUser(Context context, String fileName) {
        super(context,fileName);
    }
}
