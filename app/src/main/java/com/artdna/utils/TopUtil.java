package com.artdna.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p>Title:     设置头部的左按键，中间标题，右按键
 * <p>Description:
 * <p>@author:  liaodl
 * <p>Copyright: Copyright (c) 2014
 * <p>Company: @小鱼网
 * <p>Create Time: 2014-10-31
 * <p>@author:
 * <p>Update Time:
 * <p>Updater:       liaodl
 * <p>Update Comments: 支持Fragment里的控件查找
 */
public class TopUtil {

    /**
     * 设置标题
     */
    public static void updateTitle(TextView view, String title) {
        if (view != null && !TextUtils.isEmpty(title)) {
            view.setVisibility(View.VISIBLE);
            view.setSelected(true);
            view.setText(title);
        }
    }

    /**
     * 设置标题
     */
    public static void updateTitle(Activity activity, int viewId, String title) {
        TextView view = (TextView) activity.findViewById(viewId);
        updateTitle(view, title);
    }

    /**
     * 设置标题
     */
    public static void updateTitle(View parentView, int viewId, String title) {
        TextView textView = (TextView) parentView.findViewById(viewId);
        updateTitle(textView, title);
    }

    /**
     * 设置标题
     */
    public static void updateTitle(Activity activity, int viewId, int resourceId) {
        TextView view = (TextView) activity.findViewById(viewId);
        String title = activity.getString(resourceId);
        updateTitle(view, title);
    }

    /**
     * 设置右边按键
     */
    public static void updateRight(View view, int resourceId) {
        if (view != null && view instanceof ImageView) {
            ((ImageView) view).setImageResource(resourceId);
        } else {
            view.setBackgroundResource(resourceId);
        }
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边按键
     */
    public static void updateRight(Activity activity, int viewId, int resourceId) {
        View view = activity.findViewById(viewId);
        updateRight(view, resourceId);
    }

    /**
     * 设置右边按键标题
     */
    public static void updateRightTitle(Activity activity, int viewId, int resource) {
        View view = activity.findViewById(viewId);
        if (view == null) {
            return;
        }
        if (view instanceof TextView) {
            view.setVisibility(View.VISIBLE);
            ((TextView) view).setText(activity.getString(resource));
        }
    }

    /**
     * 设置右边按键标题
     */
    public static void updateRightTitle(Activity activity, int viewId, String strRes) {
        View view = activity.findViewById(viewId);
        if (view == null) {
            return;
        }
        if (view instanceof TextView) {
            view.setVisibility(View.VISIBLE);
            ((TextView) view).setText(strRes);
        }
    }

    /**
     * 设置右边按键标题
     */
    public static void updateRightTitle(View parentView, int viewId, String resourceStr) {
        View view = parentView.findViewById(viewId);
        if (view == null) {
            return;
        }
        if (view instanceof TextView) {
            view.setVisibility(View.VISIBLE);
            ((TextView) view).setText(resourceStr);
        }
    }

    /**
     * 设置右边按键标题和图片
     * 标题在图片底部
     */
    public static void updateRightImgTitle(Activity activity, int viewId, int txtRes, int imgRes) {
        View view = activity.findViewById(viewId);
        if (view == null) {
            return;
        }
        if (view instanceof TextView) {
            view.setVisibility(View.VISIBLE);
            ((TextView) view).setText(activity.getString(txtRes));
            Drawable drawable = activity.getResources().getDrawable(imgRes);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView) view).setCompoundDrawables(null, drawable, null, null);
        }
    }

    /**
     * 设置左边按钮标题
     */
    public static void updateLeftTitle(Activity activity, int viewId, int resource) {
        View view = activity.findViewById(viewId);
        if (view == null) {
            return;
        }
        view.setVisibility(View.VISIBLE);
        if (view instanceof TextView) {
            ((TextView) view).setText(activity.getString(resource));
        }
    }

    /**
     * 设置左边按钮标题
     */
    public static void updateLeftTitle(Activity activity, int viewId, String text) {
        View view = activity.findViewById(viewId);
        if (view == null) {
            return;
        }
        view.setVisibility(View.VISIBLE);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
    }

    /**
     * 设置左边按钮标题
     */
    public static void updateLeftTitle(View parentView, int viewId, String text) {
        View view = parentView.findViewById(viewId);
        if (view == null) {
            return;
        }
        view.setVisibility(View.VISIBLE);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
    }

    /**
     * 改变左边按钮图片
     */
    public static void updateLeftIcon(Context context, View view, int resourceId) {
        if (view != null && view instanceof TextView) {
            Drawable drawable = null;
            try {
                drawable = context.getResources().getDrawable(resourceId);
            } catch (Resources.NotFoundException e) {
            }
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            ((TextView) view).setCompoundDrawables(drawable, null, null, null);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 改变左边按钮图片
     */
    public static void updateLeftIcon(Activity activity, int viewId, int resourceId) {
        View view = activity.findViewById(viewId);
        updateLeftIcon(activity, view, resourceId);
    }

    /**
     * 改变文字按钮图片
     */
    public static void updateTextViewIconLeft(Context context, View view, int resourceId) {
        if (view != null && view instanceof TextView) {
            Drawable drawable = null;
            try {
                drawable = context.getResources().getDrawable(resourceId);
            } catch (Resources.NotFoundException e) {
            }
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            ((TextView) view).setCompoundDrawables(drawable, null, null, null);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 改变文字按钮图片
     */
    public static void updateTextViewIconLeft(Activity activity, int viewId, int resourceId) {
        View view = activity.findViewById(viewId);
        updateTextViewIconLeft(activity, view, resourceId);
    }

    /**
     * 隐藏控件id为viewId的控件
     *
     * @param activity
     * @param viewId
     */
    public static void hideViewByINVISIBLE(Activity activity, int viewId) {
        hideView(activity, viewId, View.INVISIBLE);
    }

    public static void hideViewByINVISIBLE(View view, int viewId) {
        hideView(view, viewId, View.INVISIBLE);
    }

    public static void hideViewByGONE(Activity activity, int viewId) {
        hideView(activity, viewId, View.GONE);
    }

    public static void hideViewByGONE(View view, int viewId) {
        hideView(view, viewId, View.GONE);
    }

    public static void hideView(Activity activity, int viewId, int mode) {
        View view = activity.findViewById(viewId);
        if (view != null) {
            view.setVisibility(mode);
        }
    }

    public static void hideView(View parentView, int viewId, int mode) {
        View view = parentView.findViewById(viewId);
        if (view != null) {
            view.setVisibility(mode);
        }
    }

    /**
     * 显示控件id为viewId的控件
     *
     * @param activity
     * @param viewId
     */
    public static void showView(Activity activity, int viewId) {
        View view = activity.findViewById(viewId);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示控件id为viewId的控件
     *
     * @param activity
     * @param viewId
     */
    public static void showView(View parentView, int viewId) {
        View view = parentView.findViewById(viewId);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置控件点击监听动作
     */
    public static void setOnclickListener(Activity activity, int viewId, OnClickListener listener) {
        View view = activity.findViewById(viewId);
        if (view == null) {
            return;
        }
        view.setOnClickListener(listener);
    }

    /**
     * 设置控件点击监听动作
     */
    public static void setOnclickListener(View parentView, int viewId, OnClickListener listener) {
        View view = parentView.findViewById(viewId);
        if (view == null) {
            return;
        }
        view.setOnClickListener(listener);
    }

}
