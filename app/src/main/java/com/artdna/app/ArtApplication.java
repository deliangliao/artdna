package com.artdna.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import com.artdna.BuildConfig;
import com.artdna.utils.CommonUtils;
import com.shengshi.base.app.BaseApplication;
import com.shengshi.base.tools.AppHelper;
import com.shengshi.base.tools.Log;
import com.shengshi.base.tools.logger.LogLevel;
import com.umeng.analytics.MobclickAgent;

public class ArtApplication extends BaseApplication implements Application.ActivityLifecycleCallbacks {

    public static final long CHECK_DELAY = 800;
    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private Runnable check;

    @Override
    public void onCreate() {
        super.onCreate();
        initConfig();
    }

    public void initConfig() {
        if (!AppHelper.getCurProcessName(mContext).equals(AppHelper.getPackageName(mContext))) {
            return;
        }
        this.registerActivityLifecycleCallbacks(this);
        //全局调试模式开关 ， 在appconfig.xml里配置
        boolean globalDebugMode = CommonUtils.isDebug(mContext);
        // 在debug下，才显示log
        Log.init("art").setLogLevel(globalDebugMode & BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
        //打开umeng调试模式后，数据实时发送
        MobclickAgent.setDebugMode(globalDebugMode);
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        foreground = true;
        if (check != null) {
            handler.removeCallbacks(check);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;
        if (check != null) {
            handler.removeCallbacks(check);
        }
        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    Log.d("App went background");
                } else {
                    Log.d("App still foreground");
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
