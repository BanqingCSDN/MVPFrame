package com.mvpframe.capabilities.http.interfaces;

/**
 * CJY
 */
@FunctionalInterface
public interface Success<T> {
    void success(T model);
}
