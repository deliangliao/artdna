package com.artdna.ui.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artdna.R;
import com.artdna.bean.DNADetailEntity;
import com.artdna.bean.DNAEntity;
import com.artdna.config.Urls;
import com.artdna.ui.base.BaseArtActivity;
import com.artdna.utils.NFCUtils;
import com.artdna.utils.TopUtil;
import com.shengshi.base.tools.Log;
import com.shengshi.http.net.AppException;
import com.shengshi.http.net.Request;
import com.shengshi.http.net.callback.JsonCallback;

import butterknife.Bind;
import butterknife.OnClick;

public class DnaInfoActivity extends BaseArtActivity {

    @Bind(R.id.dnaCode)
    TextView dnaCode;
    @Bind(R.id.dnaCount)
    TextView dnaCount;
    @Bind(R.id.ic_auth)
    ImageView icAuth;
    @Bind(R.id.auth_company_info_layout)
    View authCompanyTip;
    @Bind(R.id.art_detail_info_list)
    View detailList;
    @Bind(R.id.artImg)
    ImageView artImg;
    @Bind(R.id.artName)
    TextView artName;
    @Bind(R.id.artType)
    TextView artType;
    @Bind(R.id.artAge)
    TextView artAge;
    @Bind(R.id.artPrice)
    TextView artPrice;
    @Bind(R.id.artModel)
    TextView artModel;
    @Bind(R.id.artAuthor)
    TextView artAuthor;

    //==================== NFC相关参数开始 ====================================
    NfcAdapter nfcAdapter;
    PendingIntent mPendingIntent;
    IntentFilter[] mFilters;
    String[][] mTechLists;
    //==================== NFC相关参数结束 ====================================

