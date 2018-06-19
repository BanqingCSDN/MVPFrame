package com.mvpframe.biz.personcenter;

import android.content.Context;

import com.mvpframe.bean.home.LoginResp;
import com.mvpframe.biz.BasePresenter;
import com.mvpframe.biz.IMvpView;
import com.mvpframe.bridge.BridgeFactory;
import com.mvpframe.bridge.Bridges;
import com.mvpframe.bridge.http.HttpManager;
import com.mvpframe.bridge.security.SecurityManager;
import com.mvpframe.bridge.sharePref.EBSharedPrefManager;
import com.mvpframe.bridge.sharePref.EBSharedPrefUser;
import com.mvpframe.constant.URLUtil;

/**
 * <功能详细描述>
 */
public class LoginPresenter extends BasePresenter<IMvpView> {

    public LoginPresenter() {

    }

    /**
     * 登录业务
     * @param context
     * @param userid
     * @param pwd
     */
    public void login(Context context, String userid, String pwd) {
        //MD5加密
        SecurityManager security = BridgeFactory.getBridge(Bridges.SECURITY);
        //网络层
        HttpManager http = BridgeFactory.getBridge(Bridges.HTTP);
        //判断网络是否可用
        http.isConnected(context);
        mvpView.showLoading();
        http.tag(getName()).params("userid", userid).params("pwd",pwd).setClass(LoginResp.class).post(URLUtil.LOGIN);
        http.onCompleted(() -> {
            mvpView.hideLoading();
        }).success(s -> {
            EBSharedPrefManager sharedPref = BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);
            sharedPref.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.USER_NAME, ((LoginResp) s).getUserInfo().getUserid());
            mvpView.onSuccess(s);
        }).error(v -> {
            mvpView.onError((String) v[0], "");
        });
    }
}
