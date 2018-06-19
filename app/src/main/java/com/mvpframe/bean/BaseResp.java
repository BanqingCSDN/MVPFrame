package com.mvpframe.bean;

import java.util.List;

/**
 * <网络请求返回体>
 */
public class BaseResp {
    /**
     * 返回状态码
     */
    private String loginState;



    public String getLoginState() {
        return loginState;
    }
    
    public void setLoginState(String loginState) {
        this.loginState = loginState;
    }



}
