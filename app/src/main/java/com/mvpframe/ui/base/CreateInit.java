package com.mvpframe.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * <公共方法抽象>
 *
 */
public interface CreateInit {

    public interface CreateInitActivity extends CreateInit {
        /**
         * 设置布局文件
         */
        public void setContentView(Bundle savedInstanceState);

    }
    public interface CreateInitFragment extends CreateInit {
        /**
         * 设置布局文件
         */
        public View initView(LayoutInflater inflater);

    }
    /**
     * 初始化布局组件
     */
    public void initViews();

    /**
     * 增加按钮点击事件
     */
    void initListeners();

    /**
     * 初始化数据
     */
    public void initData();

    /**
     * 初始化公共头部
     */
    public void setHeader();
}

