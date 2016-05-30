package com.cn.clound.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.easemob.Constant;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.model.ExtendedChatModel;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.util.List;

import butterknife.Bind;

/**
 * 聊天界面
 *
 * @author ChunfaLee(ly09219@gami.com)
 * @date 2016-5-3 10:54:51
 */
public class DtChatActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.message_list)
    EaseChatMessageList list;
    @Bind(R.id.input_menu)
    EaseChatInputMenu inputMenu;
    @Bind(R.id.voice_recorder)
    EaseVoiceRecorderView voiceRecorderView;
    @Bind(R.id.img_base_right)
    ImageView imgBaseRight;
    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected int pagesize = 20;
    private EMConversation conversation;
    private String toChatUsername;
    protected int chatType;
    protected ListView listView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ClipboardManager clipboard;
    protected InputMethodManager inputManager;
    protected EaseChatFragment.EaseChatFragmentListener chatFragmentListener;
    private boolean isMessageListInited;
    protected boolean isloading;
    protected boolean haveMoreData = true;
    protected File cameraFile;
    protected int[] itemStrings = {com.hyphenate.easeui.R.string.attach_take_pic, com.hyphenate.easeui.R.string.attach_picture};
    protected int[] itemdrawables = {com.hyphenate.easeui.R.drawable.ease_chat_takepic_selector, com.hyphenate.easeui.R.drawable.ease_chat_image_selector};
    protected int[] itemIds = {ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_LOCATION};
    protected MyItemClickListener extendMenuItemClickListener;
    private ExtendedChatModel extendedChatModel;
    private EMChatRoomChangeListener chatRoomChangeListener;
    protected EMMessage contextMenuMessage;

    public void setChatFragmentListener(EaseChatFragment.EaseChatFragmentListener chatFragmentListener) {
        this.chatFragmentListener = chatFragmentListener;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_chat_message;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        imgBaseRight.setVisibility(View.VISIBLE);
        imgBaseRight.setOnClickListener(this);
        toChatUsername = getIntent().getExtras().getString(Constant.EXTRA_USER_ID);
        chatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);
        extendedChatModel = (ExtendedChatModel) getIntent().getSerializableExtra("extended_chat_model");
        if (chatType != EaseConstant.CHATTYPE_SINGLE) {
            imgBaseRight.setImageResource(R.mipmap.img_group_chat);
        } else {
            imgBaseRight.setImageResource(R.mipmap.img_single_chat);
        }
        listView = list.getListView();
        extendMenuItemClickListener = new MyItemClickListener();
        registerExtendMenuItem();
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                // 发送文本消息
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        // 发送语音消息
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                //发送大表情(动态表情)
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });
        swipeRefreshLayout = list.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(com.hyphenate.easeui.R.color.holo_blue_bright, com.hyphenate.easeui.R.color.holo_green_light,
                com.hyphenate.easeui.R.color.holo_orange_light, com.hyphenate.easeui.R.color.holo_red_light);

        inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        list.getListView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
        initData();
    }

    private void initData() {
//        titleBar.setTitle(toChatUsername);
        if (chatType == EaseConstant.CHATTYPE_SINGLE) { // 单聊
            // 设置标题
            if (extendedChatModel != null) {
                if (extendedChatModel.getFromUserNo().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo())) {
                    tvMidTitle.setText(extendedChatModel.getToUSerNick());
                } else {
                    tvMidTitle.setText(extendedChatModel.getFromUserNick());
                }
            }
        } else {
            if (extendedChatModel != null) {
                tvMidTitle.setText(extendedChatModel.getSessionName());
            } else {
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group != null) {

                }
                tvMidTitle.setText(group.getGroupName());
            }
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
            } else {
                onChatRoomViewCreation();
            }
        }
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }
        setRefreshLayoutListener();
        String forward_msg_id = getIntent().getExtras().getString("forward_msg_id");
        if (forward_msg_id != null) {
            // 发送要转发的消息
//            forwardMessage(forward_msg_id);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isMessageListInited)
            list.refresh();
        EaseUI.getInstance().pushActivity(this);
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.cn.szdt.SENDBROADCAST");
        registerReceiver(destroyedBroadCast, filter);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (msgListener != null) {
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        }
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
        AppConfig.IS_DT_CHAT_BACK = true;
        if (chatRoomChangeListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(chatRoomChangeListener);
        }
        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_MAP) { // 地图
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress);
                } else {
                    Toast.makeText(DtChatActivity.this, com.hyphenate.easeui.R.string.unable_to_get_loaction, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    protected void onChatRoomViewCreation() {
        final ProgressDialog pd = ProgressDialog.show(this, "", "Joining......");
        EMClient.getInstance().chatroomManager().joinChatRoom(toChatUsername, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(final EMChatRoom value) {
                DtChatActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (DtChatActivity.this.isFinishing() || !toChatUsername.equals(value.getId()))
                            return;
                        pd.dismiss();
                        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(toChatUsername);
                        if (room != null) {
//                            titleBar.setTitle(room.getName());
                        } else {
//                            titleBar.setTitle(toChatUsername);
                        }
                        addChatRoomChangeListenr();
                        onConversationInit();
                        onMessageListInit();
                    }
                });
            }

            @Override
            public void onError(final int error, String errorMsg) {
                // TODO Auto-generated method stub
//                EMLog.d(TAG, "join room failure : " + error);
                DtChatActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
                DtChatActivity.this.finish();
            }
        });
    }

    protected void onConversationInit() {
        // 获取当前conversation对象
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        // 把此会话的未读数置为0
        conversation.markAllMessagesAsRead();
        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }
    }

    protected void onMessageListInit() {
        list.init(toChatUsername, chatType, chatFragmentListener != null ?
                chatFragmentListener.onSetCustomChatRowProvider() : null);
        //设置list item里的控件的点击事件
        setListItemClickListener();
        list.getListView().setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                hideKeyboard();
                inputMenu.hideExtendMenuContainer();
                return false;
            }
        });
        isMessageListInited = true;
    }

    protected void setListItemClickListener() {
        list.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(EMMessage message) {
                Intent details = new Intent(DtChatActivity.this, ContactsDetailsActivity.class);
                try {
                    String temp = message.getStringAttribute("extended_msg_json");
                    ExtendedChatModel extendedChatModel = com.hyphenate.easeui.utils.GsonTools.getPerson(temp, ExtendedChatModel.class);
                    if (extendedChatModel == null) {
                        details.putExtra("user_no", MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                    } else {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            details.putExtra("user_no", MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                        } else {
                            if (extendedChatModel.getFromUserNo().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo())) {
                                if (extendedChatModel.getMsgType().equals("chat_join_me")) {
                                    details.putExtra("user_no", MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                                } else {
                                    details.putExtra("user_no", extendedChatModel.getToUserNo());
                                }
                            } else {
                                details.putExtra("user_no", extendedChatModel.getFromUserNo());
                            }
                        }
                    }
                    startActivity(details);
                    DtChatActivity.this.finish();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResendClick(final EMMessage message) {
                new EaseAlertDialog(DtChatActivity.this, com.hyphenate.easeui.R.string.resend, com.hyphenate.easeui.R.string.confirm_resend, null, new EaseAlertDialog.AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
//                        resendMessage(message);
                    }
                }, true).show();
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;
                if (chatFragmentListener != null) {
                    chatFragmentListener.onMessageBubbleLongClick(message);
                }
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
//                if (chatFragmentListener != null) {
//                    return chatFragmentListener.onMessageBubbleClick(message);
//                }
                return false;
            }
        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                            List<EMMessage> messages;
                            try {
                                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                                    messages = conversation.loadMoreMsgFromDB(list.getItem(0).getMsgId(),
                                            pagesize);
                                } else {
                                    messages = conversation.loadMoreMsgFromDB(list.getItem(0).getMsgId(),
                                            pagesize);
                                }
                            } catch (Exception e1) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }
                            if (messages.size() > 0) {
                                list.refreshSeekTo(messages.size() - 1);
                                if (messages.size() != pagesize) {
                                    haveMoreData = false;
                                }
                            } else {
                                haveMoreData = false;
                            }
                            isloading = false;
                        } else {
                            Toast.makeText(DtChatActivity.this, getResources().getString(com.hyphenate.easeui.R.string.no_more_messages),
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 600);
            }
        });
    }


    //发送消息方法
    //==========================================================================
    protected void sendTextMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        sendMessage(message);
    }

    protected void sendBigExpressionMessage(String name, String identityCode) {
        EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }

    protected void sendMessage(EMMessage message) {
        if (message == null) {
            return;
        }
        if (chatFragmentListener != null) {
            //设置扩展属性
            chatFragmentListener.onSetMessageAttributes(message);
        }
        //自定义属性
        ExtendedChatModel exm = new ExtendedChatModel();
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            exm.setMsgType("1");
            EMGroup group = EMClient.getInstance().groupManager().getGroup(message.getUserName());
            exm.setSessionName(extendedChatModel.getSessionName());
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            exm.setMsgType("1");
            EMGroup group = EMClient.getInstance().groupManager().getGroup(message.getUserName());
            exm.setSessionName(extendedChatModel.getSessionName());
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else {
            exm.setMsgType("0");
            if (extendedChatModel.getFromUserNo().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo())) {
                exm.setSessionName(extendedChatModel.getToUSerNick().equals("") ? extendedChatModel.getToUserNo() : extendedChatModel.getToUSerNick());
            } else {
                exm.setSessionName(extendedChatModel.getFromUserNick().equals("") ? extendedChatModel.getFromUserNo() : extendedChatModel.getFromUserNick());
            }
        }
        exm.setFromUserAvatar(MyApplication.getInstance().getUm().getData().getUserInfo().getHead());
        exm.setFromUserNo(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
        exm.setFromUserNick(MyApplication.getInstance().getUm().getData().getUserInfo().getName());
        if (extendedChatModel.getFromUserNo().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo())) {
            exm.setToUserAvatar(extendedChatModel.getToUserAvatar());
            exm.setToUserNo(extendedChatModel.getToUserNo());
            exm.setToUSerNick(extendedChatModel.getToUSerNick());
        } else {
            exm.setToUserAvatar(extendedChatModel.getFromUserAvatar());
            exm.setToUserNo(extendedChatModel.getFromUserNo());
            exm.setToUSerNick(extendedChatModel.getFromUserNick());
        }
        message.setAttribute("extended_msg_json", GsonTools.obj2json(exm));
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        //刷新ui
        if (isMessageListInited) {
            list.refreshSelectLast();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                DtChatActivity.this.finish();
                break;
            case R.id.img_base_right:
                Intent intent = new Intent(this, ChatRoomDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("extended_chat_model", extendedChatModel);
                intent.putExtras(bundle);
                intent.putExtra("imGroupNo", toChatUsername);
                intent.putExtra(Constant.EXTRA_CHAT_TYPE, chatType);
                startActivity(intent);
                break;
        }
    }

    /**
     * 注册底部菜单扩展栏item; 覆盖此方法时如果不覆盖已有item，item的id需大于3
     */
    protected void registerExtendMenuItem() {
        for (int i = 0; i < itemStrings.length; i++) {
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }

    /**
     * 扩展菜单栏item点击事件
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            if (chatFragmentListener != null) {
                if (chatFragmentListener.onExtendMenuItemClick(itemId, view)) {
                    return;
                }
            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE: // 拍照
                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    selectPicFromLocal(); // 图库选择图片
                    break;
                case ITEM_LOCATION: // 位置
                    startActivityForResult(new Intent(DtChatActivity.this, EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, com.hyphenate.easeui.R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, com.hyphenate.easeui.R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(file.getAbsolutePath());
        }
    }

    /**
     * 照相获取图片
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isExitsSdcard()) {
            Toast.makeText(DtChatActivity.this, com.hyphenate.easeui.R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 从图库获取图片
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    protected void addChatRoomChangeListenr() {
        chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(toChatUsername)) {
                    DtChatActivity.this.finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
            }

            @Override
            public void onMemberKicked(String roomId, String roomName, String participant) {
                if (roomId.equals(toChatUsername)) {
                    String curUser = EMClient.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                        EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
                        DtChatActivity.this.finish();
                    } else {
                    }
                }
            }

        };
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            for (EMMessage message : messages) {
                String username = null;
                // 群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    // 单聊消息
                    username = message.getFrom();
                }

                // 如果是当前会话的消息，刷新聊天页面
                if (username.equals(toChatUsername)) {
                    list.refreshSelectLast();
                    // 声音和震动提示有新消息
                    EaseUI.getInstance().getNotifier().viberateAndPlayTone(message);
                } else {
                    // 如果消息不是和当前聊天ID的消息
                    EaseUI.getInstance().getNotifier().onNewMsg(message);
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            if (isMessageListInited) {
                list.refresh();
            }
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            if (isMessageListInited) {
                list.refresh();
            }
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            if (isMessageListInited) {
                list.refresh();
            }
        }
    };

    BroadcastReceiver destroyedBroadCast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.cn.szdt.SENDBROADCAST")) {
                DtChatActivity.this.finish();
            }
        }
    };
}
