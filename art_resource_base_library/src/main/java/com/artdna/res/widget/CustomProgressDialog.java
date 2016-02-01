package com.artdna.res.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.artdna.res.R;

import java.util.HashMap;

/**
 * <p>Title:     自定义加载框
 * <p>Description:
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2014
 * <p>Company: @小鱼网
 * <p>Create Time: 2014-10-8
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public class CustomProgressDialog extends Dialog {

    private static CustomProgressDialog loadingDialog = null;

    private static HashMap<Activity, CustomProgressDialog> dialogMap = new HashMap<Activity, CustomProgressDialog>();

    private static Activity mActivity;

    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static CustomProgressDialog getDialog(Activity activity) {
        mActivity = activity;
        loadingDialog = dialogMap.get(activity);
        if (loadingDialog == null) {
            loadingDialog = new CustomProgressDialog(activity, R.style.loading_dialog);
            loadingDialog.setContentView(R.layout.loading_dialog_layout);
            loadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            dialogMap.put(activity, loadingDialog);
        }
        return loadingDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (loadingDialog == null) {
            return;
        }
        ProgressWheel progressWheel = (ProgressWheel) loadingDialog.findViewById(R.id.progressWheel);
        progressWheel.spin();
    }

    /**
     * 设置提示信息
     *
     * @param strMessage
     * @return
     */
    public CustomProgressDialog setMessage(String strMessage) {
        loadingDialog = dialogMap.get(mActivity);
        if (loadingDialog == null) {
            loadingDialog = getDialog(mActivity);
        }
        if (loadingDialog != null) {
            TextView tvMsg = (TextView) loadingDialog.findViewById(R.id.loading_text);
            if (tvMsg != null) {
                if (!TextUtils.isEmpty(strMessage)) {
                    tvMsg.setVisibility(View.VISIBLE);
                    tvMsg.setText(strMessage);
                } else {
                    tvMsg.setVisibility(View.GONE);
                }
            }
        }
        return loadingDialog;
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            loadingDialog = null;
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
            loadingDialog = null;
        } catch (Exception e) {
            loadingDialog = null;
        }
    }

    @Override
    public void cancel() {
        try {
            super.cancel();
            loadingDialog = null;
        } catch (Exception e) {
            loadingDialog = null;
        }
    }
}
