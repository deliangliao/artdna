package com.artdna.utils;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

import com.shengshi.base.tools.Log;

import java.util.List;

public class AndroidUtils {

    public static void copyToClipBoard(Context context, String text, String success) {
        ClipData clipData = ClipData.newPlainText("book_copy", text);
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
        Toast.makeText(context, success, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断App是否在前台运行，来源于网上
     * <strong>亲测无效，此类还保留此方法，方便后来者查看对比，知道此坑</strong>
     * @Deprecated
     * @param context
     * @return
     */
//    public static boolean isAppOnForeground(Context context) {
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
//        if (appProcesses == null) {
//            return false;
//        }
//        String packageName = context.getPackageName();
//        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
//            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
//                    && appProcess.processName.equals(packageName)) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 判断App是否在前台运行
     * @param context
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    //首页是否存在(启动过)
    public static boolean isExitsMainPage(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(2);
            String activityName = "";
            for (ActivityManager.RunningTaskInfo info : taskInfo) {
                ComponentName name = info.baseActivity;
                activityName = name.getClassName();
                if ("com.shengshi.book.ui.activity.MainActivity".equals(activityName)) {
                    return true;
                }
            }
        } catch (SecurityException e) {
            Log.e(e.getMessage(), e);
        }
        return false;
    }
}
