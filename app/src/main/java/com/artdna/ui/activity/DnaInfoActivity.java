package com.artdna.ui.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artdna.R;
import com.artdna.bean.DNACollectEntity;
import com.artdna.bean.DNADetailEntity;
import com.artdna.bean.DNAEntity;
import com.artdna.bean.TongbaoDNADetailEntity;
import com.artdna.config.ArtKey;
import com.artdna.config.Urls;
import com.artdna.ui.base.BaseArtActivity;
import com.artdna.utils.NFCUtils;
import com.artdna.utils.TopUtil;
import com.shengshi.base.tools.Log;
import com.shengshi.http.net.AppException;
import com.shengshi.http.net.Request;
import com.shengshi.http.net.callback.JsonCallback;

import java.io.IOException;

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
    @Bind(R.id.artAuthorInfo)
    TextView artAuthorInfo;
    @Bind(R.id.zsmc)
    TextView zsmc;
    @Bind(R.id.bfjg)
    TextView bfjg;
    @Bind(R.id.collectInfoContainer)
    LinearLayout collectContainer;
    @Bind(R.id.zjContainer)
    LinearLayout zjContainer;
    @Bind(R.id.dna_other_info_rootview)
    View otherInfoLayout;

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

    private static final float BEEP_VOLUME = 0.50f;
    MediaPlayer mediaPlayer;
    boolean playBeep;
    boolean vibrate = true;

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
        showFailLayout("请把卡片放在支持NFC功能的手机背面");
        initNfc();
        //TODO 模拟NFC测试
