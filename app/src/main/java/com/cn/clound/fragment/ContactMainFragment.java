package com.cn.clound.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.activity.ChatGroupActivity;
import com.cn.clound.activity.ContactsDetailsActivity;
import com.cn.clound.activity.DeptManagerActivity;
import com.cn.clound.activity.HierarchyActivity;
import com.cn.clound.activity.InviteContactActivity;
import com.cn.clound.activity.OrganizationActivity;
import com.cn.clound.activity.UnallocatedDeptMenberActivity;
import com.cn.clound.adapter.ContactMainRecyclerAdapter;
import com.cn.clound.adapter.ContactsMainAdapter;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.PinyinUtils;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.view.SwipeMenuRecyclerView;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.User.DTUser;
import com.cn.clound.bean.User.FindTopContactsModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.utils.PinyinComparator;
import com.cn.clound.view.AlertDialog;
import com.cn.clound.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @version 1.00
 * @date 2016年3月30日13:15:52
 */
public class ContactMainFragment extends BaseFragment implements OnItemClickLitener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.main_contacts_sidebar)
    SideBar sidebar;
    @Bind(R.id.floating_header)
    TextView tvSideBarTitle;
    @Bind(R.id.query)
    EditText etQuery;
    @Bind(R.id.main_contacts_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.main_contacts_swipeMenuList)
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    @Bind(R.id.main_contacts_list)
    ListView listView;
    private int isClear = 1;
    private ContactMainRecyclerAdapter recyclerAdapter;
    int[] header = new int[]{R.string.man_contact_header_join, R.string.man_contact_header_up_down, R.string.man_contact_header_org, R.string.man_contact_header_group, R.string.man_contact_header_group_unallocated};
    private List<DTUser> listUser = new ArrayList<DTUser>();
    private PinyinComparator pinyinComparator;
    private MyHttpHelper httpHelper;
    private int HTTP_GET_TOP_CONTACTS = 110;
    private int HTTP_DELETE_TOP_CONTACTS = 112;
    private ContactsMainAdapter adapter;

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
                            user.setCm(cm.getData().getResult().get(i));
                            user.setSortLetters(PinyinUtils.getPinYinHeadChar(cm.getData().getResult().get(i).getName()));
                            if (PinyinUtils.getPinYinHeadChar(cm.getData().getResult().get(i).getName()).length() > 0) {
                                user.setHeadKey(PinyinUtils.getPinYinHeadChar(cm.getData().getResult().get(i).getName()).substring(0, 1).toUpperCase());
                            } else {
                                user.setHeadKey("#");
                            }
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
                    swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (listUser.size() > 0) {
                        recyclerAdapter = new ContactMainRecyclerAdapter(getActivity(), listUser, handler);
                        swipeMenuRecyclerView.setAdapter(recyclerAdapter);
                        recyclerAdapter.setOnItemClickLitener(ContactMainFragment.this);
                    }
                }
            } else if (msg.arg1 == HTTP_DELETE_TOP_CONTACTS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    getContacts();
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            } else if (msg.what == 1000) {
                final int index = (int) msg.obj;
                new AlertDialog(getActivity()).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Todo 删除常用联系人
                        httpHelper.postStringBack(HTTP_DELETE_TOP_CONTACTS, AppConfig.DELETE_TOP_CONTACTS, addTopParames(listUser.get(index).getCm().getUserNo()), handler, BaseModel.class);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setMsg("是否确认删除常用联系人？").show();
            }
        }
    };

    /***
     * 添加常用联系人接口
     **/
    private HashMap<String, String> addTopParames(String userNo) {
        HashMap<String, String> addParames = new HashMap<String, String>();
        addParames.put("token", TelephoneUtil.getIMEI(getActivity()));
        addParames.put("toUser", userNo);
        return addParames;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_contact_main_fragment;
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
     * 初始化View
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(getActivity());
        tvMidTitle.setText(getResources().getString(R.string.mid_title_contacts));
        sidebar.setTextView(tvSideBarTitle);
        pinyinComparator = new PinyinComparator();
        swipeRefreshLayout.setColorSchemeResources(com.hyphenate.easeui.R.color.holo_blue_bright, com.hyphenate.easeui.R.color.holo_green_light,
                com.hyphenate.easeui.R.color.holo_orange_light, com.hyphenate.easeui.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContacts();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = recyclerAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    swipeMenuRecyclerView.scrollToPosition(position);
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
    }

    /**
     * 搜索联系人List数据
     *
     * @param s
     */
    private void queryList(String s) {
        final List<DTUser> temp = new ArrayList<DTUser>();
        List<DTUser> tempSort = new ArrayList<DTUser>();
        if (TextUtils.isEmpty(s)) {
            getContacts();
            isClear = 1;
            swipeMenuRecyclerView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            swipeMenuRecyclerView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            temp.clear();
            tempSort = listUser;
            if (isClear == 1) {
                for (int i = 0; i < header.length; i++) {
                    tempSort.remove(0);
                }
                isClear = -1;
            }
            for (DTUser dUser : tempSort) {
                String sP = PinyinUtils.getPinYin(dUser.getCm().getName());
                String name = dUser.getCm().getName()
                        + PinyinUtils.getPinYinHeadChar(dUser.getCm().getName()) + sP;
                if (name.indexOf(s.toString()) != -1) {
                    temp.add(dUser);
                }
            }
            // 根据a-z进行排序
            Collections.sort(temp, pinyinComparator);
        }
        adapter = new ContactsMainAdapter(getActivity(), temp);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ContactsDetailsActivity.class);
                intent.putExtra("user_no", temp.get(position).getCm().getUserNo());
                startActivity(intent);
            }
        });
    }

    /**
     * 获取联系人
     */
    private void getContacts() {
        if (listUser != null && listUser.size() > 0) {
            listUser.clear();
        }
        httpHelper.postStringBack(HTTP_GET_TOP_CONTACTS, AppConfig.GET_TOP_CONTACTS_LIST, setParames(), handler, FindTopContactsModel.class);
    }

    /**
     * 获取常用联系人列表参数配置
     *
     * @return
     */
    private HashMap<String, String> setParames() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(getActivity()));
        return parames;
    }

    @Override
    public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentStarted(Fragment fragment) {

    }

    @Override
    public void onFragmentResumed(Fragment fragment) {
        if (ContactsDetailsActivity.TOP_NEED_LOAD) {
            getContacts();
            ContactsDetailsActivity.TOP_NEED_LOAD = false;
        }
    }

    @Override
    public void onFragmentPaused(Fragment fragment) {

    }

    @Override
    public void onFragmentStopped(Fragment fragment) {

    }

    @Override
    public void onFragmentDestroyed(Fragment fragment) {

    }

    @Override
    public void onFragmentDetach(Fragment fragment) {

    }

    @Override
    public void onFragmentSaveInstanceState(Fragment fragment, Bundle outState) {

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(getActivity(), InviteContactActivity.class));
                break;
            case 1:
                AppConfig.DEPT_MENBER_MUTIL = false;
                Intent intentHier = new Intent(getActivity(), HierarchyActivity.class);
                startActivity(intentHier);
                break;
            case 2://如果是超级管理员跳转到组织架构如果不是则跳转到部门管理
                AppConfig.IS_HIERARCHY = false;
                AppConfig.DEPT_MENBER_MUTIL = false;
                if ("1".equals(MyApplication.getInstance().getUm().getData().getUserInfo().getIsAdmin())) {//直接跳转到部门管理界面
                    startActivity(new Intent(getActivity(), OrganizationActivity.class));
                } else {//跳转到组织架构
                    Intent intentOrg = new Intent(getActivity(), DeptManagerActivity.class);
                    intentOrg.putExtra("unit_id", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
                    startActivity(intentOrg);
                }
                break;
            case 3:
                startActivity(new Intent(getActivity(), ChatGroupActivity.class));
                break;
            case 4:
                startActivity(new Intent(getActivity(), UnallocatedDeptMenberActivity.class).putExtra("where_from", "fragment").putExtra("unitId", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo()));
                break;
            default:
                Intent intent = new Intent(getActivity(), ContactsDetailsActivity.class);
                intent.putExtra("user_no", listUser.get(position).getCm().getUserNo());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
