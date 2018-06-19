package com.mvpframe.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


/**
 * <基础fragment>
 */

public abstract class BaseFragment extends Fragment implements CreateInit.CreateInitFragment, PublishActivityCallBack, PresentationLayerFunc, IMvpView, View.OnClickListener {

    private PresentationLayerFuncHelper presentationLayerFuncHelper;

    public BasePresenter presenter;

    public final String TAG = this.getClass().getSimpleName();
    /**
     * Activity对象
     */
    protected static Activity mActivity;

    private Unbinder unbinder;
    /**
     * 标题，右边字符
     */
    @BindView(R.id.tv_title) public TextView title;
    @BindView(R.id.tv_right) public TextView right;

    /**
     * 此方法可以得到上下文对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    /*
     * 返回一个需要展示的View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initView(inflater);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    /*
     * 当Activity初始化之后可以在这里进行一些数据的初始化操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presentationLayerFuncHelper = new PresentationLayerFuncHelper(mActivity);

        initViews();
        initListeners();
        initData();
        setHeader();

        MApplication.ebApplication.currentActivityName = this.getClass().getName();
    }

    @Override
    public void setHeader() {

    }

    @Override
    public void onClick(View v) {

    }

    public void onEventMainThread(Event event) {

    }


    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        Intent intent = new Intent(mActivity, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(mActivity, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void setResultOk(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) ;
        intent.putExtras(bundle);
        mActivity.setResult(RESULT_OK, intent);
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
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView(this);
        }
        HttpManager httpManager = BridgeFactory.getBridge(Bridges.HTTP);
        httpManager.cancelActivityRequest(TAG);
        unbinder.unbind();
    }
}
