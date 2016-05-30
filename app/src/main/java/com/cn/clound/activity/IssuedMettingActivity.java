package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.MeetingTimeRecyclerAdapter;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.bean.metting.IssuedMeetingModel;
import com.cn.clound.bean.metting.MeetingTimeExpandModel;
import com.cn.clound.bean.metting.MeetingTimeModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 会议发布界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-24 11:51:24
 */
public class IssuedMettingActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_left)
    LinearLayout llleft;
    @Bind(R.id.tv_base_left)
    TextView tvBaseLeft;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.ll_issued_meeting_date)
    LinearLayout llIssuedMeetingDate;
    @Bind(R.id.et_issued_meeting_name)
    EditText etMeetingName;
    @Bind(R.id.et_issued_meeting_desc)
    EditText etMeetingDesc;
    @Bind(R.id.recycler_meeting_time)
    RecyclerView recyclerView;
    @Bind(R.id.img_issued_meeting_net)
    ImageView imgNetMeeting;
    @Bind(R.id.img_issued_meeting_off_line)
    ImageView imgOffMeeting;
    @Bind(R.id.img_issued_dis)
    ImageView imgMeetingDis;
    @Bind(R.id.img_issued_not_dis)
    ImageView imgMeetingNotDis;
    private MeetingTimeRecyclerAdapter expandAdapter;
    private int GET_RESULT = 6704;
    private List<MeetingTimeModel> listData = new ArrayList<MeetingTimeModel>();
    private String meetingType, whetherDis;// 1

    private IssuedMeetingModel imm;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 101) {
                expandAdapter = new MeetingTimeRecyclerAdapter(IssuedMettingActivity.this, listData);
                recyclerView.setAdapter(expandAdapter);
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_issued_metting;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        tvMidTitle.setText("会议发布");
        llleft.setVisibility(View.VISIBLE);
        llleft.setOnClickListener(this);
        tvBaseLeft.setText("取消");
        tvBaseRight.setText("下一步");
        meetingType = "1";//网络会议
        whetherDis = "0";//不讨论

        if (meetingType.equals("1")) {
            imgNetMeeting.setImageResource(R.mipmap.img_no_choose);
            imgOffMeeting.setImageResource(R.mipmap.img_choosed);
        } else {
            imgOffMeeting.setImageResource(R.mipmap.img_no_choose);
            imgNetMeeting.setImageResource(R.mipmap.img_choosed);
        }
        if (!whetherDis.equals("0")) {
            imgMeetingDis.setImageResource(R.mipmap.img_choosed);
            imgMeetingNotDis.setImageResource(R.mipmap.img_no_choose);
        } else {
            imgMeetingDis.setImageResource(R.mipmap.img_no_choose);
            imgMeetingNotDis.setImageResource(R.mipmap.img_choosed);
        }

        Editable ea = etMeetingName.getText();
        etMeetingName.setSelection(ea.length());
        tvBaseRight.setVisibility(View.VISIBLE);

        tvBaseRight.setOnClickListener(this);
        llIssuedMeetingDate.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imgMeetingDis.setOnClickListener(this);
        imgMeetingNotDis.setOnClickListener(this);
        imgNetMeeting.setOnClickListener(this);
        imgOffMeeting.setOnClickListener(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GET_RESULT == requestCode && resultCode == 1005) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                List<MeetingTimeExpandModel> listExpand = (List<MeetingTimeExpandModel>) bundle.getSerializable("meeting_time_list");
                for (int i = 0; i < listExpand.size(); i++) {
                    MeetingTimeModel mte = new MeetingTimeModel();
                    mte.setMeetingTime(listExpand.get(i).getMeetingTime());
                    mte.setMeetingDate(listExpand.get(i).getMeetingDate());
                    mte.setMeetingUpdate(listExpand.get(i).getMeetingUpdate());
                    listData.add(mte);
                    if (listExpand.get(i).getTimeList() != null && listExpand.get(i).getTimeList().size() > 0) {
                        for (int j = 0; j < listExpand.get(i).getTimeList().size(); j++) {
                            MeetingTimeModel mt1 = new MeetingTimeModel();
                            mt1.setMeetingTime(listExpand.get(i).getTimeList().get(j).getTime());
                            mt1.setMeetingDate(listExpand.get(i).getTimeList().get(j).getDate());
                            mt1.setMeetingUpdate(listExpand.get(i).getTimeList().get(j).getUpdate());
                            listData.add(mt1);
                        }
                    }
                }
                handler.sendEmptyMessage(101);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_left:
                this.finish();
                break;
            case R.id.ll_issued_meeting_date:
                startActivityForResult(new Intent(this, ChooseMeetingDateActivity.class), GET_RESULT);
                break;
            case R.id.tv_base_right:
                imm = new IssuedMeetingModel();
                IssuedMeetingModel.IssuedMeeting im = new IssuedMeetingModel().new IssuedMeeting();
                IssuedMeetingModel.IssuedMeeting.Meeting meeting = new IssuedMeetingModel().new IssuedMeeting().new Meeting();
                meeting.setmDescription(etMeetingDesc.getText().toString());
                meeting.setmIsSpeak(whetherDis);
                meeting.setmName(etMeetingName.getText().toString());
                meeting.setmType(meetingType);
                List<IssuedMeetingModel.IssuedMeeting.MeetingTime> listMt = new ArrayList<>();
                for (MeetingTimeModel mtm : listData) {
                    IssuedMeetingModel.IssuedMeeting.MeetingTime mt = new IssuedMeetingModel().new IssuedMeeting().new MeetingTime();
                    mt.setBeginAt("2016-" + mtm.getMeetingDate() + " " + mtm.getMeetingTime().substring(0, mtm.getMeetingTime().indexOf("-")));
                    mt.setEndAt("2016-" + mtm.getMeetingDate() + " " + mtm.getMeetingTime().substring(mtm.getMeetingTime().indexOf("-") + 1));
                    listMt.add(mt);
                }
                im.setTimes(listMt);
                im.setMeeting(meeting);
                imm.setData(im);
                Intent addMenber = new Intent(this, IssuedMeetingAddMenberActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("meeting_add_menber_model", imm);
                addMenber.putExtras(bundle);
                startActivity(addMenber);
                break;
            case R.id.img_issued_dis:
                whetherDis = "1";
                imgMeetingDis.setImageResource(R.mipmap.img_no_choose);
                imgMeetingNotDis.setImageResource(R.mipmap.img_choosed);
                break;
            case R.id.img_issued_not_dis:
                whetherDis = "0";
                imgMeetingNotDis.setImageResource(R.mipmap.img_no_choose);
                imgMeetingDis.setImageResource(R.mipmap.img_choosed);
                break;
            case R.id.img_issued_meeting_net:
                meetingType = "1";
                imgNetMeeting.setImageResource(R.mipmap.img_no_choose);
                imgOffMeeting.setImageResource(R.mipmap.img_choosed);
                break;
            case R.id.img_issued_meeting_off_line:
                meetingType = "2";
                imgOffMeeting.setImageResource(R.mipmap.img_no_choose);
                imgNetMeeting.setImageResource(R.mipmap.img_choosed);
                break;
            default:
                break;
        }
    }
}
