package com.mvpframe.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.mvpframe.bridge.BridgeFactory;
import com.mvpframe.bridge.BridgeLifeCycleSetKeeper;
import com.mvpframe.bridge.Bridges;
import com.mvpframe.bridge.localstorage.LocalFileStorageManager;
import com.mvpframe.capabilities.http.HttpUtil;
import com.mvpframe.capabilities.http.interfaces.HeadersInterceptor;
import com.mvpframe.capabilities.http.interfaces.ParamsInterceptor;
import com.mvpframe.constant.URLUtil;
import com.mvpframe.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mvpframe.constant.Constants.URL_TIME;

/**
 * <应用初始化> <功能详细描述>
 */
public class MApplication extends Application {
    /**
     * app实例
     */
    public static MApplication ebApplication = null;

    /**
     * 本地activity栈
     */
    public static List<Activity> activitys = new ArrayList<Activity>();

    /**
     * 当前avtivity名称
     */
    public static String currentActivityName = "";

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ebApplication = this;
        new HttpUtil.SingletonBuilder(getApplicationContext(), URLUtil.SERVER).build();
        BridgeFactory.init(this);
        BridgeLifeCycleSetKeeper.getInstance().initOnApplicationCreate(getApplicationContext());
        LocalFileStorageManager manager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE);
        new GiphyGlideModule(manager);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        onDestory();
    }

    /**
     * 退出应用，清理内存
     */
    private void onDestory() {
        BridgeLifeCycleSetKeeper.getInstance().clearOnApplicationQuit();
        ToastUtil.destory();
    }


    /**
     * <添加> <功能详细描述>
     *
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void addActivity(Activity activity) {
        activitys.add(activity);
    }

    /**
     * <删除>
     * <功能详细描述>
     *
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void deleteActivity(Activity activity) {
        if (activity != null) {
            activitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public class GiphyGlideModule implements GlideModule {

        LocalFileStorageManager manager;

        public GiphyGlideModule(LocalFileStorageManager manager) {
            this.manager = manager;
        }

        @Override
        public void applyOptions(Context context, GlideBuilder builder) {
            builder.setDiskCache(new DiskCache.Factory() {
                @Override
                public DiskCache build() {
                    return DiskLruCacheWrapper.get(new File(manager.getCacheImgFilePath(ebApplication)), 200);
                }
            });
        }

        @Override
        public void registerComponents(Context context, Glide glide) {

        }
    }
}
