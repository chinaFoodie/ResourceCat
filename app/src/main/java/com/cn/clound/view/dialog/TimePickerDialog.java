package com.cn.clound.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.DensityUtil;
import com.cn.clound.base.common.utils.DisplayUtil;
import com.cn.clound.view.wheelpicker.WheelDateAdapter;
import com.cn.clound.view.wheelpicker.views.OnWheelChangedListener;
import com.cn.clound.view.wheelpicker.views.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间选择器dialog
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-26 17:59:25
 */
public class TimePickerDialog extends Dialog {
    private int maxTextSize = 24;
    private int minTextSize = 14;
    private Context context;
    private WheelView wheelStartHour, wheelStartMinute, wheelEndHour, wheelEndMinute;
    private WheelDateAdapter startHourAdapter, startMinuteAdapter, endHourAdapter, endMinuteAdapter;
    private List<String> listHour = new ArrayList<String>();
    private List<String> listMinute = new ArrayList<String>();
    private String startHour, startMinute, endHour, endMinute;
    private DateChangerListener onDateChangerListener;

    public void setOnDateChangerListener(DateChangerListener onDateChangerListener) {
        this.onDateChangerListener = onDateChangerListener;
    }

    public interface DateChangerListener {
        void currentDate(String startTime, String endTime);
    }

    public TimePickerDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TimePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alert_choose_time_pop, null);
        setContentView(view);

        wheelStartHour = (WheelView) view.findViewById(R.id.wheel_start_hour);
        wheelStartMinute = (WheelView) view.findViewById(R.id.wheel_start_minute);
        wheelEndHour = (WheelView) view.findViewById(R.id.wheel_end_hour);
        wheelEndMinute = (WheelView) view.findViewById(R.id.wheel_end_minute);

        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = DensityUtil.getScreenW(context);
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        initWheelView(view);
    }

    private void initWheelView(View view) {

        listHour.removeAll(listHour);
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                listHour.add("0" + i + "");
            } else {
                listHour.add(i + "");
            }
        }

        for (int j = 0; j < 60; j++) {
            if (j < 10) {
                listMinute.add("0" + j);
            } else {
                listMinute.add(j + "");
            }
        }

        startHourAdapter = new WheelDateAdapter(context, listHour, 6, maxTextSize, minTextSize);
        startMinuteAdapter = new WheelDateAdapter(context, listMinute, 16, maxTextSize, minTextSize);
        endHourAdapter = new WheelDateAdapter(context, listHour, 6, maxTextSize, minTextSize);
        endMinuteAdapter = new WheelDateAdapter(context, listMinute, 16, maxTextSize, minTextSize);

        wheelStartHour.setViewAdapter(startHourAdapter);
        wheelStartMinute.setViewAdapter(startMinuteAdapter);
        wheelEndHour.setViewAdapter(endHourAdapter);
        wheelEndMinute.setViewAdapter(endMinuteAdapter);

        wheelStartHour.setCurrentItem(6);
        startHour = (String) startHourAdapter.getItemText(6);
        wheelStartHour.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) startHourAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, startHourAdapter);
                startHour = currentText;
            }
        });

        wheelStartMinute.setCurrentItem(16);
        startMinute = (String) startMinuteAdapter.getItemText(16);
        wheelStartMinute.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) startMinuteAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, startMinuteAdapter);
                startMinute = currentText;
            }
        });

        wheelEndHour.setCurrentItem(6);
        endHour = (String) endHourAdapter.getItemText(6);
        wheelEndHour.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) endHourAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, endHourAdapter);
                endHour = currentText;
            }
        });

        wheelEndMinute.setCurrentItem(16);
        endMinute = (String) endMinuteAdapter.getItemText(16);
        wheelEndMinute.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) endMinuteAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, endMinuteAdapter);
                endMinute = currentText;
            }
        });

        view.findViewById(R.id.tv_cancel_get_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.this.dismiss();
            }
        });

        view.findViewById(R.id.tv_sure_get_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDateChangerListener != null) {
                    onDateChangerListener.currentDate(startHour + ":" + startMinute, endHour + ":" + endMinute);
                }
            }
        });
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, WheelDateAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(24);
            } else {
                textvew.setTextSize(14);
            }
        }
    }
}
