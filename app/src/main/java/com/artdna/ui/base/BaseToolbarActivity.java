package com.artdna.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;

import com.artdna.R;

public abstract class BaseToolbarActivity extends BaseArtActivity {

    public void onToolbarClick() {}

    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;
    protected boolean mIsHidden = false;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.btn_back_selector);

        if (mToolbar == null || mAppBar == null) {
            throw new IllegalStateException("No toolbar");
        }

//        mToolbar.setOnClickListener(v -> onToolbarClick());

        setSupportActionBar(mToolbar);

        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            mAppBar.setElevation(10.6f);
        }
    }

    public boolean canBack() {
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected void setAppBarAlpha(float alpha) {
        mAppBar.setAlpha(alpha);
    }

    protected void hideOrShowToolbar() {
        mAppBar.animate()
            .translationY(mIsHidden ? 0 : -mAppBar.getHeight())
            .setInterpolator(new DecelerateInterpolator(2))
            .start();

        mIsHidden = !mIsHidden;
    }
}
