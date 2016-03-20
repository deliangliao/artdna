package com.artdna.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.artdna.R;
import com.artdna.config.ArtKey;
import com.artdna.ui.base.BaseToolbarActivity;
import com.artdna.utils.AndroidUtils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.shengshi.base.tools.AppHelper;

import butterknife.Bind;

public class WebActivity extends BaseToolbarActivity {

    @Bind(R.id.progressbar)
    NumberProgressBar mProgressbar;
    @Bind(R.id.webView)
    WebView mWebView;
    @Bind(R.id.tv_title)
    TextSwitcher mTextSwitcher;

    String mUrl, mTitle;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initData() {
        mUrl = getIntent().getStringExtra(ArtKey.KEY_INTENT_WEB_URL);
        mTitle = getIntent().getStringExtra(ArtKey.KEY_INTENT_WEB_TITLE);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        String user_agent = settings.getUserAgentString();
        String bookUA = user_agent + "; artdna-android/" + AppHelper.getVersionName(mContext);
        settings.setUserAgentString(bookUA);
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new BookWebViewClient());
        mWebView.loadUrl(mUrl);

//        mTextSwitcher.setFactory(() -> {
//            TextView textView = new TextView(this);
//            textView.setTextAppearance(this, R.style.WebTitle);
//            textView.setSingleLine(true);
//            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//            textView.postDelayed(() -> textView.setSelected(true), 1738);
//            return textView;
//        });

        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                final TextView textView = new TextView(mContext);
                textView.setTextAppearance(mContext, R.style.WebTitle);
                textView.setSingleLine(true);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setSelected(true);
                    }
                }, 1738);
                return textView;
            }
        });
        mTextSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mTextSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        if (mTitle != null) setTitle(mTitle);
    }

    @Override
    public String getTopTitle() {
        return null;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTextSwitcher.setText(title);
    }

    private void refresh() {
        mWebView.reload();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_copy_url:
                String copyDone = getString(R.string.toast_copy_done);
                AndroidUtils.copyToClipBoard(this, mWebView.getUrl(), copyDone);
                return true;
            case R.id.action_open_url:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(mUrl);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    toast(R.string.toast_open_fail);
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) mWebView.destroy();
    }

    @Override
    protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
    }

    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressbar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressbar.setVisibility(View.GONE);
            } else {
                mProgressbar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    private class BookWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }
    }
}
