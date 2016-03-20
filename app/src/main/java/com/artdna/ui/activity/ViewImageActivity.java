package com.artdna.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.artdna.R;
import com.artdna.adapter.ViewImageAdapter;
import com.artdna.res.widget.ViewPagerFixed;
import com.artdna.ui.base.BaseArtActivity;
import com.shengshi.base.tools.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:        ViewImageActivity查看大图
 * <p>Description:
 * 如何使用：
 * <p>Intent intent = new Intent(mContext, ViewImageActivity.class);
 * <p>Bundle bundle = new Bundle();
 * <p>bundle.putStringArray("urls", list.toArray(new String[]{}));//传路径数组，支持url和本地path
 * <p>bundle.putInt("index", position);// 当前查看的图片的顺序位置
 * <p><strong>以下2个参数非必须，多图发帖等查看大图 可供选择功能 用</strong>
 * <p>bundle.putSerializable("ViewType", ViewType.Select);
 * <p>bundle.putStringArrayList("have_select_photos_list", have_select_photos_list);
 * <p>intent.putExtras(bundle);
 * <p>startActivity(intent);
 * <p/>
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2014
 * <p>Company:
 * <p>Create Time: 2014-9-24
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:
 * <p>Update Comments:
 */
public class ViewImageActivity extends BaseArtActivity implements OnClickListener {

    private ViewPagerFixed mViewPager;
    private ViewImageAdapter adapter;
    private int curPos;
    private List<String> mUrlsdata;
    private TextView page;

    private Button choiceCompleteBtn;

    private TextView mCheckBox;
    private boolean isChecked;

    @Override
    protected void initComponents() {
        super.initComponents();
        setSwipeBackEnable(false);
        // 隐藏Activity刚进来焦点在EditText时的键盘显示
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mViewPager = (ViewPagerFixed) findViewById(R.id.viewimagePager);
        page = (TextView) findViewById(R.id.head_center_title);
        findViewById(R.id.head_button_left_img).setOnClickListener(this);
        findViewById(R.id.head_button_right).setOnClickListener(this);
    }

    // 初始化控件数据
    @Override
    protected void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        final String[] mUrls = bundle.getStringArray("urls");
        curPos = bundle.getInt("index", 0);
        mUrlsdata = new ArrayList<String>();
        for (int i = 0; i < mUrls.length; i++) {
            mUrlsdata.add(mUrls[i]);
        }
        page.setText((curPos + 1) + "/" + mUrls.length);
        adapter = new ViewImageAdapter(mActivity, curPos, mUrlsdata);
        mViewPager.setAdapter(adapter);
        // 定位点击传送来的position
        mViewPager.setCurrentItem(curPos);
        adapter.setCurrPos(curPos);
        adapter.setListener(new ViewImageAdapter.OnSaveSuccessListener() {

            @Override
            public void onSuccess() {
                ToastUtils.showToast(mActivity, "保存成功", Toast.LENGTH_SHORT);
            }
        });
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int postion) {
                if (postion > mUrls.length) {
                    postion = (mUrls.length - 1);
                }
                curPos = postion;
                page.setText((postion + 1) + "/" + mUrls.length);
                adapter.setCurrPos(postion);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {// 从1到2滑动，在1滑动前调用
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {// 状态有三个0空闲，1是增在滑行中，2目标加载完毕
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.head_button_left_img) {
            onBackPressed();
        } else if (id == R.id.head_button_right) {
            if (adapter != null) {
                adapter.saveImgInThread();
            }
        }
    }

    @Override
    public String getTopTitle() {
        return null;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_viewimage;
    }

}
