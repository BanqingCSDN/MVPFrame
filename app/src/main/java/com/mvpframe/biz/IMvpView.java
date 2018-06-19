package com.mvpframe.biz;

/**
 * <功能详细描述>
 */
public interface IMvpView {
    /**
     * 出错
     * @param errorMsg
     * @param code
     */
    void onError(String errorMsg, String code);

    /**
     * 成功
     * @param s
     */

    void onSuccess(Object s);

    /**
     * 正在加载
     */
    void showLoading();

    /**
     * 默认显示
     */
    void hideLoading();
}
