package com.mvpframe.biz;

/**
 * <基础业务类>
 *
 */
public interface Presenter<V> {

    void attachView(V view);
    void detachView(V view);
    String getName();
}
