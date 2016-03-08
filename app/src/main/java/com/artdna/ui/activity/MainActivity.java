package com.artdna.ui.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.artdna.R;
import com.artdna.utils.CommonUtils;

public class MainActivity extends TabActivity {

    public static TabHost mTabHost;

    // 首页 社区 生活 我的
    String TAB_HOME = "homepage";
    String TAB_COMMUNITY = "community";
    String TAB_LIFE = "life";
    String TAB_MINE = "mine";
    public static int CLICK_TAB = 0; //发送广播页
    View[] view = new View[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void newTabSpec() {
        view[0] = createTabView(getApplicationContext(), R.drawable.main_tabbg_home,
                getString(R.string.tab_home));
        TabSpec tab0 = mTabHost.newTabSpec(TAB_HOME).setIndicator(view[0])
                .setContent(new Intent(this, HomeActivity.class));

        view[1] = createTabView(getApplicationContext(), R.drawable.main_tabbg_members,
                getString(R.string.tab_members));
        TabSpec tab1 = mTabHost.newTabSpec(TAB_COMMUNITY).setIndicator(view[1])
                .setContent(new Intent(this, MembersActivity.class));

        view[2] = createTabView(getApplicationContext(), R.drawable.main_tabbg_info,
                getString(R.string.tab_info));
        TabSpec tab2 = mTabHost.newTabSpec(TAB_LIFE).setIndicator(view[2])
                .setContent(new Intent(this, InfoActivity.class));

        view[3] = createTabView(getApplicationContext(), R.drawable.main_tabbg_setting, getString(R.string.tab_setting));
        TabSpec tab3 = mTabHost.newTabSpec(TAB_MINE).setIndicator(view[3])
                .setContent(new Intent(this, SettingsActivity.class));

        mTabHost.addTab(tab0);
        mTabHost.addTab(tab1);
        mTabHost.addTab(tab2);
        mTabHost.addTab(tab3);

        view[0].setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CLICK_TAB = 0;
                mTabHost.setCurrentTab(0);
            }
        });

        view[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CLICK_TAB = 1;
                mTabHost.setCurrentTab(1);
            }
        });

        view[2].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CLICK_TAB = 2;
                mTabHost.setCurrentTab(2);
            }
        });

        view[3].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CLICK_TAB = 3;
                mTabHost.setCurrentTab(3);
            }
        });

    }

    /**
     * 初始化界面
     */
    private void initView() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        newTabSpec();
        mTabHost.setCurrentTab(CLICK_TAB);
    }

    private View createTabView(Context context, int imageResource, String textRes) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_tab_item, null);
        Drawable drawable = ContextCompat.getDrawable(context, imageResource);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        TextView tabText = (TextView) view.findViewById(R.id.tab);
        tabText.setCompoundDrawables(null, drawable, null, null);
        tabText.setText(textRes);
        return view;
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次后退键退出app", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                CommonUtils.doExit(getApplicationContext());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
