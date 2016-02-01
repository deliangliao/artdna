package com.artdna.utils;

import android.content.Context;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * <p>Title:     友盟版本检测升级工具类
 * <p>Description:
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2015
 * <p>Company: @小鱼网
 * <p>Create Time: 2015-5-19
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public class UmengUpdateUtil {

	private UmengUpdateUtil() {
	}

	/**
	 * 友盟工具 - 版本检测升级
	 * @param context
	 */
	public static void OnUpdate(final Context context){
		UmengUpdateAgent.update(context); //从服务器获取更新信息
		UmengUpdateAgent.setUpdateOnlyWifi(false); //是否在只在wifi下提示更新，默认为 true
		UmengUpdateAgent.setUpdateAutoPopup(false); //是否自动弹出更新对话框
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch (updateStatus) {
					case UpdateStatus.Yes:// 有更新
						UmengUpdateAgent.showUpdateDialog(context, updateInfo);
						break;
					case UpdateStatus.No:// 无更新
						//toast("当前已是最新版.");
						break;
					case UpdateStatus.NoneWifi:// 如果设置为wifi下更新且wifi无法打开时调用
						//toast("没有wifi连接， 只在wifi下更新");
						break;
					case UpdateStatus.Timeout:// 连接超时
						//toast("连接超时，请稍候重试");
						break;
				}
			}
		});
	}
}
