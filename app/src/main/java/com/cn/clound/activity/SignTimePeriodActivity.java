package com.cn.clound.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.SharePreferceUtil;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.singed.QuerySignModel;
import com.cn.clound.bean.singed.SetSignModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;
import com.cn.clound.view.wheelpicker.WheelDateAdapter;
import com.cn.clound.view.wheelpicker.views.OnWheelChangedListener;
import com.cn.clound.view.wheelpicker.views.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 设置签到时间周期界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-14 11:34:23
 */
public class SignTimePeriodActivity extends BaseActivity implements View.OnClickListener {
    private int maxTextSize = 24;
    private int minTextSize = 14;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.wheel_view_mor_after)
    WheelView wheelViewMorAfter;
    @Bind(R.id.wheel_view_hours)
    WheelView wheelViewHours;
    @Bind(R.id.wheel_view_minute)
    WheelView wheelViewMinute;
    @Bind(R.id.tv_sign_zhouqi)
    TextView tvSignCycle;
    @Bind(R.id.rl_to_set_time_cycle)
    RelativeLayout rlTimeCycle;
    private QuerySignModel.QuerySign querySign;
    private WheelDateAdapter morAftAdapter, hoursAdapter, minuteAdapter;
    private String[] listMorAft = new String[]{"上午", "下午"};
    private String[] listHours = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private List<String> listMinute = new ArrayList<String>();
    private String alarmHours, alarmMinute, alarmMorAft;
    private CustomProgress progress;
    private int HTTP_SET_SIGN_TIP = 136;
    private MyHttpHelper httpHelper;
    private SharePreferceUtil share;
    private boolean NEED_REFRUSE = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_SET_SIGN_TIP) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    SetSignModel ssm = (SetSignModel) msg.obj;
                    if (ssm != null) {
                        SignTimePeriodActivity.this.finish();
                    }
                } else {
                    Toastor.showToast(SignTimePeriodActivity.this, msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_sign_time_period;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        share = SharePreferceUtil.getInstance(this);
        httpHelper = MyHttpHelper.getInstance(this);
        progress = new CustomProgress(this, "请稍候...");
        progress.show();
        querySign = (QuerySignModel.QuerySign) this.getIntent().getSerializableExtra("sign_tip_model");
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText("时间周期");
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("保存");
        tvBaseRight.setOnClickListener(this);
        rlTimeCycle.setOnClickListener(this);
        int choseMorAft = 0;
        int choseHours = 0;
        int choseMinute = 0;
        if (querySign != null) {
            int tempint = Integer.parseInt(querySign.getRemindTime().substring(0, querySign.getRemindTime().indexOf(":")));
            if (tempint <= 12 && tempint > 0) {
                choseMorAft = 0;
                alarmMorAft = "上午";
                choseHours = tempint;
//                choseHours--;
            } else {
                alarmMorAft = "下午";
                choseMorAft = 1;
                choseHours = tempint % 12;
                if (choseHours == 0) {
                    choseHours = 12;
                }
            }
            choseMinute = Integer.parseInt(querySign.getRemindTime().substring(querySign.getRemindTime().indexOf(":") + 1));
            tvSignCycle.setText(replase(querySign.getRepeatStr()));
            alarmMinute = choseMinute + "";
            alarmHours = choseHours + "";
            choseHours--;
        } else {
            alarmMorAft = "上午";
            alarmMinute = "00";
            alarmHours = "1";
        }
        morAftAdapter = new WheelDateAdapter(this, Arrays.asList(listMorAft), choseMorAft, maxTextSize, minTextSize);
        wheelViewMorAfter.setViewAdapter(morAftAdapter);
        wheelViewMorAfter.setVisibleItems(5);
        wheelViewMorAfter.setCurrentItem(choseMorAft);
        wheelViewMorAfter.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) morAftAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, morAftAdapter);
                alarmMorAft = currentText;
            }
        });
        hoursAdapter = new WheelDateAdapter(this, Arrays.asList(listHours), choseHours, maxTextSize, minTextSize);
        wheelViewHours.setViewAdapter(hoursAdapter);
        wheelViewHours.setVisibleItems(5);
        wheelViewHours.setCurrentItem(choseHours);
        wheelViewHours.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) hoursAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, hoursAdapter);
                alarmHours = currentText;
            }
        });
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                listMinute.add("0" + i + "");
            } else {
                listMinute.add(i + "");
            }
        }
        minuteAdapter = new WheelDateAdapter(this, listMinute, choseMinute, maxTextSize, minTextSize);
        wheelViewMinute.setViewAdapter(minuteAdapter);
        wheelViewMinute.setVisibleItems(5);
        wheelViewMinute.setCurrentItem(choseMinute);
        wheelViewMinute.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) minuteAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, minuteAdapter);
                alarmMinute = currentText;
            }
        });
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
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

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (NEED_REFRUSE) {
            String temp = share.getString("set_sign_tip_cycle");
            if (!temp.equals("")) {
                if (temp.substring(temp.length() - 1).equals(",")) {
                    temp = temp.substring(0, temp.length() - 1);
                }
                tvSignCycle.setText(replase(temp));
            } else {
                tvSignCycle.setText("每天");
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        NEED_REFRUSE = true;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        share.setCache("set_sign_tip_cycle", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            case R.id.tv_base_right:
                progress.show();
                httpHelper.postStringBack(HTTP_SET_SIGN_TIP, AppConfig.SET_MY_SIGN_TIP, setSign(), handler, SetSignModel.class);
                break;
            case R.id.rl_to_set_time_cycle:
                Intent intent = new Intent(this, TimeCycleActivity.class);
                startActivity(intent);
                break;
        }
    }

    private HashMap<String, String> setSign() {
        HashMap<String, String> parmears = new HashMap<String, String>();
        if (querySign != null) {
            parmears.put("id", querySign.getId());
            parmears.put("state", querySign.getState());
        } else {
            parmears.put("state", "1");
        }
        if (alarmMorAft.equals("下午")) {
            alarmHours = Integer.parseInt(alarmHours) + 12 + "";
            if (Integer.parseInt(alarmHours) == 24) {
                alarmHours = 0 + "";
            }
        }
        parmears.put("remindTime", alarmHours + ":" + alarmMinute);
        String temp = share.getString("set_sign_tip_cycle");
        if (!temp.equals("")) {
            if (temp.substring(temp.length() - 1).equals(",")) {
                parmears.put("repeatStr", temp.substring(0, temp.length() - 1));
            } else {
                parmears.put("repeatStr", temp);
            }
        } else {
            parmears.put("repeatStr", "1,2,3,4,5,6,7");
        }
        parmears.put("token", TelephoneUtil.getIMEI(this));
        parmears.put("type", "3");
        return parmears;
    }

    public static boolean isServiceWorked(Context context, String serviceName) {
        boolean isServiceWorked = false;
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
                isServiceWorked = true;
            }
        }
        return isServiceWorked;
    }

    private String replase(String replase) {
        if (replase.contains("1") && replase.contains("2") && replase.contains("3") && replase.contains("4") &&
                replase.contains("5") && replase.contains("6") && replase.contains("7")) {
            replase = "每天";
        } else {
            replase = replase.replace("1", "每周一").replace("2", "每周二").replace("3", "每周三")
                    .replace("4", "每周四").replace("5", "每周五").replace("6", "每周六").replace("7", "每周日");
        }
        return replase;
    }
}
