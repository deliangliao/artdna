package com.artdna.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.artdna.R;
import com.artdna.ui.base.BaseArtActivity;
import com.artdna.utils.TopUtil;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseArtActivity {

    @Bind(R.id.roll_view_pager)
    RollPagerView mRollViewPager;

    @Nullable
    @OnClick(R.id.ic_dna)
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
        setReturnBtnEnable(false);
        TopUtil.updateRight(mActivity, R.id.top_right, R.drawable.ic_msg);
    }

    @Override
    protected void initData() {
        mRollViewPager.setHintView(new ColorPointHintView(mContext, Color.GRAY, Color.WHITE));
        mRollViewPager.setAdapter(new TestLoopAdapter(mRollViewPager));
    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {
                R.drawable.banner,
                R.drawable.banner,
                R.drawable.banner,
                R.drawable.banner
        };

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }

    }

    @Override
    public String getTopTitle() {
        return "首页";
    }
}
