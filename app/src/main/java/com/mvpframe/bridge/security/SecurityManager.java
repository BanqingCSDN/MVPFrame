package com.mvpframe.bridge.security;

import android.content.Context;

import com.mvpframe.bridge.BridgeLifeCycleListener;
import com.mvpframe.capabilities.security.SecurityUtils;

/**
 * <加解密管理类>

 */
public class SecurityManager implements BridgeLifeCycleListener {

    @Override
    public void initOnApplicationCreate(Context context) {

    }

    @Override
    public void clearOnApplicationQuit() {

    }

    /**
     * md5 加密
     * @param str
     * @return
     */
    public String get32MD5Str(String str){
        return SecurityUtils.get32MD5Str(str);
    }

}
