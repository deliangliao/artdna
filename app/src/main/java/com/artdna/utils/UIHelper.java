package com.artdna.utils;

import android.app.Activity;

import com.artdna.res.widget.CustomProgressDialog;


/**
 * <p>Title:      UIHelper
 * <p>Description:
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2016
 * <p>Company: @artdna
 * <p>Create Time: 16/2/1 11:29
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public class UIHelper {

    /**
     * 设置隐藏模态进度条
     */
    public static CustomProgressDialog customProgressDialog(Activity activity, String message) {
        CustomProgressDialog progressDialog = CustomProgressDialog.getDialog(activity);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

}
