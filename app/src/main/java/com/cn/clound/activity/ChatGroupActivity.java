package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.ChatRoomAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.chat.ChatRoomModel;
import com.cn.clound.easemob.Constant;
import com.cn.clound.easemob.ui.ChatActivity;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.ExtendedChatModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 我的群聊
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-15 09:57:03
 */
public class ChatGroupActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.lv_org_list_view)
    ListView listView;
    @Bind(R.id.rl_create_chat_room)
    RelativeLayout rlCreateChatRoom;
    private List<ChatRoomModel.ChatRoom> list = new ArrayList<ChatRoomModel.ChatRoom>();
    private MyHttpHelper httpHelper;
    private int HTTP_GET_CHAT_ROOM_LIST = 118;

    private ChatRoomModel chatModel;
    private ChatRoomAdapter adapter;
    private CustomProgress progress;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_CHAT_ROOM_LIST) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    if (list != null && list.size() > 0) {
                        list.clear();
                    }
                    chatModel = (ChatRoomModel) msg.obj;
                    if (chatModel != null && chatModel.getData().getResult().size() > 0) {
                        list.addAll(chatModel.getData().getResult());
                        adapter = new ChatRoomAdapter(ChatGroupActivity.this, list);
                        listView.setAdapter(adapter);
                    }
                } else {
                    Toastor.showToast(ChatGroupActivity.this, msg.obj.toString());
                }
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_chat_group_layout;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化试图
     */
    private void init() {
        progress = new CustomProgress(this, "加载中...");
        progress.show();
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText(getResources().getString(R.string.man_contact_header_group));
        getHierarchy();
        rlCreateChatRoom.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    /**
     * 获取上下级组织关系
     */
    private void getHierarchy() {
        httpHelper.postStringBack(HTTP_GET_CHAT_ROOM_LIST, AppConfig.GET_CHAT_ROOM_LIST, setChatParames(), handler, ChatRoomModel.class);
    }

    /**
     * 获取群聊列表参数设置
     *
     * @return
     */
    private HashMap<String, String> setChatParames() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("pageNo", "1");
        parames.put("pageSize", "100000");
        return parames;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (AppConfig.CHAT_GROUP_REFUSER.equals("1")) {
            getHierarchy();
            AppConfig.CHAT_GROUP_REFUSER = "";
        }
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
                finish();
                break;
            case R.id.rl_create_chat_room:
                Intent intent = new Intent(this, CreateChatRoomActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ChatGroupActivity.this, DtChatActivity.class);
        ExtendedChatModel ecm = new ExtendedChatModel();
        ecm.setMsgType(EaseConstant.CHATTYPE_GROUP + "");
        ecm.setSessionName(list.get(position).getName());
        ecm.setToUSerNick(list.get(position).getImGroupId());
        ecm.setToUserNo(list.get(position).getGroupNo());
        ecm.setToUserAvatar("");
        ecm.setFromUserAvatar(MyApplication.getInstance().getUm().getData().getUserInfo().getHead());
        ecm.setFromUserNick(MyApplication.getInstance().getUm().getData().getUserInfo().getName());
        ecm.setFromUserNo(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
        Bundle bundle = new Bundle();
        bundle.putSerializable("extended_chat_model", ecm);
        intent.putExtras(bundle);
        intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
        intent.putExtra("userId", list.get(position).getImGroupId());
        startActivity(intent);
        this.finish();
    }
}
