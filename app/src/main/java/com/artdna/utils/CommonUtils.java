package com.artdna.utils;

import android.content.Context;
import android.content.Intent;

import com.artdna.R;
import com.shengshi.base.tools.AppManager;
import com.shengshi.base.tools.SharedPreferencesUtil;

/**
 * <p>Title:     CommonUtils一些常用工具类
 * <p>Description: 跟业务比较相关且常用，包括：
 * <p>1.设置全局debug模式
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2016
 * <p>Company: @artdna
 * <p>Create Time: 16/2/1 11:26
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public class CommonUtils {

    private static final String ART_PREFERENT_NAME = "com_art_preferent";

    private CommonUtils() {
    }

    public static boolean isDebug(Context context) {
        String compare = context.getString(R.string.is_debug);
        if ("true".equalsIgnoreCase(compare)) {
            return true;
        }
        return false;
    }

    public static void saveClientId(Context context, String cid) {
        SharedPreferencesUtil.setValue(context, "client_id", cid);
    }

    public static String getClientId(Context context) {
        return SharedPreferencesUtil.getValue(context, "client_id");
    }

    //应用重启
    public static void restartApp(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        doExit(context);
    }

    public static void doExit(Context context) {
        CommonUtils.saveClientId(context, "");
//        AccountUtil.removeAccountInfo(context);//要清空
        AppManager.getAppManager().finishAllActivity();//退出
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
