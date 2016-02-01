package com.artdna.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * <p>Title:      SoftInputUtils
 * <p>Description:        软键盘工具类
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2016
 * <p>Company: @小鱼网
 * <p>Create Time: 16/1/7 15:44
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public class SoftInputUtils {

	/**
	 *  打开软键盘 默认为false
	 * @param ctx
	 * @param etx
	 * @param sheepTimeShort：true：100。 false:500
	 */
	public static void openInput(final Context ctx, EditText etx) {
		openInput(ctx, etx, true);
	}

	/**
	 *  打开软键盘
	 * @param ctx
	 * @param etx
	 * @param sheepTimeShort：true：100。 false:500
	 */

	public static void openInput(final Context ctx, final EditText etx, boolean sheepTimeShort) {
		if (etx == null) {
			return;
		}
		etx.setFocusable(true);
		etx.setFocusableInTouchMode(true);
		etx.requestFocus();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(etx, InputMethodManager.SHOW_IMPLICIT);
			}
		}, (sheepTimeShort == true) ? 100 : 500);
	}

	// 关闭软键盘
	public static void closeInput(Context ctx, EditText editText) {
		if (editText == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		editText.setHint("");
	}

	public static boolean isOpenInput(Context ctx, EditText editText) {
		if (editText == null) {
			return false;
		}
		InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive(editText);
	}

	/**
	 * 隐藏软件输入法
	 * @param Activity activity  
	 */
	public static void closeSoftInput(Activity activity) {
		if (activity == null) {
			return;
		}
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

}
