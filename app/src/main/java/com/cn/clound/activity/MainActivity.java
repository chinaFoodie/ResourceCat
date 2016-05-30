package com.cn.clound.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.netstate.NetWorkUtil;
import com.cn.clound.bean.User.UserModel;
import com.cn.clound.easemob.Constant;
import com.cn.clound.easemob.HuanXinHelper;
import com.cn.clound.easemob.db.InviteMessgeDao;
import com.cn.clound.easemob.db.UserDao;
import com.cn.clound.easemob.ui.ChatActivity;
import com.cn.clound.easemob.ui.GroupsActivity;
import com.cn.clound.fragment.ContactMainFragment;
import com.cn.clound.fragment.IndexMainFragment;
import com.cn.clound.fragment.MessageMainFragment;
import com.cn.clound.view.slidingmenu.SlidingMenu;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Class MainAcitivity
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年3月29日11:27:05
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    //需要加载的fragment集合
    private Fragment[] fragments;
    private IndexMainFragment indexMainFragment;
    private ContactMainFragment contactMainFragment;// ContactListFragment
    private MessageMainFragment messageMainFragment; //ConversationListFragment
    private boolean isAccountRemovedDialogShow;
    private RelativeLayout[] mTabs;
    private ImageView[] mainImgs;
    private TextView[] mainTvs;
    private int[] normal, press;
    public static SlidingMenu leftMenu;
    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    private boolean isConflictDialogShow;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private BroadcastReceiver internalDebugReceiver;
    //本地广播
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;

    @Bind(R.id.img_main_work)
    ImageView imgMainWork;
    @Bind(R.id.img_main_message)
    ImageView imgMainMessage;
    @Bind(R.id.img_main_contacts)
    ImageView imgMainContacts;
    @Bind(R.id.tv_main_work)
    TextView tvMainWork;
    @Bind(R.id.tv_main_message)
    TextView tvMainMessage;
    @Bind(R.id.tv_main_contacts)
    TextView tvMainContacts;
    @Bind(R.id.rl_main_work)
    RelativeLayout rlMainWork;
    @Bind(R.id.rl_main_message)
    RelativeLayout rlMainMessage;
    @Bind(R.id.rl_main_contacts)
    RelativeLayout rlMainContacts;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_main;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
        initLeftMenu();
        initHuanXin(savedInstanceState);
    }

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    private void initHuanXin(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            HuanXinHelper.getInstance().logout(true, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        //umeng api
//        MobclickAgent.updateOnlineConfig(this);
//        UmengUpdateAgent.setUpdateOnlyWifi(false);
//        UmengUpdateAgent.update(this);
        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
        //注册local广播接收者，用于接收demohelper中发出的群组联系人的变动通知
        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        //内部测试方法，请忽略
        registerInternalDebugReceiver();
    }

    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                HuanXinHelper.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // 重新显示登陆页面
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (currentTabIndex == 0) {
//                     当前页面如果为聊天历史页面，刷新此页面
//                    if (messageMainFragment != null) {
//                        messageMainFragment.refresh();
//                    }
//                } else if (currentTabIndex == 1) {
//                    if (contactMainFragment != null) {
//                        contactMainFragment.refresh();
//                    }
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void updateUnreadAddressLable() {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                int count = getUnreadAddressCountTotal();
//                if (count > 0) {
////					unreadAddressLable.setText(String.valueOf(count));
//                    unreadAddressLable.setVisibility(View.VISIBLE);
//                } else {
//                    unreadAddressLable.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
    }

    private int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    public void updateUnreadLabel() {
//        int count = getUnreadMsgCountTotal();
//        if (count > 0) {
//            unreadLabel.setText(String.valueOf(count));
//            unreadLabel.setVisibility(View.VISIBLE);
//        } else {
//            unreadLabel.setVisibility(View.INVISIBLE);
//        }
    }

    private int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        HuanXinHelper.getInstance().logout(false, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private android.app.AlertDialog.Builder conflictBuilder;

    private void showConflictDialog() {

        isConflictDialogShow = true;
        HuanXinHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    /**
     * 初始化左侧边栏
     */
    private void initLeftMenu() {
        leftMenu = new SlidingMenu(mContext);
        leftMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        leftMenu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单视图的宽度
        leftMenu.setBehindOffsetRes(R.dimen.slidingmenu_leftset);
        // 设置渐入渐出效果的值
        leftMenu.setFadeDegree(0.0f);
        leftMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        leftMenu.setMenu(R.layout.dt_left_menu_layout);
        LinearLayout leftMenuBack = (LinearLayout) leftMenu.findViewById(R.id.ll_base_back);
        leftMenu.findViewById(R.id.btn_exit).setOnClickListener(this);
        leftMenuBack.setOnClickListener(this);
        leftMenuBack.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化视图View
     */
    private void init() {
        rlMainWork.setOnClickListener(this);
        rlMainContacts.setOnClickListener(this);
        rlMainMessage.setOnClickListener(this);
        normal = new int[]{R.mipmap.btn_main_work_normal, R.mipmap.btn_main_message_normal, R.mipmap.btn_main_contacts_normal};
        press = new int[]{R.mipmap.btn_main_work_press, R.mipmap.btn_main_message_press, R.mipmap.btn_main_contacts_press};
        mainImgs = new ImageView[]{imgMainWork, imgMainMessage, imgMainContacts};
        mainTvs = new TextView[]{tvMainWork, tvMainMessage, tvMainContacts};
        mTabs = new RelativeLayout[]{rlMainWork, rlMainMessage, rlMainContacts};
        indexMainFragment = new IndexMainFragment();
        messageMainFragment = new MessageMainFragment();//MessageMainFragment
        contactMainFragment = new ContactMainFragment();// ContactListFragment
        fragments = new Fragment[]{indexMainFragment, messageMainFragment, contactMainFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout, messageMainFragment).hide(messageMainFragment)
                .add(R.id.main_frame_layout, contactMainFragment).hide(contactMainFragment).add(R.id.main_frame_layout, indexMainFragment).
                show(indexMainFragment).commit();
//        mTabs[0].setSelected(true);
        registerForContextMenu(mTabs[1]);
        getUsreInfo();
    }

    /***
     * 测试写法
     **/
    private void getUsreInfo() {
        OkHttpUtils.post().url(AppConfig.GET_USER_INFO).addParams("token", TelephoneUtil.getIMEI(MainActivity.this)).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String status = json.getString("status");
                    if (AppConfig.SUCCESS.equals(status)) {
                        UserModel um = GsonTools.getPerson(response, UserModel.class);
                        MyApplication.getInstance().setUm(um);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (AppConfig.IS_DT_CHAT_BACK) {
            index = 1;
            onTabClicked();
            messageMainFragment.refreshMsgUi();
            AppConfig.IS_DT_CHAT_BACK = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onConnect(NetWorkUtil.netType type) {
        super.onConnect(type);
        Toastor.showToast(mContext, "网络已连接");
    }

    @Override
    public void onDisConnect() {
        super.onDisConnect();
        Toastor.showToast(mContext, "网络已断开");
    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }
        unregisterBroadcastReceiver();
        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        // Todo 按钮点击事件
        switch (v.getId()) {
            case R.id.ll_base_back:
                leftMenu.toggle();
                break;
            case R.id.rl_main_work:
                index = 0;
                onTabClicked();
                break;
            case R.id.rl_main_message:
                index = 1;
                onTabClicked();
                messageMainFragment.refreshMsgUi();
                break;
            case R.id.rl_main_contacts:
                index = 2;
                onTabClicked();
                break;
            //测试环信退出
            case R.id.btn_exit:
                hxExit();
                break;
            default:
                break;
        }
    }

    /**
     * 退出环信
     */
    private void hxExit() {
        HuanXinHelper.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        // 重新显示登陆页面
                        MainActivity.this.finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * button点击事件
     *
     * @param
     */
    public void onTabClicked() {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.main_frame_layout, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
//        mTabs[index].setSelected(true);
        mainTvs[currentTabIndex].setTextColor(getResources().getColor(R.color.colorWhiteMainTab));
        mainTvs[index].setTextColor(getResources().getColor(R.color.colorBlueMainTab));
        mainImgs[currentTabIndex].setImageResource(normal[currentTabIndex]);
        mainImgs[index].setImageResource(press[index]);
        currentTabIndex = index;
    }

    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_SHORT)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onContactAgreed(String username) {
        }

        @Override
        public void onContactRefused(String username) {
        }
    }
}
