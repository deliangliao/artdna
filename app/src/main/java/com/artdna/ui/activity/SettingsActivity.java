package com.artdna.ui.activity;

import com.artdna.R;
import com.artdna.ui.base.BaseArtActivity;

public class SettingsActivity extends BaseArtActivity {

    @Override
    public String getTopTitle() {
        return "设置";
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        setSwipeBackEnable(false);
        setReturnBtnEnable(false);
    }

    @Override
    protected void initData() {

    }
}
