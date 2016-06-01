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
import com.cn.clound.adapter.MeetingDetailsMenberRecyclerAdapter;
import com.cn.clound.adapter.MeetingDetailsTimeRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.metting.MeetingDetailsModel;
import com.cn.clound.http.MyHttpHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 会议详情界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-30 17:48:02
 */
public class MeetingDetailsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.recycler_details_meeting_times)
    RecyclerView recyclerViewTimes;
    @Bind(R.id.recycler_joined_menber)
    RecyclerView recyclerViewMenber;
    @Bind(R.id.tv_meeting_details_name)
    TextView tvMeetingName;
    @Bind(R.id.tv_meeting_details_type)
    TextView tvMeetingType;
    @Bind(R.id.tv_meeting_dis_value)
    TextView tvMeetingDis;
    @Bind(R.id.tv_meeting_sign_type_value)
    TextView tvMeetingSignType;

    private int HTTP_QUERY_MEETING_DETAILS = 148;
    private MyHttpHelper httpHelper;
    private String meetingId;
    private List<MeetingDetailsModel.MeetingDetails.DetailTime> listTimes = new ArrayList<>();
    private List<MeetingDetailsModel.MeetingDetails.DetailUser> listMenber = new ArrayList<>();
    private MeetingDetailsTimeRecyclerAdapter timeAdapter;
    private MeetingDetailsMenberRecyclerAdapter menberAdapter;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_QUERY_MEETING_DETAILS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    MeetingDetailsModel mdm = (MeetingDetailsModel) msg.obj;
                    if (mdm != null) {
                        listTimes = mdm.getData().getTimes();
                        timeAdapter = new MeetingDetailsTimeRecyclerAdapter(MeetingDetailsActivity.this, listTimes);
                        recyclerViewTimes.setAdapter(timeAdapter);
                        listMenber = mdm.getData().getUsers();
                        menberAdapter = new MeetingDetailsMenberRecyclerAdapter(MeetingDetailsActivity.this, listMenber);
                        recyclerViewMenber.setAdapter(menberAdapter);
                        tvMeetingName.setText(mdm.getData().getName());
                        tvMeetingType.setText(mdm.getData().getTypeStr());
                        tvMeetingDis.setText(mdm.getData().getCanTalk().equals("0") ? "不允许" : "允许");
                        tvMeetingSignType.setText(mdm.getData().getSignTypeStr());
                    }
                } else {
                    Toastor.showToast(MeetingDetailsActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_meeting_details;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText("会议详情");
        recyclerViewTimes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMenber.setLayoutManager(new GridLayoutManager(this, 5));
        meetingId = this.getIntent().getStringExtra("meeting_id");
        httpHelper.postStringBack(HTTP_QUERY_MEETING_DETAILS, AppConfig.QUERY_MEETING_DETAILS, detailsParmeas(meetingId), handler, MeetingDetailsModel.class);
    }

    private HashMap<String, String> detailsParmeas(String id) {
        HashMap<String, String> details = new HashMap<String, String>();
        details.put("id", id);
        details.put("token", TelephoneUtil.getIMEI(this));
        return details;
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
