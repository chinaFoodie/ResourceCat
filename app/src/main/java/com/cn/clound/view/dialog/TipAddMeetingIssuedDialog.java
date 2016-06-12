package com.cn.clound.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;

/**
 * 会议添加发布人提示dialog
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-7 17:19:16
 */
public class TipAddMeetingIssuedDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private LinearLayout lLayout_bg;
    private boolean showTitle = false;
    private TextView txt_title;
    private TextView tv2AddPublish;
    private Button btn_neg;
    private Button btn_pos;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;

    public TipAddMeetingIssuedDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public TipAddMeetingIssuedDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dt_tip_add_meeting_publish, null);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        tv2AddPublish = (TextView) view.findViewById(R.id.tv_add_meeting_publish);
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
        return this;
    }

    public TipAddMeetingIssuedDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public TipAddMeetingIssuedDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return TipAddMeetingIssuedDialog.this;
    }

    public void show() {
        setLayout();
        dialog.show();
    }

    public void dissMiss() {
        dialog.dismiss();
    }

    public TipAddMeetingIssuedDialog setNegativeButton(
            final View.OnClickListener listener) {
        showNegBtn = true;
        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public TipAddMeetingIssuedDialog setPositiveButton(
            final View.OnClickListener listener) {
        showPosBtn = true;
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public TipAddMeetingIssuedDialog setAddPublish(
            final View.OnClickListener listener) {
        tv2AddPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btn_pos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }
    }
}
