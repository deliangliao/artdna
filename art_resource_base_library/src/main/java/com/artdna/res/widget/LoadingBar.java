package com.artdna.res.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artdna.res.R;
import com.artdna.res.widget.jumpingbeans.JumpingBeans;

/**
 * <p>Title: 自定义加载进度条效果                                 </p>
 * <p>Description:                     </p>
 * <p>@author: liaodl                  </p>
 * <p>Copyright: Copyright (c) 2014    </p>
 * <p>Company:       @小鱼网            </p>
 * <p>Create Time: 2014-11-02          </p>
 * <p>@author:                         </p> 
 * <p>Update Time:                     </p>
 * <p>Updater:                         </p>
 * <p>Update Comments:                 </p>
 */
public class LoadingBar extends LinearLayout {

    private ProgressWheel progressWheel;
	private TextView textView;
	private TextView dots;
	private JumpingBeans jumpingBeans;
	private Context mContext;
	private OnClickListener listener;

	public LoadingBar(Context context) {
		this(context, null);
	}

	public LoadingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.loading_bar_layout, this, true);
		setGravity(Gravity.CENTER);

		progressWheel = (ProgressWheel) this.findViewById(R.id.progressWheel);
		progressWheel.spin();

		textView = (TextView) findViewById(R.id.loading_bar_tip);
		textView.setText(context.getString(R.string.common_loading_no_dots));
//		jumpingBeans = new JumpingBeans.Builder().makeTextJump(textView, 0, textView.getText().length()).build();

		dots = (TextView) findViewById(R.id.loading_bar_dots);
		dots.setVisibility(View.VISIBLE);
		jumpingBeans = new JumpingBeans.Builder().appendJumpingDots(dots).build();
	}

	public void hide() {
		this.setVisibility(View.GONE);
        progressWheel.stopSpinning();
		jumpingBeans.stopJumping();
	}

	public void show() {
		this.setVisibility(View.VISIBLE);
		showLoadingPic();
		showLoadingTip();
		textView.setText(mContext.getString(R.string.common_loading_no_dots));
		dots.setVisibility(View.VISIBLE);
		this.setOnClickListener(null);
	}

	public void showFailLayout() {
		showFailLayout("", listener);
	}

	public void showFailLayout(String msg) {
		showFailLayout(msg, listener);
	}

	public void showFailLayout(String msg, OnClickListener onlickListener) {
		this.setVisibility(View.VISIBLE);
		hideLoadingPic();
		setTextColor(Color.BLACK);
		dots.setVisibility(View.GONE);
		if (!TextUtils.isEmpty(msg)) {
			setMessage(msg);
		} else {
			setMessage(mContext.getString(R.string.common_loading_fail_no_dots));
		}
		this.setOnClickListener(onlickListener);
	}

	public void setFailLayoutOnClick(OnClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 自定义提示信息
	 * @param messageStr
	 */
	public void setMessage(String messageStr) {
		textView.setText(messageStr);
	}

	/**
	 * 自定义字体颜色
	 * @param colors
	 */
	public void setTextColor(ColorStateList colors) {
		textView.setTextColor(colors);
	}

	/**
	 * 自定义字体颜色
	 * @param colors
	 */
	public void setTextColor(int color) {
		textView.setTextColor(color);
	}

	/**
	 * 自定义字体大小
	 * @param colors
	 */
	public void setTextSize(float size) {
		textView.setTextSize(size);
	}

	/**
	 * 隐藏旋转图片
	 */
	public void hideLoadingPic() {
        progressWheel.stopSpinning();
        progressWheel.setVisibility(View.GONE);
	}

	/**
	 * 显示旋转图片
	 */
	public void showLoadingPic() {
        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();
	}

	/**
	 * 隐藏加载提示信息文字
	 */
	public void hideLoadingTip() {
		textView.setVisibility(View.GONE);
	}

	/**
	 * 显示加载提示信息文字
	 */
	public void showLoadingTip() {
		textView.setVisibility(View.VISIBLE);
	}


}
