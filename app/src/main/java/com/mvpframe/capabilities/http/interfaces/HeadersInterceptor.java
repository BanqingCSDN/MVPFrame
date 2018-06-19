package com.mvpframe.capabilities.http.interfaces;

import java.util.Map;

/**
 * CJY
 */
@FunctionalInterface
public interface HeadersInterceptor {
    Map checkHeaders(Map headers);
}
