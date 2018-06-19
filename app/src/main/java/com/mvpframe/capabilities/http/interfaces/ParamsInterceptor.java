package com.mvpframe.capabilities.http.interfaces;

import java.util.Map;

/**
 * CJY
 */
@FunctionalInterface
public interface ParamsInterceptor {
    Map checkParams(Map params);
}
