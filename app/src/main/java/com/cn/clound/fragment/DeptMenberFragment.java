package com.cn.clound.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.activity.ChoseChatRoomMenberActivity;
import com.cn.clound.activity.DeptSettingsActivity;
import com.cn.clound.adapter.ActFragCoon;
import com.cn.clound.adapter.DeptDetailsChooseListViewAdapter;
import com.cn.clound.adapter.FragActCoon;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.choose.RadioOrMultDeptUser;
import com.cn.clound.bean.dept.FindDepUserListModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 部门列表详情碎片化界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月29日 14:26:51
 */
public class DeptMenberFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ActFragCoon {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.dept_menber_choose_list_view)
    ListView listView;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    private FragActCoon fragActCoon;
    private String deptId, deptName;
    private int index = -1;
    private MyHttpHelper httpHelper;
    private int HTTP_FLAG_GET_MENBERS = 116;
    private List<RadioOrMultDeptUser> listUser = new ArrayList<RadioOrMultDeptUser>();
    private DeptDetailsChooseListViewAdapter adapter;
    private CustomProgress progress;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == HTTP_FLAG_GET_MENBERS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    FindDepUserListModel um = (FindDepUserListModel) msg.obj;
                    if (listUser != null && listUser.size() > 0) {
                        listUser.clear();
                    }
                    if (um != null && um.getData().getDepUser().size() > 0) {
                        for (FindDepUserListModel.DepUser du : um.getData().getDepUser()) {
                            RadioOrMultDeptUser user = new RadioOrMultDeptUser();
                            user.setUser(du);
                            if (PublicDataUtil.listBottom.size() > 0) {
                                if (PublicDataUtil.isContacs(PublicDataUtil.listBottom, du.getUserNo())) {
                                    user.setHadChecked(true);
                                } else {
                                    user.setHadChecked(false);
                                }
                            } else {
                                user.setHadChecked(false);
                            }
                            listUser.add(user);
                        }
                        adapter = new DeptDetailsChooseListViewAdapter(getActivity(), listUser, PublicDataUtil.listHasCunZai);
                        listView.setAdapter(adapter);
                    } else {
                        Toastor.showToast(getActivity(), msg.obj.toString());
                    }
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
        return R.layout.dt_fragment_dept_menber;
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
        httpHelper = MyHttpHelper.getInstance(getActivity());
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        progress = new CustomProgress(getActivity(), "加载中...");
        progress.show();
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvBaseRight.setText("确定");
        tvBaseRight.setOnClickListener(this);
        deptName = OrganizationFragment.deptManager.getDm().getDepName();
        tvMidTitle.setText(deptName);
        deptId = OrganizationFragment.deptManager.getDm().getDepNo();
        httpHelper.postStringBack(HTTP_FLAG_GET_MENBERS, AppConfig.GET_DEPT_MENBERS_BY_ID, setParames(deptId), handler, FindDepUserListModel.class);
        listView.setOnItemClickListener(this);
    }

    private HashMap<String, String> setParames(String deptId) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("depId", deptId);
        parames.put("token", TelephoneUtil.getIMEI(getActivity()));
        parames.put("unitId", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                fragActCoon.fragToAct(this, new OrganizationFragment());
                break;
            case R.id.tv_base_right:
                if (index != -1) {
                    if (AppConfig.DEPT_MENBER_MUTIL) {
                        for (RadioOrMultDeptUser user : listUser) {
                            if (user.isHadChecked()) {
                                AppConfig.listTemp.add(user);
                                startActivity(new Intent(getActivity(), ChoseChatRoomMenberActivity.class));
                                getActivity().finish();
                            }
                        }
                    } else {
                        Intent backIntent = new Intent(getActivity(), DeptSettingsActivity.class);
                        AppConfig.DEPT_MANAGER_NAME = listUser.get(index).getUser().getName();
                        AppConfig.DEPT_MANAGER_PHONE = listUser.get(index).getUser().getUserPhone();
                        AppConfig.DEPT_NAMAGER_DUTY = listUser.get(index).getUser().getDutyName();
                        startActivity(backIntent);
                        getActivity().finish();
                    }
                } else {
                    Toastor.showToast(getActivity(), "请选择要添加的成员!");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        index = position;
        if (AppConfig.DEPT_MENBER_MUTIL) {
            muiltSelecte(position);
        } else {
            radioSelecte();
        }
    }

    /**
     * 单选操作
     */
    private void radioSelecte() {
        for (int i = 0; i < listUser.size(); i++) {
            if (index == i) {
                listUser.get(i).setHadChecked(true);
            } else {
                listUser.get(i).setHadChecked(false);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void muiltSelecte(int position) {
        if (listUser.get(position).isHadChecked()) {
            listUser.get(position).setHadChecked(false);
        } else {
            listUser.get(position).setHadChecked(true);
        }
        adapter.notifyDataSetChanged();
        changeBottomMenber(position);
    }

    /**
     * 改变底部成员列表数据
     */
    private void changeBottomMenber(int index) {
        BottomUserModel bum = new BottomUserModel();
        bum.setUserHead(listUser.get(index).getUser().getUserHead());
        bum.setUserId(listUser.get(index).getUser().getUserNo());
        bum.setUserName(listUser.get(index).getUser().getName());
        if (!PublicDataUtil.isContacs(PublicDataUtil.listBottom, bum.getUserId()) && listUser.get(index).isHadChecked()) {
            PublicDataUtil.listBottom.add(bum);
        } else if (PublicDataUtil.isContacs(PublicDataUtil.listBottom, bum.getUserId())) {
            PublicDataUtil.delete(PublicDataUtil.listBottom, bum.getUserId());
        }
        fragActCoon.fragToAct(PublicDataUtil.listBottom);
    }

    @Override
    public void actToFrag(List<BottomUserModel> listBottom) {
        refuser(listBottom);
    }

    private void refuser(List<BottomUserModel> listBottom) {
        for (RadioOrMultDeptUser rm : listUser) {
            if (PublicDataUtil.isContacs(listBottom, rm.getUser().getUserNo())) {
                rm.setHadChecked(true);
            } else {
                rm.setHadChecked(false);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
