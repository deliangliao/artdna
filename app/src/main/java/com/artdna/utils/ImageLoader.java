package com.artdna.utils;

import android.content.Context;

import com.artdna.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.shengshi.base.image.BaseImageLoader;

/**
 * <p>Title:      ImageLoader 图片显示工具
 * <p>Description:           项目都用此工具加载图片，如需扩展，就继承或者在此添加
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2016
 * <p>Company: @artdna
 * <p>Create Time: 16/2/1 11:28
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public class ImageLoader extends BaseImageLoader {

	public DisplayImageOptions.Builder builder;

	public static ImageLoader getInstance(Context context) {
		return new ImageLoader(context);
	}

	private ImageLoader(Context context) {
		super(context);
		builder = getDefaultDisplayOptionsBuilder();
	}

	@Override
	public int getDefaultImageOnLoading() {
		return R.color.pic_default_bg_color;
	}

	@Override
	public int getDefaultImageOnLoadFail() {
		return R.color.pic_default_bg_color;//R.drawable.pic_default_medium;
	}

	/**
	 * 改变默认正在加载图片
	 * 
	 * @param loadingImg
	 * @return
	 */
	public DisplayImageOptions.Builder setImageOnLoading(int loadingImg) {
		builder.showImageOnLoading(loadingImg);
		return builder;
	}

	/**
	 * 改变默认加载失败图片
	 * 
	 * @param failImg
	 * @return
	 */
	public DisplayImageOptions.Builder setImageOnLoadFail(int failImg) {
		builder.showImageOnFail(failImg);
		return builder;
	}

	@Override
	public DisplayImageOptions.Builder getUserDisplayOptionsBuilder() {
		return builder;
	}

	@Override
	public String patternUrl(String url) {
		return url;
	}

}
