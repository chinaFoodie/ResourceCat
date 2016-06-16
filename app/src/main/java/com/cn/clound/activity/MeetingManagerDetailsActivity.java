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
import com.cn.clound.adapter.MeetingDetailsTimeTitleRecyclerAdapter;
import com.cn.clound.interfaces.OnItemClickListener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.metting.MeetingAttendanceModel;
import com.cn.clound.bean.metting.MeetingDetailsModel;
import com.cn.clound.bean.metting.MeetingDetailsTitleTimeModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;

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
    @Bind(R.id.tv_absent_menber)
    TextView tvAbsentNumber;
    @Bind(R.id.tv_total_menber)
    TextView tvTotalNumber;
    @Bind(R.id.tv_late_menber)
    TextView tvLateNumber;
    @Bind(R.id.tv_late_total_menber)
    TextView tvLateTotalNumber;
    @Bind(R.id.tv_absent_number_value)
    TextView tvAbsentValue;
    @Bind(R.id.tv_late_number_value)
    TextView tvLateValue;
    @Bind(R.id.tv_meeting_number_total)
    TextView tvMeetingNumber;
    @Bind(R.id.tv_meeting_number_come)
    TextView tvComeNumber;
    @Bind(R.id.tv_history_meeting_type)
    TextView tvMeetingType;
    @Bind(R.id.tv_meeting_manager_name)
    TextView tvMeetingName;
    @Bind(R.id.tv_meeting_manager_time)
    TextView tvMeetingTime;

    private MyHttpHelper httphelper;
    private int HTTP_GET_ABSENT_AND_LATE_MENBER = 151;
    private int HTTP_GET_HISTORY_MEETING_DETAILS = 152;
    private List<MeetingAttendanceModel.MeetingAttendance.Absent> listAbsent = new ArrayList<>();
    private List<MeetingAttendanceModel.MeetingAttendance.Absent> listLate = new ArrayList<>();
    private List<MeetingDetailsTitleTimeModel> listTime = new ArrayList<>();
    private String meetingId;
    private MeetingDetailsModel mdm;
    private MeetingAbsentOrLateRecyclerAdapter absentAdapter;
    private MeetingAbsentOrLateRecyclerAdapter lateAdapter;
    private MeetingDetailsTimeTitleRecyclerAdapter timeAdapter;
    private CustomProgress progress;
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
                        LinearLayout.LayoutParams absent = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
                        absent.weight = (float) listAbsent.size();
                        tvAbsentNumber.setLayoutParams(absent);
                        LinearLayout.LayoutParams absentTotal = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
                        absentTotal.weight = (float) mdm.getData().getUsers().size() - listAbsent.size();
                        tvTotalNumber.setLayoutParams(absentTotal);
                        LinearLayout.LayoutParams late = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
                        late.weight = (float) listLate.size();
                        tvLateNumber.setLayoutParams(late);
                        tvComeNumber.setText(mdm.getData().getUsers().size() - listAbsent.size() + "");
                        tvMeetingNumber.setText(mdm.getData().getUserSize() + "");
                        tvAbsentValue.setText("缺席" + listAbsent.size() + "人");
                        tvLateValue.setText("迟到" + listLate.size() + "人");
                        LinearLayout.LayoutParams lateTotal = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
                        lateTotal.weight = (float) mdm.getData().getUsers().size() - listLate.size();
                        tvLateTotalNumber.setLayoutParams(lateTotal);
                        recyclerViewAbsent.setAdapter(absentAdapter);
                        recyclerViewLate.setAdapter(lateAdapter);
                    }
                } else {
                    Toastor.showToast(MeetingManagerDetailsActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_GET_HISTORY_MEETING_DETAILS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    mdm = (MeetingDetailsModel) msg.obj;
                    if (mdm != null) {
                        listTime.clear();
                        for (int i = 0; i < mdm.getData().getTimes().size(); i++) {
                            MeetingDetailsTitleTimeModel mdttm = new MeetingDetailsTitleTimeModel();
                            mdttm.setBeginAt(mdm.getData().getTimes().get(i).getBeginAt());
                            mdttm.setEndAt(mdm.getData().getTimes().get(i).getEndAt());
                            mdttm.setDetailId(mdm.getData().getTimes().get(i).getDetailId());
                            if (i == 0) {
                                mdttm.setChecked(true);
                            } else {
                                mdttm.setChecked(false);
                            }
                            listTime.add(mdttm);
                        }
                        if (listTime.size() > 1) {
                            recyclerViewTimeLine.setVisibility(View.VISIBLE);
                        } else {
                            recyclerViewTimeLine.setVisibility(View.GONE);
                        }
                        timeAdapter.notifyDataSetChanged();
                        timeAdapter.setOnItemClickLitener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                progress.show();
                                for (int i = 0; i < listTime.size(); i++) {
                                    if (i == position) {
                                        listTime.get(i).setChecked(true);
                                    } else {
                                        listTime.get(i).setChecked(false);
                                    }
                                }
                                tvMeetingTime.setText("会议时间：" + mdm.getData().getTimes().get(position).getBeginAt().substring(0, mdm.getData().getTimes().get(position).getBeginAt().indexOf(" ")));
                                httphelper.postStringBack(HTTP_GET_ABSENT_AND_LATE_MENBER, AppConfig.GET_ABSENT_AND_LATE_MEETING_MENBER, getAbsent(mdm.getData().getTimes().get(position).getDetailId()), handler, MeetingAttendanceModel.class);
                                timeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });
                        tvMeetingName.setText(mdm.getData().getName());
                        tvMeetingType.setText(mdm.getData().getTypeStr());
                        tvMeetingTime.setText("会议时间：" + mdm.getData().getTimes().get(0).getBeginAt().substring(0, mdm.getData().getTimes().get(0).getBeginAt().indexOf(" ")));
                        httphelper.postStringBack(HTTP_GET_ABSENT_AND_LATE_MENBER, AppConfig.GET_ABSENT_AND_LATE_MEETING_MENBER, getAbsent(mdm.getData().getTimes().get(0).getDetailId()), handler, MeetingAttendanceModel.class);
                    }
                } else {
                    Toastor.showToast(MeetingManagerDetailsActivity.this, msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
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
        progress = new CustomProgress(this, "加载中...");
        progress.show();
        httphelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        meetingId = this.getIntent().getStringExtra("meeting_id");
        llBack.setOnClickListener(this);
        tvMidTitle.setText("会议详情");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTimeLine.setLayoutManager(layoutManager);
        recyclerViewAbsent.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerViewLate.setLayoutManager(new GridLayoutManager(this, 5));
        timeAdapter = new MeetingDetailsTimeTitleRecyclerAdapter(this, listTime);
        recyclerViewTimeLine.setAdapter(timeAdapter);
        httphelper.postStringBack(HTTP_GET_HISTORY_MEETING_DETAILS, AppConfig.QUERY_MEETING_DETAILS, getDetails(meetingId), handler, MeetingDetailsModel.class);
    }

    private HashMap<String, String> getDetails(String id) {
        HashMap<String, String> details = new HashMap<String, String>();
        details.put("id", id);
        details.put("token", TelephoneUtil.getIMEI(this));
        return details;
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
