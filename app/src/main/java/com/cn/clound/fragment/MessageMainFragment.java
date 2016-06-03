package com.cn.clound.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.clound.R;
import com.cn.clound.activity.AAAAActivity;
import com.cn.clound.activity.DtChatActivity;
import com.cn.clound.activity.InviteContactActivity;
import com.cn.clound.adapter.MessageMainAdapter;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.view.OnRcvScrollListener;
import com.cn.clound.base.view.SwipeMenuRecyclerView;
import com.cn.clound.base.view.VerticalSpaceItemDecoration;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.easemob.Constant;
import com.cn.clound.easemob.db.InviteMessgeDao;
import com.cn.clound.utils.PopWindowUtil;
import com.cn.clound.view.AlertDialog;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.ExtendedChatModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @version 1.00
 * @date 2016年3月30日13:15:52
 */
public class MessageMainFragment extends BaseFragment implements OnItemClickLitener, View.OnClickListener {
    @Bind(R.id.message_fragment_title)
    View parent;
    @Bind(R.id.message_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.message_listView)
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.img_base_right)
    ImageView imgBaseRight;
    @Bind(R.id.ll_base_right)
    LinearLayout llParent;
    private Handler mHandler;
    private MessageMainAdapter adapter;
    private List<EMConversation> list;
    private PopWindowUtil popMenu;
    private boolean isRefresh = false;

    protected EMConversationListener convListener = new EMConversationListener() {

        @Override
        public void onCoversationUpdate() {
            refreshMsgUi();
        }
    };

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1001:
                    isRefresh = !isRefresh;
                    getMessageList();
                    break;
                case 1002:
                    final int index = (int) msg.obj;
                    new AlertDialog(getActivity()).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Todo 删除会话
                            try {
                                // 删除此会话
                                EMClient.getInstance().chatManager().deleteConversation(list.get(index).getUserName(), true);
                                InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
                                inviteMessgeDao.deleteMessage(list.get(index).getUserName());
                                isRefresh = true;
                                getMessageList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).setMsg("是否确认删除会话？").show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_message_main_fragment;
    }

    @Override
    public void onFragmentAttach(Fragment fragment, Activity activity) {

    }

    @Override
    public void onFragmentCreated(Fragment fragment, Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentCreateView(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    }

    @Override
    public void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        tvMidTitle.setText(getResources().getString(R.string.mid_title_message));
        imgBaseRight.setVisibility(View.VISIBLE);
        imgBaseRight.setImageResource(R.mipmap.dt_img_add);
        imgBaseRight.setOnClickListener(this);
        mHandler = new Handler();
        getMessageList();
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeMenuRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(0));
        adapter.setmOnItemClickLitener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = !isRefresh;
                getMessageList();
            }
        });
        swipeMenuRecyclerView.setOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                // Todo 到底部自动加载
//                if (!isLoadingData) {
////                    adapter.loadOldData();
//                    isLoadingData = true;
//                }
            }
        });
        EMClient.getInstance().addConnectionListener(connectionListener);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dt_pop_message_menu, null);
        popMenu = new PopWindowUtil(getActivity(), view);
        view.findViewById(R.id.create_muti_chat).setOnClickListener(this);
        view.findViewById(R.id.join_contacts_chat).setOnClickListener(this);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };

    @Override
    public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {
    }

    @Override
    public void onFragmentStarted(Fragment fragment) {

    }

    @Override
    public void onFragmentResumed(Fragment fragment) {
        if (AppConfig.BACK_SHUA_XIN) {
            isRefresh = true;
            getMessageList();
            AppConfig.BACK_SHUA_XIN = false;
        }
    }

    @Override
    public void onFragmentPaused(Fragment fragment) {
        AppConfig.BACK_SHUA_XIN = true;
    }

    @Override
    public void onFragmentStopped(Fragment fragment) {

    }

    @Override
    public void onFragmentDestroyed(Fragment fragment) {
        AppConfig.BACK_SHUA_XIN = false;
    }

    @Override
    public void onFragmentDetach(Fragment fragment) {

    }

    @Override
    public void onFragmentSaveInstanceState(Fragment fragment, Bundle outState) {

    }

    public void refreshMsgUi() {
        getMessageList();
        adapter.setmOnItemClickLitener(this);
    }

    /**
     * 获取会话列表
     *
     * @return
     */
    private List<EMConversation> getMessageList() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                EMMessage emMessage = conversation.getLastMessage();
                if (emMessage != null) {
                    String temp = emMessage.getStringAttribute("extended_msg_json", "");
                    if (!temp.equals("")) {
                        ExtendedChatModel ecm = GsonTools.getPerson(temp, ExtendedChatModel.class);
                        if (conversation.getAllMessages().size() != 0 && !ecm.getMsgType().equals("MeetMessage")) {
                            sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                        }
                    }
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        adapter = new MessageMainAdapter(getActivity(), list, handler);
        swipeMenuRecyclerView.setAdapter(adapter);
        if (isRefresh) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setmOnItemClickLitener(this);
            isRefresh = !isRefresh;
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {
                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    public static void refreshMessage() {
//        Toastor.showToast(getActivity(),"");
    }

    @Override
    public void onItemClick(View view, int position) {
        EMConversation conversation = list.get(position);
        String username = conversation.getUserName();
        EMMessage message = conversation.getLastMessage();
        ExtendedChatModel ecm = new ExtendedChatModel();
        String temp = message.getStringAttribute("extended_msg_json", "");
        ecm = GsonTools.getPerson(temp, ExtendedChatModel.class);
        if (username.equals(EMClient.getInstance().getCurrentUser()))
            Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
        else {
            // 进入聊天页面
            Intent intent = new Intent(getActivity(), DtChatActivity.class);
            if (conversation.isGroup()) {
                if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                    // it's group chat
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                } else {
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                }
            } else {
                intent.putExtra(Constant.EXTRA_CHAT_TYPE, 1);
            }
            // it's single chat
            Bundle bundle = new Bundle();
            bundle.putSerializable("extended_chat_model", ecm);
            intent.putExtras(bundle);
            intent.putExtra(Constant.EXTRA_USER_ID, username);
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_base_right:
                popMenu.showPopupWindow(llParent);
                break;
            case R.id.create_muti_chat:
                popMenu.dismissPopupWindow();
                PublicDataUtil.listBottom.clear();
                BottomUserModel bum1 = new BottomUserModel();
                bum1.setUserHead(MyApplication.getInstance().getUm().getData().getUserInfo().getHead());
                bum1.setUserId(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                PublicDataUtil.listHasCunZai.add(bum1);
                startActivity(new Intent(getActivity(), AAAAActivity.class));
                break;
            case R.id.join_contacts_chat:
                popMenu.dismissPopupWindow();
                startActivity(new Intent(getActivity(), InviteContactActivity.class));
                break;
            default:
                break;
        }
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            handler.sendEmptyMessage(1001);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };
}
