package com.artdna.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.artdna.R;
import com.artdna.config.ArtKey;
import com.artdna.ui.base.BaseArtActivity;
import com.artdna.utils.TopUtil;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

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
        mRollViewPager.setHintView(new ColorPointHintView(mContext, getColorById(R.color.colorPrimary), Color.WHITE));
//        mRollViewPager.setHintView(null);//隐藏指示器
        mRollViewPager.setAdapter(new TestLoopAdapter(mRollViewPager));
    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {
                R.drawable.home_banner_1,
                R.drawable.home_banner_2,
                R.drawable.home_banner_3,
                R.drawable.home_banner_4
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

    @OnClick(R.id.top_right)
    public void toastMsg() {
        toast("让艺术品都流通起来");
    }

    @OnClick(R.id.ic_dsjgl)
    public void dodsjgl() {
        toWebActivity("http://a.eqxiu.com/s/k7wpGNwI");
    }

    @OnClick(R.id.ic_xsjy)
    public void doxsjy() {
        toWebActivity("http://e.eqxiu.com/s/nhYjIM4R");
    }

    @OnClick(R.id.ic_xxjy)
    public void doxxjy() {
        toWebActivity("http://a.eqxiu.com/s/boAUjzay");
    }

    @OnClick(R.id.ic_pm)
    public void dopm() {
        toWebActivity("http://g.eqxiu.com/s/N97Ua5sF");
    }

    @OnClick(R.id.ic_zydk)
    public void dozydk() {
        toWebActivity("http://q.eqxiu.com/s/fdXJo2hj");
    }

    @OnClick(R.id.ic_jrfw)
    public void dojrfw() {
        toWebActivity("http://b.eqxiu.com/s/upwFebEW");
    }

    @OnClick(R.id.ic_jdpg)
    public void dojdpg() {
        toWebActivity("http://g.eqxiu.com/s/EslsqEct");
    }

    @OnClick(R.id.ic_pxjl)
    public void dopxjl() {
        toWebActivity("http://q.eqxiu.com/s/e45x1Sx0");
    }

    public void toWebActivity(String url) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra(ArtKey.KEY_INTENT_WEB_URL, url);
        startActivity(intent);
    }

}
