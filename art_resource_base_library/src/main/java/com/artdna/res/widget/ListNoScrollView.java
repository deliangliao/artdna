package com.artdna.res.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * <p>Title:      ListNoScrollView
 * <p>Description:
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2016
 * <p>Company: @artdna
 * <p>Create Time: 16/2/1 10:35
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public class ListNoScrollView extends ListView {

	public ListNoScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListNoScrollView(Context context) {
		super(context);
	}

	public ListNoScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
