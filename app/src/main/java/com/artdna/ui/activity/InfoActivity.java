package com.artdna.ui.activity;

import com.artdna.R;
import com.artdna.ui.base.BaseArtActivity;

public class InfoActivity extends BaseArtActivity {

    @Override
    public String getTopTitle() {
        return "资讯";
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_info;
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
