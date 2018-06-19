package com.mvpframe.bridge.http;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.mvpframe.bridge.BridgeLifeCycleListener;
import com.mvpframe.capabilities.http.HttpBuilder;
import com.mvpframe.capabilities.http.interfaces.Error;
import com.mvpframe.capabilities.http.interfaces.OnCompleted;
import com.mvpframe.capabilities.http.interfaces.Progress;
import com.mvpframe.capabilities.http.interfaces.Success;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class HttpManager<T> implements BridgeLifeCycleListener {

    private HttpBuilder builder;

    @Override
    public void initOnApplicationCreate(Context context) {

    }

    public HttpManager (){
        builder=new HttpBuilder();
    }

    /**
     * 是否允许缓存，传入时间如：1*3600 代表一小时缓存时效
     *
     * @param time 缓存时间 单位：秒
     */
    public HttpManager cacheTime(int time) {
        builder.header("Cache-Time", time + "");
        return this;
    }

    public HttpManager path(@NonNull String path) {
        builder.path(path);
        return this;
    }

    public HttpManager tag(@NonNull Object tag) {
        builder.tag(tag);
        return this;
    }

    public HttpManager params(@NonNull Map<String, String> params) {
        builder.params(params);
        return this;
    }

    public HttpManager params(@NonNull String key, String value) {
        builder.params(key, value);
        return this;
    }

    public HttpManager headers(@NonNull Map<String, String> headers) {
        builder.headers(headers);
        return this;
    }

    public HttpManager header(@NonNull String key, String value) {
        builder.header(key, value);
        return this;
    }

    public HttpManager success(@NonNull Success success) {
        builder.success(success);
        return this;
    }

    public HttpManager onCompleted(@NonNull OnCompleted onCompleted) {
        builder.onCompleted(onCompleted);
        return this;
    }

    public HttpManager progress(@NonNull Progress progress) {
        builder.progress(progress);
        return this;
    }

    public HttpManager error(@NonNull Error error) {
        builder.error(error);
        return this;
    }

    public HttpManager isConnected(@NonNull Context context) {
        builder.isConnected(context);
        return this;
    }

    public HttpManager setClass(@NonNull Class<T> clazz) {
        builder.setClass(clazz);
        return this;
    }

    public void get(@NonNull String url) {
        //设置请求地址
        builder.get(url);
    }

    public void get(@NonNull String url, @NonNull Success success, @NonNull Error error) {
        //设置请求地址
        builder.success(success).error(error).get(url);
    }

    public void get(@NonNull String url, @NonNull Map<String, String> params, @NonNull Success success, @NonNull Error error) {
        //设置请求地址
        builder.params(params).success(success).error(error).get(url);
    }

    public void post(@NonNull String url) {
        //设置请求地址
        builder.post(url);
    }

    public void post(@NonNull String url, @NonNull Success success, @NonNull Error error) {
        //设置请求地址
        builder.success(success).error(error).post(url);
    }

    public void post(@NonNull String url, @NonNull Map<String, String> params, @NonNull Success success, @NonNull Error error) {
        //设置请求地址
        builder.params(params).success(success).error(error).post(url);
    }

    public Observable<ResponseBody> Obdownload(String url) {
        return builder.Obdownload(url);
    }

    //下载
    public void download(String url) {
        builder.download(url);
    }


    @CheckResult
    public Observable<String> Obget(String url) {
        return builder.Obget(url);
    }

    @CheckResult
    public Observable<String> Obpost(String url) {
        return builder.Obpost(url);
    }

    @CheckResult
    public Observable<String> Obput(String url) {
        return builder.Obput(url);
    }

    /**
     * 取消当前页面正在请求的请求
     *
     * @param activity
     */
    public void cancelActivityRequest(String activity) {
        builder.cancelActivityRequest(activity);
    }

    @Override
    public void clearOnApplicationQuit() {
        builder = null;
    }
}