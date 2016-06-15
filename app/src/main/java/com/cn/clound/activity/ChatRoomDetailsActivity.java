package com.cn.clound.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.clound.R;
import com.cn.clound.adapter.ChatMenberRecyclerAdapter;
import com.cn.clound.interfaces.OnItemClickLitener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.SharePreferceUtil;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.User.UserModel;
import com.cn.clound.bean.chat.ChatMenberModel;
import com.cn.clound.bean.chat.ChatRoomInfoModel;
import com.cn.clound.easemob.Constant;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
import com.cn.clound.view.CustomProgress;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.model.ExtendedChatModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 聊天详情界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-3 14:52:31
 */
public class ChatRoomDetailsActivity extends BaseActivity implements View.OnClickListener, OnItemClickLitener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.grid_view_chat_room_menber)
    RecyclerView gvChatMenber;
    @Bind(R.id.tv_all_menber_count)
    TextView tvMenberCount;
    @Bind(R.id.img_msg_dont_load)
    ImageView imgMsgDont;
    @Bind(R.id.img_session_save)
    ImageView imgSessionSave;
    @Bind(R.id.rl_clear_history)
    RelativeLayout rlClear;
    @Bind(R.id.rl_all_chat_menber)
    RelativeLayout rlAllMenber;
    @Bind(R.id.rl_char_group_name)
    RelativeLayout rlChatGroupName;
    @Bind(R.id.tv_char_group_name_value)
    TextView tvChatGroupName;
    @Bind(R.id.rl_exit_group)
    RelativeLayout rlExite;
    @Bind(R.id.tv_exit_group)
    TextView tvExitGroup;
    @Bind(R.id.rl_save_to_contact)
    RelativeLayout rlSaveContacts;
    private boolean saveSession = false;
    private boolean msgLoad = false;
    private String groupNo = "";
    private int chatType;
    private String hxIMGroupId = "";
    private ChatMenberRecyclerAdapter adapter;
    private MyHttpHelper httpHelper;
    private int HTTP_GET_CHAT_GROUP_INFO = 120;
    private int HTTP_GET_CHAT_GROUP_MENBER = 121;
    private int HTTP_SAVE_TO_CONTACTS = 122;
    private int HTTP_GET_USER_INFO_HXID = 127;
    private int HTTP_EXIT_GROUP = 128;
    private int HTTP_DEL_GROUP = 129;
    private ChatRoomInfoModel chatInfo;
    private CustomProgress progress;
    private List<ChatMenberModel.ChatMenberData.ChatMenber> listMenber;
    private SharePreferceUtil share;
    private ExtendedChatModel extendedChatModel;
    private boolean isCreate = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_CHAT_GROUP_INFO) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    chatInfo = (ChatRoomInfoModel) msg.obj;
                    tvChatGroupName.setText(chatInfo.getData().getName());
                    groupNo = chatInfo.getData().getGroupNo();
                    if (chatInfo.getData().getOffTongxunlu().equals("1")) {
                        imgSessionSave.setImageResource(R.mipmap.img_switch_open);
                        saveSession = true;
                    } else {
                        imgSessionSave.setImageResource(R.mipmap.img_switch_off);
                        saveSession = false;
                    }
                    httpHelper.postStringBack(HTTP_GET_CHAT_GROUP_MENBER, AppConfig.QUERY_CHAT_MENBER_BY_GROUP_NO, menberParames(chatInfo.getData().getGroupNo()), handler, ChatMenberModel.class);
                }
            } else if (msg.arg1 == HTTP_GET_CHAT_GROUP_MENBER) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    ChatMenberModel c = (ChatMenberModel) msg.obj;
                    listMenber = c.getData().getResult();
                    for (ChatMenberModel.ChatMenberData.ChatMenber cm : listMenber) {
                        if (cm.getUserNo().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo())) {
                            if (cm.getIsCreate().equals("1")) {
                                isCreate = true;
                                tvExitGroup.setText("解散此群");
                            } else {
                                isCreate = false;
                            }
                        }
                    }
                    List<ChatMenberModel.ChatMenberData.ChatMenber> temp = new ArrayList<ChatMenberModel.ChatMenberData.ChatMenber>();
                    if (listMenber.size() > 14 && isCreate) {
                        temp = listMenber.subList(0, 14);
                    } else if (listMenber.size() > 15) {
                        temp = listMenber.subList(0, 15);
                    } else {
                        temp = listMenber;
                    }
                    adapter = new ChatMenberRecyclerAdapter(ChatRoomDetailsActivity.this, temp, isCreate);
                    gvChatMenber.setAdapter(adapter);
                    rlAllMenber.setVisibility(View.VISIBLE);
                    rlChatGroupName.setVisibility(View.VISIBLE);
                    rlSaveContacts.setVisibility(View.VISIBLE);
                    rlExite.setVisibility(View.VISIBLE);
                    tvMenberCount.setText("全体成员(" + listMenber.size() + ")");
                    adapter.setOnItemClickLitener(ChatRoomDetailsActivity.this);
                }
            } else if (msg.arg1 == HTTP_GET_USER_INFO_HXID) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    UserModel um = (UserModel) msg.obj;
                    if (um != null) {
                        rlAllMenber.setVisibility(View.GONE);
                        rlChatGroupName.setVisibility(View.GONE);
                        rlSaveContacts.setVisibility(View.GONE);
                        rlExite.setVisibility(View.GONE);
                        listMenber = new ArrayList<ChatMenberModel.ChatMenberData.ChatMenber>();
                        List<ChatMenberModel.ChatMenberData.ChatMenber> singleMenber = new ArrayList<ChatMenberModel.ChatMenberData.ChatMenber>();
                        ChatMenberModel.ChatMenberData.ChatMenber cm = new ChatMenberModel().new ChatMenberData().new ChatMenber();
                        cm.setHead(um.getData().getUserInfo().getHead());
                        cm.setIsCreate("0");
                        cm.setName(um.getData().getUserInfo().getName());
                        cm.setUserNo(um.getData().getUserInfo().getUserNo());
                        listMenber.add(cm);
                        adapter = new ChatMenberRecyclerAdapter(ChatRoomDetailsActivity.this, listMenber, false);
                        gvChatMenber.setAdapter(adapter);
                        adapter.setOnItemClickLitener(ChatRoomDetailsActivity.this);
                    }
                } else {
                    Toastor.showToast(ChatRoomDetailsActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_SAVE_TO_CONTACTS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    if (!saveSession) {
                        imgSessionSave.setImageResource(R.mipmap.img_switch_open);
                    } else {
                        imgSessionSave.setImageResource(R.mipmap.img_switch_off);
                    }
                    saveSession = !saveSession;
                } else {
                    Toastor.showToast(ChatRoomDetailsActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_EXIT_GROUP) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Intent intent = new Intent(ChatRoomDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                    ChatRoomDetailsActivity.this.finish();
                } else {
                    Toastor.showToast(ChatRoomDetailsActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_DEL_GROUP) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Intent intent = new Intent(ChatRoomDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                    ChatRoomDetailsActivity.this.finish();
                } else {
                    Toastor.showToast(ChatRoomDetailsActivity.this, msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    private HashMap<String, String> menberParames(String groupNo) {
        HashMap<String, String> menber = new HashMap<String, String>();
        menber.put("token", TelephoneUtil.getIMEI(this));
        menber.put("groupNo", groupNo);
        menber.put("pageSize", "100000");
        menber.put("pageNo", "1");
        return menber;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_chat_room_details;
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
        share = SharePreferceUtil.getInstance(this);
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        rlClear.setOnClickListener(this);
        tvMidTitle.setText("聊天详情");
        if (share.getBoolean(groupNo)) {
            imgMsgDont.setImageResource(R.mipmap.img_switch_open);
        } else {
            imgMsgDont.setImageResource(R.mipmap.img_switch_off);
        }
        imgMsgDont.setOnClickListener(this);
        imgSessionSave.setOnClickListener(this);
        rlAllMenber.setOnClickListener(this);
        rlChatGroupName.setOnClickListener(this);
        rlExite.setOnClickListener(this);
        Intent pre = this.getIntent();
        gvChatMenber.setLayoutManager(new GridLayoutManager(this, 4));
        hxIMGroupId = pre.getStringExtra("imGroupNo");
        extendedChatModel = (ExtendedChatModel) pre.getSerializableExtra("extended_chat_model");
        chatType = pre.getIntExtra(Constant.EXTRA_CHAT_TYPE, 0);
        if (chatType != Constant.CHATTYPE_SINGLE) {//群聊聊天详情界面
            getChatInfo(hxIMGroupId);
        } else {
            if (extendedChatModel.getFromUserNo().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo())) {
                getUserInfo(extendedChatModel.getToUserNo());
            } else {
                getUserInfo(extendedChatModel.getFromUserNo());
            }
        }
    }

    private void getUserInfo(String hxIMGroupId) {
        httpHelper.postStringBack(HTTP_GET_USER_INFO_HXID, AppConfig.GET_USER_INFO, userParames(hxIMGroupId), handler, UserModel.class);
    }

    /**
     * 获取单聊信息
     *
     * @param hxIMGroupId
     * @return
     */
    private HashMap<String, String> userParames(String hxIMGroupId) {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("userNo", hxIMGroupId);
        user.put("token", TelephoneUtil.getIMEI(this));
        return user;
    }

    /***
     * 获取群聊信息
     *
     * @param imGroupNo
     */
    private void getChatInfo(String imGroupNo) {
        httpHelper.postStringBack(HTTP_GET_CHAT_GROUP_INFO, AppConfig.QUERY_CHAT_GROUP_BY_HXNO, getChatParames(imGroupNo), handler, ChatRoomInfoModel.class);
    }

    private HashMap<String, String> getChatParames(String imGroupNo) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("imGroupNo", imGroupNo);
        parames.put("token", TelephoneUtil.getIMEI(this));
        return parames;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (AppConfig.BACK_SHUA_XIN) {
            if (chatType != Constant.CHATTYPE_SINGLE) {
                getChatInfo(hxIMGroupId);
            } else {
                if (extendedChatModel.getFromUserNo().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo())) {
                    getUserInfo(extendedChatModel.getToUserNo());
                } else {
                    getUserInfo(extendedChatModel.getFromUserNo());
                }
            }
            AppConfig.BACK_SHUA_XIN = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.cn.szdt.SENDBROADCAST");
        registerReceiver(destroyedBroadCast, filter);
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
        unregisterReceiver(destroyedBroadCast);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            case R.id.img_msg_dont_load:
                if (!msgLoad) {
                    imgMsgDont.setImageResource(R.mipmap.img_switch_open);
                } else {
                    imgMsgDont.setImageResource(R.mipmap.img_switch_off);
                }
                msgLoad = !msgLoad;
                share.setCache(groupNo, msgLoad);
                break;
            case R.id.img_session_save:
                save2Contacts(saveSession);
                break;
            case R.id.rl_clear_history:
                new AlertDialog(this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearGroupHistory();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setMsg("是否确认清空消息记录？").show();
                break;
            case R.id.rl_all_chat_menber:
                Intent delIntent = new Intent();
                delIntent.putExtra("groupNo", groupNo);
                delIntent.putExtra("chooseModel", "0");
                delIntent.setClass(this, ChatGroupMenberActivity.class);
                startActivityForResult(delIntent, 6702);
                break;
            case R.id.rl_char_group_name:
                startActivityForResult(new Intent(ChatRoomDetailsActivity.this, CharGroupNameActivity.class).putExtra("groupImNo", chatInfo.getData().getImGroupId()).putExtra("groupNo", groupNo).putExtra("groupName", tvChatGroupName.getText().toString()), 6702);
                break;
            case R.id.rl_exit_group:
                if (!isCreate) {
                    new AlertDialog(this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            httpHelper.postStringBack(HTTP_EXIT_GROUP, AppConfig.EXIT_GROUP, exitGroup(), handler, BaseModel.class);
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).setMsg("是否确认退出此群？").show();
                } else {
                    new AlertDialog(this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            httpHelper.postStringBack(HTTP_DEL_GROUP, AppConfig.DELETE_GROUP, exitGroup(), handler, BaseModel.class);
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).setMsg("是否确认解散此群？").show();
                }
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> exitGroup() {
        HashMap<String, String> exitParames = new HashMap<String, String>();
        exitParames.put("groupNo", groupNo);
        exitParames.put("token", TelephoneUtil.getIMEI(this));
        return exitParames;
    }

    private void save2Contacts(boolean saveSession) {
        httpHelper.postStringBack(HTTP_SAVE_TO_CONTACTS, AppConfig.IS_SAVE_TO_CONTACTS, isSaveParames(saveSession), handler, BaseModel.class);
    }

    private HashMap<String, String> isSaveParames(boolean saveSession) {
        HashMap<String, String> saveParames = new HashMap<String, String>();
        saveParames.put("groupNo", groupNo);
        if (!saveSession) {
            saveParames.put("state", "1");
        } else {
            saveParames.put("state", "-1");
        }
        saveParames.put("token", TelephoneUtil.getIMEI(this));
        return saveParames;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (listMenber.size() > 14) {
            if (position == 14) {
                AppConfig.listTemp.clear();
                if (PublicDataUtil.listBottom != null) {
                    PublicDataUtil.listBottom.clear();
                }
                Intent addMenber = new Intent(ChatRoomDetailsActivity.this, AAAAActivity.class);
                for (ChatMenberModel.ChatMenberData.ChatMenber cm : listMenber) {
                    BottomUserModel bum = new BottomUserModel();
                    bum.setUserHead(cm.getHead());
                    bum.setUserId(cm.getUserNo());
                    PublicDataUtil.listHasCunZai.add(bum);
                }
                addMenber.putExtra("groupNo", groupNo);
                addMenber.putExtra("hxIMGroupId", hxIMGroupId);
                addMenber.putExtra("groupName", chatInfo.getData().getName());
                startActivity(addMenber);
            } else if (position == 15) {
                Intent delIntent = new Intent();
                delIntent.putExtra("groupNo", groupNo);
                delIntent.putExtra("chooseModel", "1");
                delIntent.setClass(this, ChatGroupMenberActivity.class);
                startActivityForResult(delIntent, 6702);
            } else {
                Toastor.showToast(this, listMenber.get(position).getName());
            }
        } else {
            if (position == listMenber.size()) {
                AppConfig.listTemp.clear();
                if (PublicDataUtil.listBottom != null) {
                    PublicDataUtil.listBottom.clear();
                }
                if (chatType != Constant.CHATTYPE_SINGLE) {//群聊聊天详情界面
                    Intent addMenber = new Intent(ChatRoomDetailsActivity.this, AAAAActivity.class);
                    addMenber.putExtra("groupNo", groupNo);
                    addMenber.putExtra("hxIMGroupId", hxIMGroupId);
                    addMenber.putExtra("groupName", chatInfo.getData().getName());
                    for (ChatMenberModel.ChatMenberData.ChatMenber cm : listMenber) {
                        BottomUserModel bum = new BottomUserModel();
                        bum.setUserHead(cm.getHead());
                        bum.setUserId(cm.getUserNo());
                        PublicDataUtil.listHasCunZai.add(bum);
                    }
                    startActivity(addMenber);
                } else {
                    Intent addMenber = new Intent(ChatRoomDetailsActivity.this, AAAAActivity.class);
                    addMenber.putExtra("single2group", "1");
                    addMenber.putExtra("hxIMGroupId", hxIMGroupId);
//                    addMenber.putExtra("groupName", chatInfo.getData().getName());
                    Bundle bundle = new Bundle();
                    BottomUserModel bum = new BottomUserModel();
                    if (extendedChatModel.getFromUserNo().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo())) {
                        bum.setUserId(extendedChatModel.getToUserNo());
                        bum.setUserHead(extendedChatModel.getToUserAvatar());
                    } else {
                        bum.setUserId(extendedChatModel.getFromUserNo());
                        bum.setUserHead(extendedChatModel.getFromUserAvatar());
                    }
                    bundle.putSerializable("single2menber", bum);
                    BottomUserModel bum1 = new BottomUserModel();
                    bum1.setUserHead(MyApplication.getInstance().getUm().getData().getUserInfo().getHead());
                    bum1.setUserId(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                    PublicDataUtil.listHasCunZai.add(bum1);
                    PublicDataUtil.listHasCunZai.add(bum);
                    addMenber.putExtras(bundle);
                    startActivity(addMenber);
                }
            } else if (position == listMenber.size() + 1) {
                Intent delIntent = new Intent();
                delIntent.putExtra("groupNo", groupNo);
                delIntent.putExtra("chooseModel", "1");
                delIntent.setClass(this, ChatGroupMenberActivity.class);
                startActivityForResult(delIntent, 6702);
            } else {
                Toastor.showToast(this, listMenber.get(position).getName());
            }
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6702 && resultCode == 1002) {
            getChatInfo(hxIMGroupId);
        } else if (requestCode == 6702 && resultCode == 1004) {
            String result = data.getExtras().getString("groupName");
            tvChatGroupName.setText(result);
        }
    }

    /**
     * 清空群聊天记录
     */
    public void clearGroupHistory() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(hxIMGroupId, EMConversation.EMConversationType.ChatRoom);
        if (conversation != null) {
            conversation.clearAllMessages();
        }
        Toast.makeText(this, R.string.messages_are_empty, Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver destroyedBroadCast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.cn.szdt.SENDBROADCAST")) {
                ChatRoomDetailsActivity.this.finish();
            }
        }
    };
}
