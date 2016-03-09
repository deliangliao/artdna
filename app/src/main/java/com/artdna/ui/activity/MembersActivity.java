package com.artdna.ui.activity;

import com.artdna.R;
import com.artdna.ui.base.BaseArtActivity;

public class MembersActivity extends BaseArtActivity {

    @Override
    public String getTopTitle() {
        return "会员";
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_members;
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
