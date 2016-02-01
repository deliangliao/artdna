package com.artdna.ui.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.artdna.R;
import com.artdna.res.widget.CustomProgressDialog;
import com.artdna.res.widget.LoadingBar;
import com.artdna.res.widget.swipeback.SwipeBackPagerActivity;
import com.artdna.utils.ImageLoader;
import com.artdna.utils.UIHelper;
import com.shengshi.base.tools.Log;
import com.shengshi.base.ui.BaseActionBarActivity;

import butterknife.ButterKnife;

/**
 * <p>Title:      BaseArtActivity
 * <p>Description:         Activity 基础类，通用业务可在此处理
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2016
 * <p>Company: @artdna
 * <p>Create Time: 16/2/1 13:04
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public abstract class BaseArtActivity extends SwipeBackPagerActivity implements SwipeRefreshLayout.OnRefreshListener {

    public ImageLoader imageLoader;
    public LoadingBar loadingBar;
    private TextView returnView;
    private TextView topTitleView;
    private boolean mReturnEnable = true; // 默认显示
    private OnClickListener onReturnClickListener;// 返回监听
    private CustomProgressDialog loadingDialog;

    public SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imageLoader = ImageLoader.getInstance(getApplicationContext());//保证优先初始化
        super.onCreate(savedInstanceState);
        setReturnBtnEnable();
        setTopTitle();
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        ButterKnife.bind(mActivity);
        initLoadingBar();//保证loadingBar在子类onCreate之前初始化
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.mGeneralSwipeRefreshLayout);
        if (mSwipeLayout != null) {
            mSwipeLayout.setColorSchemeResources(R.color.colorPrimary);
            mSwipeLayout.setOnRefreshListener(this);
        }
    }

    public void stopRefreshing() {
        if (mSwipeLayout != null) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    private void initLoadingBar() {
        loadingBar = generateFindViewById(R.id.mGeneralLoadingBar);
        if (loadingBar == null) {
            return;
        }
        loadingBar.setFailLayoutOnClick(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onRequestAgain();
            }
        });
    }

    /**
     * 子类重写该方法，点击屏幕，可以重写加载数据，用于加载数据失败，重试情景
     * <p>前提：
     * <p>1.引入LoadingBar布局,<include layout="@layout/widget_loading_bar_layout" />
     * <p>2.使用loadingBar.showFailLayout();显示重试视图
     */
    public void onRequestAgain() {
        loadingBar.show();
    }

    public void showLoadingBar() {
        if (loadingBar != null) {
            loadingBar.show();
        }
    }

    public void showFailLayout() {
        if (loadingBar != null) {
            loadingBar.showFailLayout();
        }
    }

    public void showFailLayout(String msg, OnClickListener onlickListener) {
        if (loadingBar != null) {
            loadingBar.showFailLayout(msg, onlickListener);
        }
    }

    public void showFailLayout(String msg) {
        if (loadingBar != null) {
            loadingBar.showFailLayout(msg);
        }
    }

    public void hideLoadingBar() {
        if (loadingBar != null) {
            loadingBar.hide();
        }
    }

    /**
     * 是否启用返回按钮
     *
     * @param enable
     */
    public void setReturnBtnEnable(boolean enable) {
        if (!enable) {
            mReturnEnable = enable;
        }
    }

    /**
     * 设置返回键
     */
    private void setReturnBtnEnable() {
        returnView = (TextView) findViewById(getIdentifier("top_btn_return", BaseActionBarActivity.IdentifierType.ID));
        if (returnView != null) {
            if (!mReturnEnable) {
                returnView.setVisibility(View.GONE);
            } else {
                returnView.setVisibility(View.VISIBLE);
                returnView.setOnClickListener(new OnReturnClickListener());
            }
        }
    }

    /**
     * 显示自定义加载框
     */
    public void showTipDialog() {
        showTipDialog("");
    }

    public void showTipDialog(String tips) {
        if (loadingDialog == null) {
            loadingDialog = UIHelper.customProgressDialog(this, tips);
            loadingDialog.show();
        } else if (!loadingDialog.isShowing()) {
            loadingDialog.setMessage(tips);
            loadingDialog.show();
        }
    }

    /**
     * 隐藏自定义加载框
     */
    public void hideTipDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 设置返回点击监听，替代原来监听
     *
     * @param listener
     */
    public void setOnReturnClickListener(OnClickListener listener) {
        onReturnClickListener = listener;
    }

    @Override
    public void onRefresh() {

    }

    private class OnReturnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (onReturnClickListener != null) {
                onReturnClickListener.onClick(v);
            } else {
                onBackPressed();
            }
        }
    }

    public void setTopTitle() {
        setTopTitle("");
    }

    /**
     * 要动态更新或重新设置标题时，子类要显式的调用该方法
     * 注：如果子类覆写了getTopTitle() 和 getTopTitleResourceId(),
     * 以getTopTitle()取得值为准，如果为空，再去取getTopTitleResourceId里的值
     */
    public void setTopTitle(String title) {
        if (topTitleView == null) {
            topTitleView = generateFindViewById(R.id.top_title);
        }
        if (topTitleView != null && topTitleView instanceof TextView) {
            topTitleView.setSelected(true);
            if (!TextUtils.isEmpty(title)) {
                topTitleView.setText(title);
            } else if (!TextUtils.isEmpty(getTopTitle())) {
                topTitleView.setText(getTopTitle());
            } else {
                if (getTopTitleResourceId() > 0) {
                    topTitleView.setText(getString(getTopTitleResourceId()));
                }
            }
        }
    }

    /**
     * 子类重写该方法，传入标题
     *
     * @return
     */
    public abstract String getTopTitle();

    /**
     * 子类重写该方法，传入标题
     *
     * @return
     */
    public int getTopTitleResourceId() {
        return 0;
    }

    @Override
    protected void protectApp() {
    }

    @Override
    protected CharSequence getPageTitleAtIndex(int position) {
        return null;
    }

    @Override
    protected void initActionBar() {
        //设置是否显示应用程序的标题
        mActionBar.setDisplayShowTitleEnabled(false);
        //设置是否显示应用程序的图标
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        //将应用程序图标设置为可点击的按钮
        mActionBar.setHomeButtonEnabled(false);
        //将应用程序图标设置为可点击的按钮,并且在图标上添加向左的箭头
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(mActivity);
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

    /***********************************
     * fragment操作
     ************************************/
    public void turnToFragment(Class<? extends BaseArtFragment> fragmentClass, String tag,
                               Bundle args, int container) {
        turnToFragment(fragmentClass, tag, args, container, false, null);
    }

    public void turnToFragment(Class<? extends BaseArtFragment> fragmentClass, String tag,
                               Bundle args, int container, boolean addToBackStack,
                               OnSetCustomAnimaitonListener listener) {
        turnToFragment(null, fragmentClass, tag, args, container, addToBackStack, listener);
    }

    public void turnToFragment(BaseArtFragment fromFragment,
                               Class<? extends BaseArtFragment> toFragmentClass, String toFragmentTag, Bundle args,
                               int container, boolean addToBackStack, OnSetCustomAnimaitonListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        BaseArtFragment toFragment = (BaseArtFragment) mFragmentManager.findFragmentByTag(toFragmentTag);
        boolean isFragmentExist = true;
        if (toFragment == null) {
            try {
                isFragmentExist = false;
                toFragment = toFragmentClass.newInstance();
                toFragment.setArguments(new Bundle());
            } catch (Exception e) {
                Log.e(e.getMessage(), e);
            }
        }
        if (args != null && !args.isEmpty()) {
            toFragment.getArguments().putAll(args);
        }
        if (listener != null) {
            listener.setCustomAnimaiton(ft);
        } else {
//			ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
//					android.R.anim.fade_in, android.R.anim.fade_out);
        }
        if (fromFragment != null) {
            if (!toFragment.isAdded()) { // 先判断是否被add过
                ft.hide(fromFragment).add(container, toFragment, toFragmentTag); // 隐藏当前的fragment，add下一个到Activity中
            } else {
//				ft.hide(fromFragment).show(toFragment); // 隐藏当前的fragment，显示下一个  
                if (isFragmentExist) {
                    ft.hide(fromFragment).detach(toFragment);
                    ft.attach(toFragment);
                }
            }
        } else {
            if (isFragmentExist) {
                ft.replace(container, toFragment);
            } else {
                ft.replace(container, toFragment, toFragmentTag);
            }
        }
        if (addToBackStack) {
            ft.addToBackStack(toFragmentTag);
        }
        ft.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }

    public interface OnSetCustomAnimaitonListener {
        public void setCustomAnimaiton(FragmentTransaction ft);
    }

    /**
     * 通过泛型来简化findViewById类型转换
     *
     * @param id
     * @return 返回view时, 加上泛型T
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T generateFindViewById(int id) {
        try {
            return (T) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(ex.getMessage(), ex);
            return null;
        }
    }

}
