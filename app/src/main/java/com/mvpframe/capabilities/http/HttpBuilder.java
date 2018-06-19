package com.mvpframe.capabilities.http;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mvpframe.bean.BaseResp;
import com.mvpframe.bridge.BridgeFactory;
import com.mvpframe.bridge.Bridges;
import com.mvpframe.bridge.localstorage.LocalFileStorageManager;
import com.mvpframe.capabilities.http.interfaces.Error;
import com.mvpframe.capabilities.http.interfaces.OnCompleted;
import com.mvpframe.capabilities.http.interfaces.Progress;
import com.mvpframe.capabilities.http.interfaces.Success;
import com.mvpframe.capabilities.http.utils.NetUtils;
import com.mvpframe.capabilities.http.utils.WriteFileUtil;
import com.mvpframe.capabilities.json.GsonHelper;
import com.mvpframe.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mvpframe.capabilities.http.HttpUtil.checkHeaders;
import static com.mvpframe.capabilities.http.HttpUtil.checkParams;
import static com.mvpframe.capabilities.http.HttpUtil.putCall;
import static com.mvpframe.constant.Constants.DOWNLOAD;
import static com.mvpframe.constant.Constants.DOWNLOAD_URL;
import static com.mvpframe.constant.Constants.ERROR_MESSAGE;
import static com.mvpframe.constant.Constants.FAILED_CODE_END;
import static com.mvpframe.constant.Constants.NETWORK_ERROR;
import static com.mvpframe.constant.Constants.SUCESS_CODE_END;

/**
 * CJY
 */

public class HttpBuilder<T> {

    Handler handler = new Handler() {};
    Map<String, String> params = new HashMap<>();
    Map<String, String> headers = new HashMap<>();
    String path;
    Error mErrorCallBack;
    Success mSuccessCallBack;
    OnCompleted mOnCompletedCallBack;
    Progress mProgressListener;
    Object tag;
    Context mContext;
    Class<T> clazz;
    boolean checkNetConnected = false;

    public HttpBuilder() {
        this.setParams();
    }

    /**
     * 是否允许缓存，传入时间如：1*3600 代表一小时缓存时效
     *
     * @param time 缓存时间 单位：秒
     */
    public HttpBuilder cacheTime(int time) {
        header("Cache-Time", time + "");
        return this;
    }

    public HttpBuilder path(@NonNull String path) {
        this.path = path;
        return this;
    }

    public HttpBuilder tag(@NonNull Object tag) {
        this.tag = tag;
        return this;
    }

