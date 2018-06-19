package com.mvpframe.ui.personcenter;

import android.os.Bundle;
import android.widget.TextView;

import com.mvpframe.R;
import com.mvpframe.bean.home.LoginResp;
import com.mvpframe.biz.personcenter.LoginPresenter;
import com.mvpframe.bridge.BridgeFactory;
import com.mvpframe.bridge.Bridges;
import com.mvpframe.bridge.sharePref.EBSharedPrefManager;
import com.mvpframe.bridge.sharePref.EBSharedPrefUser;
import com.mvpframe.ui.base.BaseActivity;

import butterknife.BindView;


public class MainActivity extends BaseActivity {

    LoginPresenter loginPresenter;

    private TextView text;
    private TextView text1;
   // @BindView(R.id.text1) TextView text1;

    @Override
    public void onError(String errorMsg, String code) {
        showToast(errorMsg);
    }

    @Override
    public void onSuccess(Object s) {
        EBSharedPrefManager sharedPref = BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);
        text.setText(sharedPref.getKDPreferenceUserInfo().getString(EBSharedPrefUser.USER_NAME,""));
        text1.setText(((LoginResp)s).getUserInfo().getPwd());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        presenter = loginPresenter = new LoginPresenter();
        loginPresenter.attachView(this);
    }

    @Override
    public void initViews() {
        text= (TextView) findViewById(R.id.text);
        text1= (TextView) findViewById(R.id.text1);

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
        loginPresenter.login(this,"15713802736","123456");
    }
}