//        processUid("0464A02A783F80");//认证通过 T00000051
//        processUid("04AFA02A783F80");//认证通过 T00000025
//        processUid("0491A02A783F80");//认证不通过 T00000028
//        processUid("0491A011183F80");//认证不通过 乱造的数据
    }

    private void initNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
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
        playBeep = true;
        vibrate = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
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

    JsonCallback<DNAEntity> jsonCallback = new CommonJsonCallback<DNAEntity>() {

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
                    playBeepSoundAndVibrate();
                    showAuthInfo();
                    //TODO 测试，发布删掉下面两行
//                    requestDnaInfoByTongbao();
//                    otherInfoLayout.setVisibility(View.GONE);
                    requestDnaInfo();
                    requestCollectInfo();
                }
            } catch (Exception e) {
                Log.e(e.getMessage(), e);
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

    private void requestDnaInfoByTongbao() {
        showLoadingBar();
        Request request = new Request(Urls.GET_DNA_INFO_URL_BY_TONG_BAO());
        //TODO 通宝商城接口测试
//        request.addParameter("LableId", "T000000011");
        request.addParameter("LableId", mDnaId);
        request.setCallback(tongbaoDnaInfoCallback);
        request.execute();
    }

    private void requestCollectInfo() {
        showLoadingBar();
        Request request = new Request(Urls.GET_DNA_COLLECT_INFO_URL());
        request.addParameter("dnaId", mDnaId);
//        request.addParameter("artId", "5ecc536e5267ca8e015267ce2a46000e");
        request.setCallback(dnaCollectInfoCallback);
        request.execute();
    }

    JsonCallback<DNADetailEntity> dnaInfoCallback = new CommonJsonCallback<DNADetailEntity>() {

        @Override
        public void onSuccess(DNADetailEntity result) {
            hideLoadingBar();
            if (result == null) {
//                showFailLayout("请求异常，请稍候再试");
                return;
            }
            if (result.RtnCode == 0) {
//                if (!TextUtils.isEmpty(result.RtnMsg)) {
//                    toast(result.RtnMsg);
//                }
                //读取芯片时，如果从艺术品信息接口获取不到信息就从通宝商城接口获取，展示内容只有作家信息上面那些，其他包括作家信息全部不要
                requestDnaInfoByTongbao();
                otherInfoLayout.setVisibility(View.GONE);
                return;
            }
            try {
                fetchData(result);
            } catch (Exception e) {
                Log.e(e.getMessage(), e);
            }
        }

    };

    JsonCallback<TongbaoDNADetailEntity> tongbaoDnaInfoCallback = new CommonJsonCallback<TongbaoDNADetailEntity>() {

        @Override
        public void onSuccess(TongbaoDNADetailEntity result) {
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
                fetchDataByTongbao(result);
            } catch (Exception e) {
                Log.e(e.getMessage(), e);
            }
        }

    };

    JsonCallback<DNACollectEntity> dnaCollectInfoCallback = new CommonJsonCallback<DNACollectEntity>() {

        @Override
        public void onSuccess(DNACollectEntity result) {
            hideLoadingBar();
            if (result == null) {
                return;
            }
            if (result.RtnCode == 0) {
                if (!TextUtils.isEmpty(result.RtnMsg)) {
                    toast(result.RtnMsg);
                }
                return;
            }
            try {
                fetchCollectData(result);
            } catch (Exception e) {
                Log.e(e.getMessage(), e);
            }
        }

    };

    private void fetchData(final DNADetailEntity entity) throws Exception {
        imageLoader.displayImage(Urls.GET_SERVER_ROOT_URL() + entity.imgUrl, artImg);
        artImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShowImg(Urls.GET_SERVER_ROOT_URL() + entity.imgUrl);
            }
        });
        if (!TextUtils.isEmpty(entity.artName)) {
            artName.setText(entity.artName);
        }
        if (!TextUtils.isEmpty(entity.ArtType)) {
            artType.setText(entity.ArtType);
        }
        if (!TextUtils.isEmpty(entity.artAge)) {
            artAge.setText(entity.artAge);
        }
        if (!TextUtils.isEmpty(entity.artPrice)) {
            artPrice.setText(entity.artPrice);
        }
        if (!TextUtils.isEmpty(entity.artModel)) {
            artModel.setText(entity.artModel);
        }
        if (!TextUtils.isEmpty(entity.artAuthor)) {
            artAuthor.setText(entity.artAuthor);
        }
        if (!TextUtils.isEmpty(entity.artAuthorInfo)) {
            artAuthorInfo.setText(entity.artAuthorInfo);
        }
        if (!TextUtils.isEmpty(entity.certificateName)) {
            zsmc.setText(entity.certificateName);
        }
        if (!TextUtils.isEmpty(entity.issAuthority)) {
            bfjg.setText(entity.issAuthority);
        }
        if (!TextUtils.isEmpty(entity.photo)) {
            LinearLayout itemView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.widget_item_dna_zj, null);
            ImageView zjIv = (ImageView) itemView.findViewById(R.id.zjImg);
            imageLoader.displayImage(Urls.GET_SERVER_ROOT_URL() + entity.photo, zjIv);
            zjContainer.addView(itemView);
            zjIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doShowImg(Urls.GET_SERVER_ROOT_URL() + entity.photo);
                }
            });
        }
        if (!TextUtils.isEmpty(entity.photos)) {
            String[] imgUrls = entity.photos.split(",");//接口第一个就返回逗号
            if (imgUrls != null && imgUrls.length > 1) {
                for (int i = 1; i < imgUrls.length; i++) {
                    LinearLayout itemView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.widget_item_dna_zj, null);
                    ImageView zjIv = (ImageView) itemView.findViewById(R.id.zjImg);
                    final String imageUrl = Urls.GET_SERVER_ROOT_URL() + imgUrls[i];
                    imageLoader.displayImage(imageUrl, zjIv);
                    zjContainer.addView(itemView);
                    zjIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doShowImg(imageUrl);
                        }
                    });
                }
            }

        }
    }

    private void fetchDataByTongbao(final TongbaoDNADetailEntity entity) throws Exception {
        imageLoader.displayImage(entity.ImgUrl, artImg);
        artImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShowImg(entity.ImgUrl);
            }
        });
        if (!TextUtils.isEmpty(entity.ArtName)) {
            artName.setText(entity.ArtName);
        }
        if (!TextUtils.isEmpty(entity.ArtType)) {
            artType.setText(entity.ArtType);
        }
        if (!TextUtils.isEmpty(entity.ArtYear)) {
            artAge.setText(entity.ArtYear);
        }
        if (!TextUtils.isEmpty(entity.artPrice)) {
            artPrice.setText(entity.artPrice);
        }
    }


    private void fetchCollectData(DNACollectEntity entity) throws Exception {
        for (final DNACollectEntity.CollectItem item : entity.data) {
            LinearLayout itemView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.widget_item_collect_dna_info, null);
            TextView collectorTv = (TextView) itemView.findViewById(R.id.collector);
            TextView shortInfoTv = (TextView) itemView.findViewById(R.id.artCollectShortInfo);
            ImageView collectIv = (ImageView) itemView.findViewById(R.id.artCollectImg);
            if (!TextUtils.isEmpty(item.collector)) {
                collectorTv.setText(item.collector + "(" + item.startDate + " 至 " + item.endDate + ")");
            }
            if (!TextUtils.isEmpty(item.remark)) {
                shortInfoTv.setText(item.remark);
            }
            imageLoader.displayImage(Urls.GET_SERVER_ROOT_URL() + item.pic, collectIv);
            collectIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doShowImg(Urls.GET_SERVER_ROOT_URL() + item.pic);
                }
            });
            collectContainer.addView(itemView);
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

    @Override
    public String getTopTitle() {
        return "艺术品DNA";
    }

    @Nullable
    @OnClick({R.id.support_tech})
    public void supportTech() {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra(ArtKey.KEY_INTENT_WEB_URL, "http://www.bk-easy.com");
        startActivity(intent);
    }

    public void doShowImg(String imgPath) {
        Intent intent = new Intent(mContext, ViewImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("urls", new String[]{imgPath});
        bundle.putInt("index", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.success);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(200);
        }
    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Nullable
    @OnClick({R.id.top_right})
    public void toastMsg() {
        toast("让艺术品都流通起来");
    }
}
