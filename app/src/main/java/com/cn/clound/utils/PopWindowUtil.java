package com.cn.clound.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.cn.clound.R;

/**
 * PopWindow
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-29 10:50:03
 */
public class PopWindowUtil extends PopupWindow {
    private Context context;
    private View layout;

    public PopWindowUtil(Context context, View layout) {
        this.context = context;
        this.layout = layout;
        // 设置SelectPicPopupWindow的View
        this.setContentView(layout);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationPreview);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 16, 4);
        } else {
            this.dismiss();
        }
    }

    /**
     * 隐藏popupWindow
     */
    public void dismissPopupWindow() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }
}
