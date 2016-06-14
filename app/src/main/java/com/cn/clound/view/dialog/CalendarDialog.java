package com.cn.clound.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.cn.clound.R;

/**
 * 日历选择对话框
 *
 * @author Chunfalee(ly09219@gmail.com)
 * @date 2016-6-14 19:07:03
 */
public class CalendarDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private GestureDetector gestureDetector = null;
    private GridView gridView = null;

    public CalendarDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public CalendarDialog builder() {
        // 获取Dialog布局
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        addGridView();
        return this;
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int Width = display.getWidth();
        int Height = display.getHeight();
        gridView = new GridView(context);
        gridView.setNumColumns(7);
        gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(0);
        gridView.setHorizontalSpacing(0);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return gestureDetector.onTouchEvent(event);
            }
        });
        gridView.setLayoutParams(params);
        dialog.setContentView(gridView);
    }

    public void show() {
        dialog.show();
    }

    public void dissMiss() {
        dialog.dismiss();
    }
}
