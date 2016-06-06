package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.MeetingMenberRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.metting.IssuedMeetingModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.utils.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 发布会议添加参会人员界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-27 17:19:47
 */
public class IssuedMeetingAddMenberActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.ll_meeting_get_joined_menber)
    LinearLayout llJoinedMenber;
    @Bind(R.id.ll_get_meeting_hostess)
    LinearLayout llHostess;
    @Bind(R.id.recycler_joined_menber)
    RecyclerView recyclerMenber;
    @Bind(R.id.recycler_hostess)
    RecyclerView recyclerHostess;
    private List<BottomUserModel> listMenber = new ArrayList<BottomUserModel>();
    private List<BottomUserModel> listHostess = new ArrayList<BottomUserModel>();
    private IssuedMeetingModel imm;
    private MeetingMenberRecyclerAdapter menberAdapter;
    private MeetingMenberRecyclerAdapter hostessAdapter;
    private String flag = "";
    private int HTTP_CREATE_MEETING = 141;
    private int HTTP_UPDATE_MEETING = 160;
    private MyHttpHelper httpHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 101) {
                menberAdapter = new MeetingMenberRecyclerAdapter(IssuedMeetingAddMenberActivity.this, listMenber);
                recyclerMenber.setAdapter(menberAdapter);
            } else if (msg.what == 102) {
                hostessAdapter = new MeetingMenberRecyclerAdapter(IssuedMeetingAddMenberActivity.this, listHostess);
                recyclerHostess.setAdapter(hostessAdapter);
            } else if (msg.arg1 == HTTP_UPDATE_MEETING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(IssuedMeetingAddMenberActivity.this, "更新会议成功");
                    Intent manager = new Intent(IssuedMeetingAddMenberActivity.this, MeetingManagerActivity.class);
                    startActivity(manager);
                    IssuedMeetingAddMenberActivity.this.finish();
                } else {
                    Toastor.showToast(IssuedMeetingAddMenberActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_CREATE_MEETING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(IssuedMeetingAddMenberActivity.this, "发布会议成功");
                    Intent manager = new Intent(IssuedMeetingAddMenberActivity.this, MeetingManagerActivity.class);
                    startActivity(manager);
                    IssuedMeetingAddMenberActivity.this.finish();
                } else {
                    Toastor.showToast(IssuedMeetingAddMenberActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_issued_meeting_add_menber;
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
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setOnClickListener(this);
        flag = this.getIntent().getStringExtra("meeting_update");
        llJoinedMenber.setOnClickListener(this);
        llHostess.setOnClickListener(this);
        httpHelper = MyHttpHelper.getInstance(this);
        imm = (IssuedMeetingModel) this.getIntent().getSerializableExtra("meeting_add_menber_model");
        if (flag.equals("update")) {
            tvBaseRight.setText("更新");
            for (IssuedMeetingModel.IssuedMeeting.MeetingUser mu : imm.getData().getUsers()) {
                BottomUserModel bum = new BottomUserModel();
                bum.setUserId(mu.getUserId());
                bum.setUserName(mu.getName());
                bum.setUserHead("");
                if (mu.getmRole().equals("2")) {
                    listHostess.add(bum);
                } else {
                    listMenber.add(bum);
                }
            }
        } else {
            tvBaseRight.setText("发布");
        }
        DividerItemDecoration dividerVERTICAL = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        dividerVERTICAL.setSize(1);
        dividerVERTICAL.setColor(0xFFDDDDDD);
        DividerItemDecoration dividerHORIZONTAL = new DividerItemDecoration(DividerItemDecoration.HORIZONTAL);
        dividerHORIZONTAL.setSize(1);
        dividerHORIZONTAL.setColor(0xFFDDDDDD);
        recyclerMenber.addItemDecoration(dividerVERTICAL);
        recyclerMenber.addItemDecoration(dividerHORIZONTAL);
        recyclerMenber.setLayoutManager(new GridLayoutManager(this, 4));
        menberAdapter = new MeetingMenberRecyclerAdapter(this, listMenber);
        recyclerMenber.setAdapter(menberAdapter);

        hostessAdapter = new MeetingMenberRecyclerAdapter(this, listHostess);
        recyclerHostess.addItemDecoration(dividerVERTICAL);
        recyclerHostess.addItemDecoration(dividerHORIZONTAL);
        recyclerHostess.setLayoutManager(new GridLayoutManager(this, 4));
        hostessAdapter = new MeetingMenberRecyclerAdapter(this, listHostess);
        recyclerHostess.setAdapter(hostessAdapter);
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
        if (resultCode == 1006) {
            Bundle b = data.getExtras();
            List<IssuedMeetingModel.IssuedMeeting.MeetingUser> listmu = new ArrayList<>();
            PublicDataUtil.listBottom.clear();
            if (b != null) {
                if (requestCode == 6705) {
                    listMenber = (List<BottomUserModel>) b.getSerializable("meeting_back_data");
                    for (BottomUserModel bum : listMenber) {
                        IssuedMeetingModel.IssuedMeeting.MeetingUser mu = new IssuedMeetingModel().new IssuedMeeting().new MeetingUser();
                        mu.setmRole("1");
                        mu.setUserId(bum.getUserId());
                        listmu.add(mu);
                    }
                    if (imm.getData().getUsers() != null) {
                        imm.getData().getUsers().addAll(listmu);
                    } else {
                        imm.getData().setUsers(listmu);
                    }
                    handler.sendEmptyMessage(101);
                } else {
                    listHostess = (List<BottomUserModel>) b.getSerializable("meeting_back_data");
                    for (BottomUserModel bum : listHostess) {
                        IssuedMeetingModel.IssuedMeeting.MeetingUser mu = new IssuedMeetingModel().new IssuedMeeting().new MeetingUser();
                        mu.setmRole("2");
                        mu.setUserId(bum.getUserId());
                        listmu.add(mu);
                    }
                    if (imm.getData().getUsers() != null) {
                        imm.getData().getUsers().addAll(listmu);
                    } else {
                        imm.getData().setUsers(listmu);
                    }
                    handler.sendEmptyMessage(102);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            case R.id.tv_base_right:
                if (flag.equals("update")) {
                    httpHelper.postStringBack(HTTP_UPDATE_MEETING, AppConfig.UPDATE_MEETING, updateMeeting(), handler, BaseModel.class);
                } else {
                    httpHelper.postStringBack(HTTP_CREATE_MEETING, AppConfig.CREATE_MEETING, createMeeting(), handler, BaseModel.class);
                }
                break;
            case R.id.ll_meeting_get_joined_menber:
                for (BottomUserModel bum : listHostess) {
                    BottomUserModel bb = new BottomUserModel();
                    bb.setUserId(bum.getUserId());
                    bb.setUserName(bum.getUserName());
                    bb.setUserHead(bum.getUserHead());
                    PublicDataUtil.listHasCunZai.add(bb);
                }
                for (BottomUserModel bum : listMenber) {
                    BottomUserModel bb = new BottomUserModel();
                    bb.setUserId(bum.getUserId());
                    bb.setUserName(bum.getUserName());
                    bb.setUserHead(bum.getUserHead());
                    PublicDataUtil.listBottom.add(bb);
                }
                startActivityForResult(new Intent(this, AAAAActivity.class).putExtra("come_from_meeting", "meeting"), 6705);
                break;
            case R.id.ll_get_meeting_hostess:
                for (BottomUserModel bum : listMenber) {
                    BottomUserModel bb = new BottomUserModel();
                    bb.setUserId(bum.getUserId());
                    bb.setUserName(bum.getUserName());
                    bb.setUserHead(bum.getUserHead());
                    PublicDataUtil.listHasCunZai.add(bb);
                }
                for (BottomUserModel bum : listHostess) {
                    BottomUserModel bb = new BottomUserModel();
                    bb.setUserId(bum.getUserId());
                    bb.setUserName(bum.getUserName());
                    bb.setUserHead(bum.getUserHead());
                    PublicDataUtil.listBottom.add(bb);
                }
                startActivityForResult(new Intent(this, AAAAActivity.class).putExtra("come_from_meeting", "meeting"), 6706);
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> updateMeeting() {
        HashMap<String, String> create = new HashMap<String, String>();
        create.put("token", TelephoneUtil.getIMEI(this));
        String data = "";
        for (int i = 0; i < imm.getData().getUsers().size(); i++) {
            imm.getData().getUsers().get(i).setName(null);
        }
        try {
            JSONObject json = new JSONObject(GsonTools.obj2json(imm));
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        create.put("data", data);
        return create;
    }

    private HashMap<String, String> createMeeting() {
        HashMap<String, String> create = new HashMap<String, String>();
        create.put("token", TelephoneUtil.getIMEI(this));
        String data = "";
        try {
            JSONObject json = new JSONObject(GsonTools.obj2json(imm));
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        create.put("data", data);
        return create;
    }
}
