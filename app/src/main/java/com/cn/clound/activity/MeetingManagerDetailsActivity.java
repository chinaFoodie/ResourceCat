package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.MeetingAbsentOrLateRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.metting.MeetingAttendanceModel;
import com.cn.clound.bean.metting.MeetingDetailsModel;
import com.cn.clound.http.MyHttpHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 会议管理历史会议详情
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-1 18:45:18
 */
public class MeetingManagerDetailsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.recycler_title_time_line)
    RecyclerView recyclerViewTimeLine;
    @Bind(R.id.recycler_absent_menber)
    RecyclerView recyclerViewAbsent;
    @Bind(R.id.recycler_late_menber)
    RecyclerView recyclerViewLate;

    private MyHttpHelper httphelper;
    private int HTTP_GET_ABSENT_AND_LATE_MENBER = 151;
    private List<MeetingAttendanceModel.MeetingAttendance.Absent> listAbsent = new ArrayList<>();
    private List<MeetingAttendanceModel.MeetingAttendance.Absent> listLate = new ArrayList<>();
    private String detailId;
    private MeetingAbsentOrLateRecyclerAdapter absentAdapter;
    private MeetingAbsentOrLateRecyclerAdapter lateAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_ABSENT_AND_LATE_MENBER) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    MeetingAttendanceModel mam = (MeetingAttendanceModel) msg.obj;
                    if (mam != null) {
                        listAbsent = mam.getData().getAbsent();
                        listLate = mam.getData().getLate();
                        absentAdapter = new MeetingAbsentOrLateRecyclerAdapter(MeetingManagerDetailsActivity.this, listAbsent);
                        lateAdapter = new MeetingAbsentOrLateRecyclerAdapter(MeetingManagerDetailsActivity.this, listLate);
                        recyclerViewAbsent.setAdapter(absentAdapter);
                        recyclerViewLate.setAdapter(lateAdapter);
                    }
                } else {
                    Toastor.showToast(MeetingManagerDetailsActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_meeting_manager_details;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        httphelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        detailId = this.getIntent().getStringExtra("meeting_id");
        llBack.setOnClickListener(this);
        tvMidTitle.setText("会议详情");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTimeLine.setLayoutManager(layoutManager);
        recyclerViewAbsent.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerViewLate.setLayoutManager(new GridLayoutManager(this, 5));
        httphelper.postStringBack(HTTP_GET_ABSENT_AND_LATE_MENBER, AppConfig.GET_ABSENT_AND_LATE_MEETING_MENBER, getAbsent(detailId), handler, MeetingAttendanceModel.class);
    }

    private HashMap<String, String> getAbsent(String detailId) {
        HashMap<String, String> absent = new HashMap<String, String>();
        absent.put("detailId", detailId);
        absent.put("token", TelephoneUtil.getIMEI(this));
        return absent;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            default:
                break;
        }
    }
}
