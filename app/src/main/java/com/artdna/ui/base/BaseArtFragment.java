package com.artdna.ui.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.artdna.res.widget.CustomProgressDialog;
import com.artdna.utils.ImageLoader;
import com.shengshi.base.ui.BaseFragment;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * <p>Title:      BaseArtFragment
 * <p>Description:          项目用到的Fragment必须继承此类
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2016
 * <p>Company: @artdna
 * <p>Create Time: 16/2/1 13:05
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public abstract class BaseArtFragment extends BaseFragment {

	public CustomProgressDialog loadingDialog;
	public ImageLoader imageLoader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		imageLoader = ImageLoader.getInstance(mContext);//保证优先初始化
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initComponents(View view) {
		ButterKnife.bind(this, view);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (Exception e) {
		}
	}

	public Drawable getDrawableById(int drawableId) {
		if (drawableId < 0) {
			return null;
		}
		return ContextCompat.getDrawable(mContext, drawableId);
	}

	public int getColorById(int colorId) {
		if (colorId < 0) {
			return 0;
		}
		return ContextCompat.getColor(mContext, colorId);
	}

	/**
	 * 显示加载对话框
	 */
	public void showTipDialog() {
		showTipDialog("");
	}

	/**
	 * 显示自定义提示信息的对话框
	 * @param msg
	 */
	public void showTipDialog(String msg) {
		loadingDialog = CustomProgressDialog.getDialog(mActivity);
		if (loadingDialog != null) {
			loadingDialog.setCanceledOnTouchOutside(false);
			if (!TextUtils.isEmpty(msg)) {
				loadingDialog.setMessage(msg);
			}
			if (!getActivity().isFinishing() && !loadingDialog.isShowing()) {
				loadingDialog.show();
			}
		}
	}

	/**
	 * 隐藏加载对话框
	 */
	public void hideTipDialog() {
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
