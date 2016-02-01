package com.artdna.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.artdna.R;
import com.artdna.ui.base.BaseArtActivity;

import butterknife.OnClick;

public class MainActivity extends BaseArtActivity {

    @Nullable
	@OnClick(R.id.artid)
	public void toDnaInfo() {
		startActivity(new Intent(mActivity, DnaInfoActivity.class));
	}

	@Override
	protected int getMainContentViewId() {
		return R.layout.activity_main;
	}

    @Override
    protected void initComponents() {
        super.initComponents();
        setSwipeBackEnable(false);
    }

    @Override
	protected void initData() {

	}

	@Override
	public String getTopTitle() {
		return "测试ArtId";
	}
}
