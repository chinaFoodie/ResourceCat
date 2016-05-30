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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.BottomChoseChatMenberAdapter;
import com.cn.clound.adapter.ChoseChatMenberAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.PinyinUtils;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.User.DTUser;
import com.cn.clound.bean.User.FindTopContactsModel;
import com.cn.clound.bean.chat.ChatRoomInfoModel;
import com.cn.clound.bean.choose.RadioOrMultDeptUser;
import com.cn.clound.easemob.Constant;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.utils.PinyinComparator;
import com.cn.clound.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 选择聊天成员界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月28日 11:53:09
 */
public class ChoseChatRoomMenberActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.main_contacts_sidebar)
    SideBar sidebar;
    @Bind(R.id.main_contacts_list)
    ListView contactsListView;
    @Bind(R.id.floating_header)
    TextView tvSideBarTitle;
    @Bind(R.id.query)
    EditText etQuery;
    @Bind(R.id.recycler_chat_menber)
    RecyclerView menberView;
    @Bind(R.id.tv_chose_menber_count)
    TextView tvMenberCount;
    private int isClear = 1;
    private String chatName;
    int[] header = new int[]{R.string.man_contact_header_up_down, R.string.man_contact_header_org};
    private List<DTUser> listUser = new ArrayList<DTUser>();
    private PinyinComparator pinyinComparator;
    private MyHttpHelper httpHelper;
    private ChoseChatMenberAdapter adapter;
    private int HTTP_GET_TOP_CONTACTS = 110;

    private int HTTP_CREATE_CHAT_ROOM = 119;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_TOP_CONTACTS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    FindTopContactsModel cm = (FindTopContactsModel) msg.obj;
                    if (null != cm.getData().getResult() && cm.getData().getResult().size() > 0) {
                        for (int i = 0; i < cm.getData().getResult().size(); i++) {
                            DTUser user = new DTUser();
                            user.setCheckedBox(false);
                            user.setCm(cm.getData().getResult().get(i));
                            user.setSortLetters(PinyinUtils.getPinYinHeadChar(cm.getData().getResult().get(i).getName()));
                            user.setHeadKey(PinyinUtils.getPinYinHeadChar(cm.getData().getResult().get(i).getName()).substring(0, 1).toUpperCase());
                            listUser.add(user);
                        }
                        Collections.sort(listUser, pinyinComparator);
                    }
                    for (int j = 0; j < header.length; j++) {
                        DTUser user = new DTUser();
                        FindTopContactsModel.TopContactsModel tcm = new FindTopContactsModel.TopContactsModel();
                        tcm.setName(getResources().getString(header[j]));
                        user.setCm(tcm);
                        listUser.add(j, user);
                    }
                    adapter = new ChoseChatMenberAdapter(ChoseChatRoomMenberActivity.this, listUser, null);
                    contactsListView.setAdapter(adapter);
                    contactsListView.setOnItemClickListener(ChoseChatRoomMenberActivity.this);
                }
            } else if (msg.arg1 == HTTP_CREATE_CHAT_ROOM) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    AppConfig.CHAT_GROUP_REFUSER = "1";
                    AppConfig.listTemp.clear();
                    ChatRoomInfoModel crm = (ChatRoomInfoModel) msg.obj;
                    startActivity(new Intent(ChoseChatRoomMenberActivity.this, DtChatActivity.class).putExtra("chatType", Constant.CHATTYPE_GROUP).putExtra("userId", crm.getData().getImGroupId()));
                    ChoseChatRoomMenberActivity.this.finish();
                } else {
                    Toastor.showToast(ChoseChatRoomMenberActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 10) {
                menberView.scrollToPosition(bottomList.size());
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_chose_chat_room_menber;
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
        tvMidTitle.setText("选择群成员");
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        Intent preIntent = this.getIntent();
        chatName = preIntent.getStringExtra("chat_room_name");
        tvMenberCount.setOnClickListener(this);
        sidebar.setTextView(tvSideBarTitle);
        pinyinComparator = new PinyinComparator();
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    contactsListView.setSelection(position);
                }
            }
        });
        getContacts();
        etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /**
                 * 搜索list数据并更新列表操作
                 */
                queryList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        menberView.setLayoutManager(linearLayoutManager);
        bottomAdapter = new BottomChoseChatMenberAdapter(ChoseChatRoomMenberActivity.this, bottomList);
        menberView.setAdapter(bottomAdapter);
    }

    private void getContacts() {
        if (listUser != null && listUser.size() > 0) {
            listUser.clear();
        }
        httpHelper.postStringBack(HTTP_GET_TOP_CONTACTS, AppConfig.GET_TOP_CONTACTS_LIST, setParames(), handler, FindTopContactsModel.class);
    }

    private HashMap<String, String> setParames() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        return parames;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (AppConfig.listTemp.size() > 0) {
            for (RadioOrMultDeptUser user : AppConfig.listTemp) {
                DTUser dtu = new DTUser();
                FindTopContactsModel.TopContactsModel cm = new FindTopContactsModel.TopContactsModel();
                cm.setUserNo(user.getUser().getUserNo());
                dtu.setCm(cm);
                bottomList.add(dtu);
            }
            bottomAdapter.notifyDataSetChanged();
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
        AppConfig.DEPT_MENBER_MUTIL = false;
        AppConfig.listTemp.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                finish();
                break;
            case R.id.tv_chose_menber_count:
                httpHelper.postStringBack(HTTP_CREATE_CHAT_ROOM, AppConfig.CREATE_CHAT_ROOM, createParames(chatName), handler, ChatRoomInfoModel.class);
                break;
            default:
                break;
        }
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
        parames.put("users", getUsers(bottomList));
        return parames;
    }

    /**
     * @return
     */
    private String getUsers(List<DTUser> list) {
        String users = "";
        for (DTUser u : list) {
            users += (u.getCm().getUserNo() + ",");
        }
        return users;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < header.length) {
            AppConfig.DEPT_MENBER_MUTIL = true;
            switch (position) {
                case 0:
                    startActivity(new Intent(ChoseChatRoomMenberActivity.this, HierarchyActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(ChoseChatRoomMenberActivity.this, DeptManagerActivity.class).putExtra("unit_id", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo()));
                    break;
                default:
                    break;
            }
        } else {
            if (listUser.get(position).isCheckedBox()) {
                listUser.get(position).setCheckedBox(false);
            } else {
                listUser.get(position).setCheckedBox(true);
            }
            adapter.notifyDataSetChanged();
            changeBottomMenber();
        }
    }

    /**
     * 改变底部成员列表数据
     */
    private List<DTUser> bottomList = new ArrayList<>();
    private BottomChoseChatMenberAdapter bottomAdapter = null;

    private void changeBottomMenber() {
        if (bottomList.size() > 0) {
            bottomList.clear();
        }
        for (DTUser user : listUser) {
            if (user.isCheckedBox()) {
                bottomList.add(user);
            }
        }
        if (AppConfig.listTemp.size() > 0) {
            for (RadioOrMultDeptUser user : AppConfig.listTemp) {
                DTUser dtu = new DTUser();
                FindTopContactsModel.TopContactsModel cm = new FindTopContactsModel.TopContactsModel();
                cm.setUserNo(user.getUser().getUserNo());
                dtu.setCm(cm);
                bottomList.add(dtu);
            }
        }
        tvMenberCount.setText("确定 (" + bottomList.size() + ")");
        bottomAdapter.notifyDataSetChanged();
        handler.sendEmptyMessage(10);
    }

    /**
     * 搜索联系人List数据
     *
     * @param s
     */
    private void queryList(String s) {
        List<DTUser> temp = new ArrayList<DTUser>();
        if (TextUtils.isEmpty(s)) {
            for (int j = 0; j < header.length; j++) {
                DTUser user = new DTUser();
                FindTopContactsModel.TopContactsModel tcm = new FindTopContactsModel.TopContactsModel();
                tcm.setName(getResources().getString(header[j]));
                user.setCm(tcm);
                listUser.add(j, user);
            }
            isClear = 1;
            temp = listUser;
        } else {
            temp.clear();
            if (isClear == 1) {
                for (int i = 0; i < header.length; i++) {
                    listUser.remove(0);
                }
                isClear = 0;
            }
            for (DTUser dUser : listUser) {
                String sP = PinyinUtils.getPinYin(dUser.getCm().getName());
                String name = dUser.getCm().getName()
                        + PinyinUtils.getPinYinHeadChar(dUser.getCm().getName()) + sP;
                if (name.indexOf(s.toString()) != -1) {
                    temp.add(dUser);
                }
            }
            // 根据a-z进行排序
            Collections.sort(temp, pinyinComparator);
            for (int j = 0; j < header.length; j++) {
                DTUser user = new DTUser();
                FindTopContactsModel.TopContactsModel tcm = new FindTopContactsModel.TopContactsModel();
                tcm.setName(getResources().getString(header[j]));
                user.setCm(tcm);
                temp.add(j, user);
            }
        }
        adapter.updateListView(temp);
    }
}
