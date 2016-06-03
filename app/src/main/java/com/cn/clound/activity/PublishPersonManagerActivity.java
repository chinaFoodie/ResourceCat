package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.adapter.PublishMeetingPersonRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.metting.MeetingPublishPersonModel;
import com.cn.clound.easemob.db.InviteMessgeDao;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
import com.cn.clound.view.CustomProgress;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 会议发布人管理界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-1 14:57:29
 */
public class PublishPersonManagerActivity extends BaseActivity implements View.OnClickListener, OnItemClickLitener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.recycler_publish_manager_person)
    RecyclerView recyclerView;
    @Bind(R.id.ll_add_publish_manager_person)
    LinearLayout llAddPublishPerson;

    private MyHttpHelper httpHelper;
    private int HTTP_GET_PUBLISH_MEETING_MANAGER_PERSON = 149;
    private int HTTP_DEL_PUBLISH_MEETING_MANAGER_PERSON = 153;
    private List<MeetingPublishPersonModel.MeetingPublishPerson> listPerson = new ArrayList<>();
    private PublishMeetingPersonRecyclerAdapter adapter;
    private CustomProgress progress;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_PUBLISH_MEETING_MANAGER_PERSON) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    MeetingPublishPersonModel mppm = (MeetingPublishPersonModel) msg.obj;
                    if (mppm != null) {
                        listPerson = mppm.getData();
                        adapter = new PublishMeetingPersonRecyclerAdapter(PublishPersonManagerActivity.this, listPerson, handler);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickLitener(PublishPersonManagerActivity.this);
                    }
                } else {
                    Toastor.showToast(PublishPersonManagerActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_DEL_PUBLISH_MEETING_MANAGER_PERSON) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    httpHelper.postStringBack(HTTP_GET_PUBLISH_MEETING_MANAGER_PERSON, AppConfig.GET_PUBLISH_MEETING_PERSON, getPerson(), handler, MeetingPublishPersonModel.class);
                } else {
                    Toastor.showToast(PublishPersonManagerActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 1002) {
                final int index = (int) msg.obj;
                new AlertDialog(PublishPersonManagerActivity.this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress.show();
                        httpHelper.postStringBack(HTTP_DEL_PUBLISH_MEETING_MANAGER_PERSON, AppConfig.DEL_MEETING_PUBLISH_PERSON, delParmeas(listPerson.get(index).getUserNo()), handler, BaseModel.class);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setMsg("确认删除？").show();
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    private HashMap<String, String> delParmeas(String userId) {
        HashMap<String, String> del = new HashMap<String, String>();
        del.put("token", TelephoneUtil.getIMEI(this));
        del.put("userId", userId);
        return del;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_publish_person_manager;
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
        progress = new CustomProgress(this, "加载中...");
        progress.show();
        llBack.setVisibility(View.VISIBLE);
        tvMidTitle.setText("发布人管理");
        llBack.setOnClickListener(this);
        llAddPublishPerson.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        httpHelper.postStringBack(HTTP_GET_PUBLISH_MEETING_MANAGER_PERSON, AppConfig.GET_PUBLISH_MEETING_PERSON, getPerson(), handler, MeetingPublishPersonModel.class);
    }

    private HashMap<String, String> getPerson() {
        HashMap<String, String> getPerson = new HashMap<String, String>();
        getPerson.put("token", TelephoneUtil.getIMEI(this));
        return getPerson;
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
        if (requestCode == 6707 && resultCode == 1007) {
            httpHelper.postStringBack(HTTP_GET_PUBLISH_MEETING_MANAGER_PERSON, AppConfig.GET_PUBLISH_MEETING_PERSON, getPerson(), handler, MeetingPublishPersonModel.class);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            case R.id.ll_add_publish_manager_person:
                startActivityForResult(new Intent(this, AAAAActivity.class).putExtra("come_from_meeting", "meeting_publish"), 6707);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ContactsDetailsActivity.class);
        intent.putExtra("user_no", listPerson.get(position).getUserNo());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
