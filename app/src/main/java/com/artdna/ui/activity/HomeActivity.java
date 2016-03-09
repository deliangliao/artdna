package com.artdna.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.artdna.R;
import com.artdna.ui.base.BaseArtActivity;
import com.artdna.utils.TopUtil;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import butterknife.Bind;
import butterknife.OnClick;

public class HomeActivity extends BaseArtActivity {

    @Bind(R.id.roll_view_pager)
    RollPagerView mRollViewPager;

    @Nullable
    @OnClick(R.id.ic_dna)
    public void toDnaInfo() {
        startActivity(new Intent(mActivity, DnaInfoActivity.class));
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_home;
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
//        mRollViewPager.setHintView(new ColorPointHintView(mContext, Color.GRAY, Color.WHITE));
        mRollViewPager.setHintView(null);//隐藏指示器
        mRollViewPager.setAdapter(new TestLoopAdapter(mRollViewPager));
    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {
                R.drawable.dna_banner,
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
        return "艺管家";
    }

    @OnClick({R.id.top_right, R.id.ic_dsjgl, R.id.ic_xxjy, R.id.ic_xsjy, R.id.ic_pm, R.id.ic_zydk, R.id.ic_jrfw, R.id.ic_jdpg, R.id.ic_pxjl})
    public void toastMsg() {
        toast("让艺术品都流通起来");
    }

}
