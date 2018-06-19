package com.mvpframe.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mvpframe.R;
import com.mvpframe.app.MApplication;
import com.mvpframe.biz.BasePresenter;
import com.mvpframe.biz.IMvpView;
import com.mvpframe.bridge.BridgeFactory;
import com.mvpframe.bridge.Bridges;
import com.mvpframe.bridge.http.HttpManager;
import com.mvpframe.constant.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * <基础activity>
 */
public abstract class BaseActivity extends Activity implements CreateInit.CreateInitActivity, PublishActivityCallBack, PresentationLayerFunc, IMvpView, View.OnClickListener {

    private PresentationLayerFuncHelper presentationLayerFuncHelper;

    private Unbinder unbinder;

    /**
     * 返回按钮
     */
    @BindView(R.id.ll_back) public LinearLayout back;

    /**
     * 标题，右边字符
     */
    @BindView(R.id.tv_title) public TextView title;
    @BindView(R.id.tv_right) public TextView right;

    public BasePresenter presenter;

    public final String TAG = this.getClass().getSimpleName();

    /**
     * Context对象
     */
    protected static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presentationLayerFuncHelper = new PresentationLayerFuncHelper(this);

        setContentView(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        mContext = this;
        initViews();
        initListeners();
        initData();
        setHeader();
        MApplication.ebApplication.addActivity(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void setHeader() {

    }

   /* @OnClick ({R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }*/

   public void backClick(View view) {
               finish();
   }


    @Override
    public void onClick(View v) {

    }

    public void onEventMainThread(Event event) {

    }

    @Override
    protected void onResume() {
        MApplication.ebApplication.currentActivityName = this.getClass().getName();
        super.onResume();
    }

    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void setResultOk(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) ;
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showToast(String msg) {
        presentationLayerFuncHelper.showToast(msg);
    }

    @Override
    public void showProgressDialog() {
        presentationLayerFuncHelper.showProgressDialog();
    }

    @Override
    public void hideProgressDialog() {
        presentationLayerFuncHelper.hideProgressDialog();
    }

    @Override
    public void showSoftKeyboard(View focusView) {
        presentationLayerFuncHelper.showSoftKeyboard(focusView);
    }

    @Override
    public void hideSoftKeyboard() {
        presentationLayerFuncHelper.hideSoftKeyboard();
    }

    @Override
    protected void onDestroy() {
        MApplication.ebApplication.deleteActivity(this);
        EventBus.getDefault().unregister(this);
        if (presenter != null) {
            presenter.detachView(this);
        }
        HttpManager httpManager = BridgeFactory.getBridge(Bridges.HTTP);
        httpManager.cancelActivityRequest(TAG);
        unbinder.unbind();
        super.onDestroy();
    }
}
