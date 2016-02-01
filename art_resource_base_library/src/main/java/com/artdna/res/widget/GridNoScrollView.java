package com.artdna.res.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * <p>Title:      GridNoScrollView
 * <p>Description:
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2016
 * <p>Company: @artdna
 * <p>Create Time: 16/2/1 10:36
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public class GridNoScrollView extends GridView {

	public GridNoScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridNoScrollView(Context context) {
		super(context);
	}

	public GridNoScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
