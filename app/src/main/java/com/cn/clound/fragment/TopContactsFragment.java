package com.cn.clound.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.activity.AAAAActivity;
import com.cn.clound.adapter.ActFragCoon;
import com.cn.clound.adapter.ChoseChatMenberAdapter;
import com.cn.clound.adapter.FragActCoon;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.PinyinUtils;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.User.DTUser;
import com.cn.clound.bean.User.FindTopContactsModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.utils.PinyinComparator;
import com.cn.clound.view.CustomProgress;
import com.cn.clound.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 常用联系人碎片界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-28 12:04:42
 */
public class TopContactsFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ActFragCoon {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_left)
    LinearLayout llleft;
    @Bind(R.id.tv_base_left)
    TextView tvBaseLeft;
    @Bind(R.id.main_contacts_sidebar)
    SideBar sidebar;
    @Bind(R.id.main_contacts_list)
    ListView contactsListView;
    @Bind(R.id.floating_header)
    TextView tvSideBarTitle;
    @Bind(R.id.query)
    EditText etQuery;

    private int isClear = 1;
    int[] header = new int[]{R.string.man_contact_header_up_down, R.string.man_contact_header_org};
    private List<DTUser> listUser = new ArrayList<DTUser>();
    private PinyinComparator pinyinComparator;
    private MyHttpHelper httpHelper;
    private ChoseChatMenberAdapter adapter;
    private int HTTP_GET_TOP_CONTACTS = 110;
    private FragActCoon fragActCoon;
    private CustomProgress progress;
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
                            if (PublicDataUtil.listBottom.size() > 0) {
                                if (PublicDataUtil.isContacs(PublicDataUtil.listBottom, user.getCm().getUserNo())) {
                                    user.setCheckedBox(true);
                                } else {
                                    user.setCheckedBox(false);
                                }
                            } else {
                                user.setCheckedBox(false);
                            }
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
                    adapter = new ChoseChatMenberAdapter(getActivity(), listUser, PublicDataUtil.listHasCunZai);
                    contactsListView.setAdapter(adapter);
                    contactsListView.setOnItemClickListener(TopContactsFragment.this);
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_fragment_chose_top_contacts;
    }

    @Override
    public void onFragmentAttach(Fragment fragment, Activity activity) {
        if (activity != null) {
            fragActCoon = (FragActCoon) activity;
        }
    }

    @Override
    public void onFragmentCreated(Fragment fragment, Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentCreateView(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
        init(view);
    }

    /**
     * 初始化视图
     */
    private void init(View view) {
        progress = new CustomProgress(getActivity(), "加载中...");
        progress.show();
        httpHelper = MyHttpHelper.getInstance(getActivity());
        tvMidTitle.setText("选择联系人");
        llleft.setVisibility(View.VISIBLE);
        llleft.setOnClickListener(this);
        tvBaseLeft.setText("取消");
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
    }

    private void getContacts() {
        if (listUser != null && listUser.size() > 0) {
            listUser.clear();
        }
        httpHelper.postStringBack(HTTP_GET_TOP_CONTACTS, AppConfig.GET_TOP_CONTACTS_LIST, setParames(), handler, FindTopContactsModel.class);
    }

    private HashMap<String, String> setParames() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(getActivity()));
        return parames;
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

    @Override
    public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentStarted(Fragment fragment) {

    }

    @Override
    public void onFragmentResumed(Fragment fragment) {
        AAAAActivity.otherUnitNo = "";
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                fragActCoon.fragToAct(new TopContactsFragment(), new HierarchyFragment());
                break;
            case 1:
                fragActCoon.fragToAct(new TopContactsFragment(), new OrganizationFragment());
                break;
            default:
                if (listUser.get(position).isCheckedBox()) {
                    listUser.get(position).setCheckedBox(false);
                } else {
                    listUser.get(position).setCheckedBox(true);
                }
                adapter.notifyDataSetChanged();
                changeBottomMenber(position);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_left:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    /**
     * 改变底部成员列表数据
     */
    private void changeBottomMenber(int index) {
        BottomUserModel bum = new BottomUserModel();
        bum.setUserHead(listUser.get(index).getCm().getHead());
        bum.setUserId(listUser.get(index).getCm().getUserNo());
        bum.setUserName(listUser.get(index).getCm().getName());
        if (!PublicDataUtil.isContacs(PublicDataUtil.listBottom, bum.getUserId()) && listUser.get(index).isCheckedBox()) {
            PublicDataUtil.listBottom.add(bum);
        } else if (PublicDataUtil.isContacs(PublicDataUtil.listBottom, bum.getUserId())) {
            PublicDataUtil.delete(PublicDataUtil.listBottom, bum.getUserId());
        }
        fragActCoon.fragToAct(PublicDataUtil.listBottom);
    }

    @Override
    public void actToFrag(List<BottomUserModel> listBottom) {
        getContacts();
    }
}
