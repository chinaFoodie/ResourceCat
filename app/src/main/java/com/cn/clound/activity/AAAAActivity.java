package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.AAABottomChoseChatMenberAdapter;
import com.cn.clound.adapter.ActFragCoon;
import com.cn.clound.adapter.FragActCoon;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.chat.ChatRoomInfoModel;
import com.cn.clound.easemob.Constant;
import com.cn.clound.fragment.TopContactsFragment;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.ExtendedChatModel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/4/29.
 */
public class AAAAActivity extends BaseActivity implements FragActCoon, OnItemClickLitener, View.OnClickListener {
    @Bind(R.id.tv_chose_menber_count)
    TextView tvCount;
    @Bind(R.id.recycler_chat_menber)
    RecyclerView recyclerView;
    @Bind(R.id.ll_layout_bottom)
    LinearLayout llLayoutBottom;
    private TopContactsFragment topFragment;
    public static String otherUnitNo = "";
    private ActFragCoon actFragCoon;
    private int HTTP_CREATE_CHAT_ROOM = 119;
    private int HTTP_ADD_CHAT_MENBER = 123;
    private int HTTP_ADD_PUBLISH_PERSON = 150;
    private MyHttpHelper httpHelper;
    private String groupNo, groupName;
    private String hxIMGroupId;
    private AAABottomChoseChatMenberAdapter bottomAdapter;
    private CustomProgress progress;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_CREATE_CHAT_ROOM) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    AppConfig.CHAT_GROUP_REFUSER = "1";
                    AppConfig.listTemp.clear();
                    ChatRoomInfoModel crif = (ChatRoomInfoModel) msg.obj;
                    if (crif != null) {
                        Intent intent = new Intent(AAAAActivity.this, DtChatActivity.class);
                        ExtendedChatModel ecm = new ExtendedChatModel();
                        ecm.setMsgType(EaseConstant.CHATTYPE_GROUP + "");
                        ecm.setSessionName(crif.getData().getName());
                        ecm.setToUSerNick(crif.getData().getName());
                        ecm.setToUserNo(crif.getData().getImGroupId());
                        ecm.setToUserAvatar("");
                        ecm.setFromUserAvatar(MyApplication.getInstance().getUm().getData().getUserInfo().getHead());
                        ecm.setFromUserNick(MyApplication.getInstance().getUm().getData().getUserInfo().getName());
                        ecm.setFromUserNo(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("extended_chat_model", ecm);
                        intent.putExtras(bundle);
                        intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                        intent.putExtra("userId", crif.getData().getImGroupId());
                        startActivity(intent);
                        AAAAActivity.this.finish();
                    }
                } else {
                    Toastor.showToast(AAAAActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_ADD_CHAT_MENBER) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    AppConfig.listTemp.clear();
                    Intent intent = new Intent(AAAAActivity.this, DtChatActivity.class);
                    ExtendedChatModel ecm = new ExtendedChatModel();
                    ecm.setMsgType(EaseConstant.CHATTYPE_GROUP + "");
                    ecm.setSessionName(groupName);
                    ecm.setToUSerNick(groupName);
                    ecm.setToUserNo(hxIMGroupId);
                    ecm.setToUserAvatar("");
                    ecm.setFromUserAvatar(MyApplication.getInstance().getUm().getData().getUserInfo().getHead());
                    ecm.setFromUserNick(MyApplication.getInstance().getUm().getData().getUserInfo().getName());
                    ecm.setFromUserNo(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("extended_chat_model", ecm);
                    intent.putExtras(bundle);
                    intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    intent.putExtra("userId", hxIMGroupId);
                    startActivity(intent);
                    AAAAActivity.this.finish();
                } else {
                    Toastor.showToast(AAAAActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_ADD_PUBLISH_PERSON) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    AAAAActivity.this.setResult(1007);
                    AAAAActivity.this.finish();
                } else {
                    Toastor.showToast(AAAAActivity.this, msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.aaaa;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        progress = new CustomProgress(this, "请稍候...");
        httpHelper = MyHttpHelper.getInstance(this);
        topFragment = new TopContactsFragment();
        AppConfig.DEPT_MENBER_MUTIL = true;
        groupNo = this.getIntent().getStringExtra("groupNo");
        hxIMGroupId = this.getIntent().getStringExtra("hxIMGroupId");
        groupName = this.getIntent().getStringExtra("groupName");
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.chose_fragment_layout, topFragment);
        actFragCoon = (ActFragCoon) topFragment;
        transaction.commit();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (PublicDataUtil.listBottom.size() > 0) {
            llLayoutBottom.setVisibility(View.VISIBLE);
        } else {
            llLayoutBottom.setVisibility(View.GONE);
        }
        bottomAdapter = new AAABottomChoseChatMenberAdapter(this, PublicDataUtil.listBottom);
        recyclerView.setAdapter(bottomAdapter);
        bottomAdapter.setOnItemClickLitener(this);
        tvCount.setOnClickListener(this);
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
        PublicDataUtil.listHasCunZai.clear();
        PublicDataUtil.listBottom.clear();
    }

    @Override
    public void fragToAct(BaseFragment current, BaseFragment nxext) {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        actFragCoon = (ActFragCoon) nxext;
        trx.hide(current).add(R.id.chose_fragment_layout, nxext).show(nxext).commit();
    }

    @Override
    public void fragToAct(List<BottomUserModel> listBottom) {
        if (PublicDataUtil.listBottom.size() > 0) {
            llLayoutBottom.setVisibility(View.VISIBLE);
        } else {
            llLayoutBottom.setVisibility(View.GONE);
        }
        tvCount.setText("确定" + " (" + listBottom.size() + ")");
        bottomAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        int MIN_CLICK_DELAY_TIME = 1000;
        long lastClickTime = 0;
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            if (PublicDataUtil.listBottom.size() > 0) {
                PublicDataUtil.listBottom.remove(PublicDataUtil.listBottom.get(position));
                bottomAdapter.notifyDataSetChanged();
                actFragCoon.actToFrag(PublicDataUtil.listBottom);
                tvCount.setText("确定" + " (" + PublicDataUtil.listBottom.size() + ")");
            }
        } else {
            Toastor.showToast(this, "点击频率过快请重试");
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private static final String ACTION = "com.cn.szdt.SENDBROADCAST";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_chose_menber_count:
                String come = getIntent().getStringExtra("come_from_meeting");
                progress.show();
                sendBroadcast(new Intent(ACTION));
                if (null != groupNo && !"".equals(groupNo)) {
                    httpHelper.postStringBack(HTTP_ADD_CHAT_MENBER, AppConfig.ADD_CHAT_GROUP_MENBER, addParames(groupNo), handler, BaseModel.class);
                } else if (come != null) {
                    if (come.equals("meeting")) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("meeting_back_data", (Serializable) PublicDataUtil.listBottom);
                        intent.putExtras(bundle);
                        this.setResult(1006, intent);
                        this.finish();
                    } else {
                        //Todo 添加会议发布人
                        httpHelper.postStringBack(HTTP_ADD_PUBLISH_PERSON, AppConfig.ADD_MEETING_PUBLISH_PERSON, addPublish(), handler, BaseModel.class);
                    }
                } else {
                    httpHelper.postStringBack(HTTP_CREATE_CHAT_ROOM, AppConfig.CREATE_CHAT_ROOM, createParames(""), handler, ChatRoomInfoModel.class);
                }
                break;
        }
    }

    /**
     * 添加会议发布人
     *
     * @return
     */
    private HashMap<String, String> addPublish() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("users", getUsers(PublicDataUtil.listBottom));
        return parames;
    }

    /**
     * 添加群组成员
     *
     * @param groupNo
     * @return
     */
    private HashMap<String, String> addParames(String groupNo) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("groupNo", groupNo);
        parames.put("userNos", getUsers(PublicDataUtil.listBottom));
        return parames;
    }

    /**
     * 创建群组配置参数
     *
     * @return
     */
    private HashMap<String, String> createParames(String chatName) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("content", "");
        parames.put("groupName", chatName);
        if (getIntent().getStringExtra("single2group") != null) {
            BottomUserModel bum = (BottomUserModel) getIntent().getSerializableExtra("single2menber");
            PublicDataUtil.listBottom.add(bum);
        }
        parames.put("users", getUsers(PublicDataUtil.listBottom));
        return parames;
    }

    /**
     * @return
     */
    private String getUsers(List<BottomUserModel> list) {
        String users = "";
        for (BottomUserModel u : list) {
            users += (u.getUserId() + ",");
        }
        return users;
    }
}
