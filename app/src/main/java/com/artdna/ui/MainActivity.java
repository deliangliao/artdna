package com.artdna.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.text.TextUtils;
import android.widget.TextView;

import com.artdna.R;
import com.artdna.config.ArtConstants;
import com.artdna.ui.base.BaseArtActivity;
import com.artdna.utils.NFCUtils;
import com.shengshi.base.tools.Log;

import butterknife.Bind;

public class MainActivity extends BaseArtActivity {

    @Bind(R.id.artid)
    TextView artId;

    //==================== NFC相关参数开始 ====================================
    NfcAdapter nfcAdapter;
    PendingIntent mPendingIntent;
    IntentFilter[] mFilters;
    String[][] mTechLists;
    int readCardType = ArtConstants.READ_CARD_TYPE_SOURCE;
    //==================== NFC相关参数结束 ====================================

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        initNfc();
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
        parseArtId(intent);
    }

    private void parseArtId(Intent intent) {
        String action = intent.getAction();
        Log.d("action=" + action);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techArray = tag.getTechList();
            StringBuilder sb = new StringBuilder();

            Log.d("id:" + tag.getId());
            Log.d("id(解码):" + NFCUtils.toHexString(tag.getId(), 0, tag.getId().length));
            String cardId = NFCUtils.toHexString(tag.getId(), 0, tag.getId().length);
            toast("id(解码):" + cardId);

            if (TextUtils.isEmpty(cardId)) {
                // 如果卡id为空
                toast(R.string.alert_card_mac_blank);
                return;
            }
            if (readCardType == ArtConstants.READ_CARD_TYPE_SOURCE) { // 溯源卡
                artId.setText(cardId);
                processRfid(cardId);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Log.d(NfcAdapter.ACTION_TECH_DISCOVERED);
        } else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Log.d(NfcAdapter.ACTION_NDEF_DISCOVERED);
        }
    }

    private void processRfid(String cardId) {

    }

    @Override
    public String getTopTitle() {
        return "测试ArtId";
    }
}
