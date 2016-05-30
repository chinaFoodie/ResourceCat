package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.DeleteChatMenberAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.view.SwipeMenuRecyclerView;
import com.cn.clound.base.view.VerticalSpaceItemDecoration;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.chat.ChatMenberModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 群成员列表
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-5 15:34:29
 */
public class ChatGroupMenberActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.menber_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.menber_listView)
    SwipeMenuRecyclerView swipeMenuListView;
    @Bind(R.id.tv_base_right)
    TextView tvRight;
    private String groupNo = "";
    private String chooseModel;
    private DeleteChatMenberAdapter adapter;
    private int HTTP_GET_CHAT_MENBER = 124;
    private int HTTP_DEL_CHAT_MENBER = 125;
    private MyHttpHelper httpHelper;
    public static List<String> listDel = new ArrayList<String>();
    private List<ChatMenberModel.ChatMenberData.ChatMenber> listMenber;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_CHAT_MENBER) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    ChatMenberModel c = (ChatMenberModel) msg.obj;
                    listMenber = c.getData().getResult();
                    adapter = new DeleteChatMenberAdapter(ChatGroupMenberActivity.this, listMenber, chooseModel, handler);
                    swipeMenuListView.setAdapter(adapter);
                } else {
                    Toastor.showToast(ChatGroupMenberActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_DEL_CHAT_MENBER) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    ChatGroupMenberActivity.this.setResult(1002);
                    ChatGroupMenberActivity.this.finish();
                } else {
                    Toastor.showToast(ChatGroupMenberActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 1000) {
                httpHelper.postStringBack(HTTP_DEL_CHAT_MENBER, AppConfig.DEL_CHAT_GROUP_MENBER, delParames(groupNo, msg.obj.toString()), handler, BaseModel.class);
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_chat_group_menber;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        if (listDel != null) {
            listDel.clear();
        }
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText("群成员");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(this);
        groupNo = this.getIntent().getStringExtra("groupNo");
        chooseModel = this.getIntent().getStringExtra("chooseModel");
        if ("1".equals(chooseModel)) {
            tvRight.setText("删除");
        } else {
            tvRight.setText("添加");
        }
        httpHelper = MyHttpHelper.getInstance(this);
        swipeMenuListView.setLayoutManager(new LinearLayoutManager(this));
        swipeMenuListView.addItemDecoration(new VerticalSpaceItemDecoration(1));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
            }
        });
        httpHelper.postStringBack(HTTP_GET_CHAT_MENBER, AppConfig.QUERY_CHAT_MENBER_BY_GROUP_NO, menberParames(groupNo), handler, ChatMenberModel.class);
    }

    private HashMap<String, String> menberParames(String groupNo) {
        HashMap<String, String> menber = new HashMap<String, String>();
        menber.put("token", TelephoneUtil.getIMEI(this));
        menber.put("groupNo", groupNo);
        menber.put("pageSize", "100000");
        menber.put("pageNo", "1");
        return menber;
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
        if (requestCode == 6703 && resultCode == 1003) {
            httpHelper.postStringBack(HTTP_GET_CHAT_MENBER, AppConfig.QUERY_CHAT_MENBER_BY_GROUP_NO, menberParames(groupNo), handler, ChatMenberModel.class);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                finish();
                break;
            case R.id.tv_base_right:
                if ("1".equals(chooseModel)) {
                    //Todo 删除成员
                    new AlertDialog(this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listDel.size() > 0) {
                                httpHelper.postStringBack(HTTP_DEL_CHAT_MENBER, AppConfig.DEL_CHAT_GROUP_MENBER, delParames(groupNo, getUsers(listDel)), handler, BaseModel.class);
                            } else {
                                Toastor.showToast(ChatGroupMenberActivity.this, "无成员操作");
                            }
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).setMsg("是否确认删以下成员？").show();
                } else {
                    //Todo 添加
                    AppConfig.listTemp.clear();
                    if (PublicDataUtil.listBottom != null) {
                        PublicDataUtil.listBottom.clear();
                    }
                    for (ChatMenberModel.ChatMenberData.ChatMenber cm : listMenber) {
                        BottomUserModel bum = new BottomUserModel();
                        bum.setUserHead(cm.getHead());
                        bum.setUserId(cm.getUserNo());
                        PublicDataUtil.listHasCunZai.add(bum);
                    }
                    Intent addMenber = new Intent(ChatGroupMenberActivity.this, AAAAActivity.class);
                    addMenber.putExtra("groupNo", groupNo);
                    startActivityForResult(addMenber, 6703);
                }
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> delParames(String groupNo, String userNos) {
        HashMap<String, String> delParames = new HashMap<String, String>();
        delParames.put("groupNo", groupNo);
        delParames.put("token", TelephoneUtil.getIMEI(this));
        delParames.put("userNos", userNos);
        return delParames;
    }

    /**
     * @return
     */
    private String getUsers(List<String> list) {
        String users = "";
        for (String u : list) {
            users += (u + ",");
        }
        return users;
    }
}