    public HttpBuilder params(@NonNull Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    public HttpBuilder params(@NonNull String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public HttpBuilder headers(@NonNull Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public HttpBuilder header(@NonNull String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpBuilder success(@NonNull Success success) {
        this.mSuccessCallBack = success;
        return this;
    }
    public HttpBuilder onCompleted(@NonNull OnCompleted onCompleted) {
        this.mOnCompletedCallBack = onCompleted;
        return this;
    }
    public HttpBuilder progress(@NonNull Progress progress) {
        this.mProgressListener = progress;
        return this;
    }

    public HttpBuilder error(@NonNull Error error) {
        this.mErrorCallBack = error;
        return this;
    }

    public HttpBuilder setClass(@NonNull Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }


    public void setParams() {
        if (HttpUtil.getmInstance() == null) {
            throw new NullPointerException("HttpUtil has not be initialized");
        }
        this.params = new HashMap<>();
        this.mErrorCallBack = (v) -> { };
        this.mSuccessCallBack = (s) -> { };
        this.mOnCompletedCallBack = () -> { };
        this.mProgressListener = new Progress() {
            @Override
            public void progress(float p) {
            }
        };
    }

    @CheckResult
    private String checkUrl(String url) {
        if (HttpUtil.checkNULL(url)) {
            throw new NullPointerException("absolute url can not be empty");
        }
        return url;
    }

    @CheckResult
    private String checkPath(String path) {
        if (HttpUtil.checkNULL(path) && mContext != null) {
            LocalFileStorageManager manager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE);
            return manager.getDownloadPath(mContext);
        }
        return path;
    }

    @CheckResult
    public String message(String mes) {
        if (HttpUtil.checkNULL(mes)) {
            mes = "服务器异常，请稍后再试";
        }

        if (mes.equals("timeout") || mes.equals("SSL handshake timed out")) {
            return "网络请求超时";
        } else {
            return mes;
        }

    }
    /**
     * 请求前初始检查网络是否连接，未连接跳转到网络设置界面
     */
    public HttpBuilder isConnected(@NonNull Context context) {
        checkNetConnected = true;
        mContext = context;
        return this;
    }
    boolean allready() {
        if (!checkNetConnected || mContext == null) {
            return true;
        }
        if (!NetUtils.isConnected(mContext)) {
            ToastUtil.makeTextShort(mContext, "检测到网络已关闭，请先打开网络");
            NetUtils.openSetting(mContext);//跳转到网络设置界面
            return false;
        }
        return true;
    }

    public void get(String url) {
        if (!allready()) {
            return;
        }
        Call call = HttpUtil.getService().get(checkUrl(url), checkParams(params), checkHeaders(headers));
        putCall(tag, url, call);
        call.enqueue(new TRequestCallBack(clazz, url));
    }

    public void post(String url) {
        if (!allready()) {
            return;
        }
        Call call = HttpUtil.getService().post(checkUrl(url), checkParams(params), checkHeaders(headers));
        putCall(tag, url, call);
        call.enqueue(new TRequestCallBack(clazz, url));
    }

    public Observable<ResponseBody> Obdownload(String url) {
        String curl = checkUrl(url);
        this.params = checkParams(this.params);
        this.headers.put(DOWNLOAD, DOWNLOAD);
        this.headers.put(DOWNLOAD_URL, curl);
        return HttpUtil.getService().Obdownload(checkHeaders(headers), curl, checkParams(params));
    }

    //下载
    public void download(String url) {
        String curl = checkUrl(url);
        this.params = checkParams(this.params);
        this.headers.put(DOWNLOAD, DOWNLOAD);
        this.headers.put(DOWNLOAD_URL, curl);
        Call call = HttpUtil.getService().download(checkHeaders(headers), curl, checkParams(params));
        putCall(tag, curl, call);

        Observable<ResponseBody> observable = Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<ResponseBody> e) throws Exception {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        e.onNext(response.body());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mErrorCallBack.error(t.getMessage());
                    }
                });
            }
        });

        observable.observeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull ResponseBody responseBody) {
                        WriteFileUtil.writeFile(responseBody, checkPath(path), mProgressListener, mSuccessCallBack, mErrorCallBack);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        mErrorCallBack.error(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    @CheckResult
    public Observable<String> Obget(String url) {
        return HttpUtil.getService().Obget(checkUrl(url), checkParams(params), checkHeaders(headers));
    }

    @CheckResult
    public Observable<String> Obpost(String url) {
        return HttpUtil.getService().Obpost(checkUrl(url), checkParams(params), checkHeaders(headers));
    }

    @CheckResult
    public Observable<String> Obput(String url) {
        return HttpUtil.getService().Obput(checkUrl(url), checkParams(params), checkHeaders(headers));
    }

    /*************************************************************
     * 回调方法
     *********************************************************/
    class TRequestCallBack<T> implements Callback {

        private Class<T> clazz;
        private String url;

        public TRequestCallBack(Class<T> clazz, String url) {
            this.clazz = clazz;
            this.url = url;
        }

        @Override
        public void onResponse(Call call, Response response) {
            if (response.isSuccessful()) {
                String result = (String) response.body(); //方法只能调用一次
                Log.e((String) tag, result);
                final T res = GsonHelper.toType(result, clazz);
                String code = "";
                if (res != null && res instanceof BaseResp) {
                    code = ((BaseResp) res).getLoginState();
                    switch (code) {
                        case SUCESS_CODE_END:
                            postSucessMsg(res);
                            break;
                        case FAILED_CODE_END:
                        default:
                            postErrorMsg(((BaseResp) res).getLoginState());
                            break;
                    }
                } else {
                    postErrorMsg(ERROR_MESSAGE);
                }
            } else {
                postErrorMsg(NETWORK_ERROR);
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            postErrorMsg(t.getMessage());
        }

        /**
         * 主线程发送错误消息
         */
        private void postErrorMsg(String notifyMsg) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mOnCompletedCallBack.onCompleted();
                    mErrorCallBack.error(message(notifyMsg));
                    if (tag != null)
                        HttpUtil.removeCall(url);
                }
            });
        }

        /**
         * 主线程发送正确消息
         */
        private void postSucessMsg(final T res) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mOnCompletedCallBack.onCompleted();
                    mSuccessCallBack.success(res);
                    if (tag != null)
                        HttpUtil.removeCall(url);
                }
            });
        }
    }


    /**
     * 取消当前页面正在请求的请求
     *
     * @param activityName
     */
    public void cancelActivityRequest(String activityName) {
        HttpUtil.cancel(activityName);
    }
}