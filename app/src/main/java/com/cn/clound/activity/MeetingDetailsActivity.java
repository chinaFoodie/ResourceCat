package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.MeetingDetailsMenberRecyclerAdapter;
import com.cn.clound.adapter.MeetingDetailsTimeRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.metting.MeetingDetailsModel;
import com.cn.clound.easemob.db.InviteMessgeDao;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
import com.hyphenate.chat.EMClient;

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
    @Bind(R.id.rl_update_meeting)
    RelativeLayout rlUpdateMeeting;
    @Bind(R.id.rl_end_meeting)
    RelativeLayout rlEndMeeting;
    @Bind(R.id.ll_meeting_bottom)
    RelativeLayout llMeetingBottom;

    private int HTTP_QUERY_MEETING_DETAILS = 148;
    private int HTTP_DROPPED_MEETING = 161;
    private MyHttpHelper httpHelper;
    private String meetingId;
    private List<MeetingDetailsModel.MeetingDetails.DetailTime> listTimes = new ArrayList<>();
    private List<MeetingDetailsModel.MeetingDetails.DetailUser> listMenber = new ArrayList<>();
    private MeetingDetailsTimeRecyclerAdapter timeAdapter;
    private MeetingDetailsMenberRecyclerAdapter menberAdapter;
    private MeetingDetailsModel updateMdm = null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_QUERY_MEETING_DETAILS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    MeetingDetailsModel mdm = (MeetingDetailsModel) msg.obj;
                    updateMdm = mdm;
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
            } else if (msg.arg1 == HTTP_DROPPED_MEETING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    MeetingDetailsActivity.this.finish();
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
        rlUpdateMeeting.setOnClickListener(this);
        rlEndMeeting.setOnClickListener(this);
        tvMidTitle.setText("会议详情");
        String show = this.getIntent().getStringExtra("is_show_bottom");
        if (show.equals("show")) {
            llMeetingBottom.setVisibility(View.VISIBLE);
        } else {
            llMeetingBottom.setVisibility(View.GONE);
        }
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
            case R.id.rl_update_meeting:
                Intent update = new Intent(this, IssuedMettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("update_meeting_info", updateMdm);
                update.putExtra("meeting_id", meetingId);
                update.putExtras(bundle);
                startActivity(update);
                break;
            case R.id.rl_end_meeting:
                new AlertDialog(this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        httpHelper.postStringBack(HTTP_DROPPED_MEETING, AppConfig.DROPPED_MEETING, dropMeeting(), handler, BaseModel.class);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setMsg("是否确认终止会议？").show();
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> dropMeeting() {
        HashMap<String, String> drop = new HashMap<String, String>();
        drop.put("id", meetingId);
        drop.put("token", TelephoneUtil.getIMEI(this));
        return drop;
    }
}
