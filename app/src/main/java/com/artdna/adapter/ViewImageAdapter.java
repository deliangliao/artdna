package com.artdna.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.artdna.R;
import com.artdna.utils.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.shengshi.base.tools.FileUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

import uk.co.senab.photoview.PhotoView;

public class ViewImageAdapter extends PagerAdapter {
    private List<String> data;
    private String mCurrentImgUrl;
    private int currPos;
    private Activity activity;
    ImageLoader loader;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (listener != null) {
                listener.onSuccess();
            }
        }

    };

    public ViewImageAdapter(Activity activity, int position, List<String> data) {
        this.activity = activity;
        this.currPos = position;
        this.data = data;
        loader = ImageLoader.getInstance(activity.getApplicationContext());
        loader.setImageOnLoadFail(R.color.transparent_black);
        loader.setImageOnLoading(R.color.transparent_black);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public int getCurrPos() {
        return currPos;
    }

    public void setCurrPos(int currPos) {
        this.currPos = currPos;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        PhotoView photoView = new PhotoView(activity);
        mCurrentImgUrl = data.get(position);
        if (mCurrentImgUrl == null) {
            return photoView;
        }
        if (!mCurrentImgUrl.contains("http://")) {
            mCurrentImgUrl = "file:///" + mCurrentImgUrl;
        }
        loader.displayImage(mCurrentImgUrl, photoView, true);
        ((ViewPager) container).addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return photoView;
    }

    public void saveImgInThread() {
        mCurrentImgUrl = data.get(currPos);
        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                File image = DiskCacheUtils.findInCache(mCurrentImgUrl,
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                                .getDiskCache());
                String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
                String photoName = mCurrentImgUrl.substring(mCurrentImgUrl.lastIndexOf("/") + 1);
                File tagFile = FileUtils.copyFile(image, path, photoName);
                // 刷新相册
                if (tagFile.getPath().length() > 0) {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(tagFile.getPath()));
                    intent.setData(uri);
                    activity.sendBroadcast(intent);
                    mHandler.obtainMessage().sendToTarget();
                }
            }
        });
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewGroup) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    OnSaveSuccessListener listener;

    public void setListener(OnSaveSuccessListener listener) {
        this.listener = listener;
    }

    public interface OnSaveSuccessListener {
        public void onSuccess();
    }

}