    /**
     * 芯片物理id
     */
    String mUid = "";
    String mDnaId = "";
    boolean hasRequested = false;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_dna_info;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        TopUtil.updateRight(mActivity, R.id.top_right, R.drawable.share);
    }

    @Override
    protected void initData() {
//        showFailLayout("请把卡片放在支持NFC功能的手机背面");
        initNfc();
        //TODO 模拟NFC测试
        processUid("04AFA02A783F80");//认证通过 T00000025
//        processUid("0491A02A783F80");//认证不通过 T00000028
//        processUid("0491A011183F80");//认证不通过 乱造的数据
    }

    private void initNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        mFilters = new IntentFilter[]{ndef, tech, tag};
        mTechLists = new String[][]{new String[]{NfcF.class.getName()}};
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mUid = getUidByIntent(intent);
        processUid(mUid);
    }

    /**
     * 获取芯片物理id （uid）
     *
     * @param intent
     */
    private String getUidByIntent(Intent intent) {
        showLoadingBar();
        String action = intent.getAction();
        Log.d("action=" + action);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Log.d("id(解码):" + NFCUtils.toHexString(tag.getId(), 0, tag.getId().length));
            String uid = NFCUtils.toHexString(tag.getId(), 0, tag.getId().length);
            return uid;
        }
        return "";
    }

    /**
     * 请求接口，获取dnaid
     *
     * @param uid
     */
    private void processUid(String uid) {
        Request request = new Request(Urls.GET_DNA_ID_URL());
        request.addParameter("uid", uid);
        request.setCallback(jsonCallback);
        request.execute();
        hasRequested = true;
    }

    JsonCallback<DNAEntity> jsonCallback = new JsonCallback<DNAEntity>() {

        @Override
        public void onSuccess(DNAEntity result) {
            hideLoadingBar();
            if (result == null) {
                showFailLayout("请求异常，请稍候再试");
                return;
            }
            if (result.RtnCode == 0) {
                if (!TextUtils.isEmpty(result.RtnMsg)) {
                    toast(result.RtnMsg);
                }
                showUnAuthInfo();
                return;
            }
            try {
                mDnaId = result.dnaId;
                dnaCode.setText("DNA编码:" + mDnaId);
                if (TextUtils.isEmpty(mDnaId)) {
                    showUnAuthInfo();
                } else {
                    showAuthInfo();
                    requestDnaInfo();
                }
            } catch (Exception e) {
                Log.e(e.getMessage(), e);
            }
        }

        @Override
        public void onFailure(AppException exception) {
            showFailLayout("请求异常，请稍候再试");
            if (exception.getStatus() == AppException.ExceptionStatus.IOException
                    || exception.getStatus() == AppException.ExceptionStatus.TimeoutException) {
                toast("网络超时，请稍候再试");
            } else {
                toast(exception.getMessage());
            }
        }

    };

    private void showAuthInfo() {
        icAuth.setImageResource(R.drawable.ic_auth);
        dnaCount.setVisibility(View.VISIBLE);
        dnaCode.setVisibility(View.VISIBLE);
        authCompanyTip.setVisibility(View.VISIBLE);
        detailList.setVisibility(View.VISIBLE);
    }

    private void showUnAuthInfo() {
        icAuth.setImageResource(R.drawable.ic_unauth);
        dnaCount.setVisibility(View.GONE);
        dnaCode.setVisibility(View.GONE);
        authCompanyTip.setVisibility(View.GONE);
        detailList.setVisibility(View.GONE);
    }

    private void requestDnaInfo() {
        showLoadingBar();
        Request request = new Request(Urls.GET_DNA_INFO_URL());
        request.addParameter("dnaID", mDnaId);
        request.setCallback(dnaInfoCallback);
        request.execute();
    }

    JsonCallback<DNADetailEntity> dnaInfoCallback = new JsonCallback<DNADetailEntity>() {

        @Override
        public void onSuccess(DNADetailEntity result) {
            hideLoadingBar();
            if (result == null) {
                showFailLayout("请求异常，请稍候再试");
                return;
            }
            if (result.RtnCode == 0) {
                if (!TextUtils.isEmpty(result.RtnMsg)) {
                    toast(result.RtnMsg);
                }
                return;
            }
            try {
                fetchData(result);
            } catch (Exception e) {
                Log.e(e.getMessage(), e);
            }
        }

        @Override
        public void onFailure(AppException exception) {
            showFailLayout("请求异常，请稍候再试");
            if (exception.getStatus() == AppException.ExceptionStatus.IOException
                    || exception.getStatus() == AppException.ExceptionStatus.TimeoutException) {
                toast("网络超时，请稍候再试");
            } else {
                toast(exception.getMessage());
            }
        }

    };

    private void fetchData(DNADetailEntity entity) throws Exception {
        imageLoader.displayImage(Urls.GET_SERVER_ROOT_URL() + entity.imgUrl, artImg);
        if(!TextUtils.isEmpty(entity.artName)){
            artName.setText(entity.artName);
        }
        if(!TextUtils.isEmpty(entity.ArtType)){
            artType.setText(entity.ArtType);
        }
        if(!TextUtils.isEmpty(entity.artAge)){
            artAge.setText(entity.artAge);
        }
        if(!TextUtils.isEmpty(entity.artPrice)){
            artPrice.setText(entity.artPrice);
        }
        if(!TextUtils.isEmpty(entity.artModel)){
            artModel.setText(entity.artModel);
        }
        if(!TextUtils.isEmpty(entity.artAuthor)){
            artAuthor.setText(entity.artAuthor);
        }
    }

    @Override
    public void onRequestAgain() {
//        if (!hasRequested) {
//            return;
//        }
//        super.onRequestAgain();
//        if (TextUtils.isEmpty(mDnaId)) {
//            processUid(mUid);
//        } else {
//            requestDnaInfo();
//        }
    }

    @Nullable
    @OnClick(R.id.artCollectAuthorLayout)
    public void toCollectInfoLayout(){
        toast("收藏信息");
    }

    @Nullable
    @OnClick(R.id.artCertificateNameLayout)
    public void toCertificateLayout(){
        toast("相关证书");
    }

    @Override
    public String getTopTitle() {
        return "艺术品DNA";
    }
}
