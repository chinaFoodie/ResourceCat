package com.cn.clound.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.cn.clound.R;
import com.cn.clound.adapter.CalendarAdapter;
import com.cn.clound.interfaces.DateCallBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private LinearLayout lLayout_bg;
    private CalendarAdapter adapter = null;
    private int jumpMonth = 0;
    private int jumpYear = 0;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private List<String> listClick = new ArrayList<String>();
    TextView currentMonth;

    private DateCallBack dateCallBack;

    public void setDateCallBack(DateCallBack dateCallBack) {
        this.dateCallBack = dateCallBack;
    }

    public CalendarDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
    }

    public CalendarDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dt_dialog_calendar, null);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        currentMonth = (TextView) view.findViewById(R.id.tv_meetings_month);
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        ViewFlipper flipper = (ViewFlipper) view.findViewById(R.id.calendar_flipper);
        gestureDetector = new GestureDetector(context, new MyGestureListener(flipper, view));
        flipper.removeAllViews();
        addGridView(flipper, view, 0);
        return this;
    }

    private void addGridView(ViewFlipper flipper, View view, int gvFlag) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int Width = display.getWidth();
        int Height = display.getHeight();
        adapter = new CalendarAdapter(context, context.getResources(), jumpMonth,
                jumpYear, year_c, month_c, day_c, listClick);
        gridView = new GridView(context);
        gridView.setNumColumns(7);
        gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setVerticalSpacing(0);
        gridView.setHorizontalSpacing(0);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return gestureDetector.onTouchEvent(event);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dateCallBack != null) {
                    String month = adapter.getShowMonth();
                    String day = adapter.getDateByClickItem(position).
                            substring(0, adapter.getDateByClickItem(position).indexOf("."));
                    if (month.length() < 2) {
                        month = "0" + month;
                    }
                    if (day.length() < 2) {
                        day = "0" + day;
                    }
                    dateCallBack.callBack(adapter.getShowYear() + "-" + month + "-" + day);
                }
            }
        });
        gridView.setLayoutParams(params);
        gridView.setAdapter(adapter);
        flipper.addView(gridView, gvFlag);
        dialog.setContentView(view);
        addTextToTopTextView(currentMonth);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(display
                .getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public void show() {
        dialog.show();
    }

    public void dissMiss() {
        dialog.dismiss();
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        ViewFlipper flipper;
        View view;

        public MyGestureListener(ViewFlipper flipper, View view) {
            this.flipper = flipper;
            this.view = view;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            if (e1.getX() - e2.getX() > 120) {
                enterNextMonth(gvFlag, flipper, view);
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                enterPrevMonth(gvFlag, flipper, view);
                return true;
            }
            return false;
        }
    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag, ViewFlipper flipper, View view) {
        jumpMonth++;
        gvFlag++;
        addGridView(flipper, view, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.fragment_slide_right_enter));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.fragment_slide_left_exit));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag, ViewFlipper flipper, View view) {
        jumpMonth--; // 上一个月
        gvFlag++;
        addGridView(flipper, view, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.fragment_slide_left_enter));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.fragment_slide_right_exit));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(adapter.getShowYear()).append("年").append(adapter.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }
}
