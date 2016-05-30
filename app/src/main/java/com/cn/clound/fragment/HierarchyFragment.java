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
import com.cn.clound.activity.AAAAActivity;
import com.cn.clound.activity.DeptManagerActivity;
import com.cn.clound.activity.DeptSettingsActivity;
import com.cn.clound.adapter.ActFragCoon;
import com.cn.clound.adapter.FragActCoon;
import com.cn.clound.adapter.HierarchyListAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.hierarchy.FindUtilListModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 上下级单位碎片界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-28 12:04:42
 */
public class HierarchyFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ActFragCoon {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.lv_org_list_view)
    ListView listViewHierarchy;
    private List<FindUtilListModel.Unit> listdept = new ArrayList<FindUtilListModel.Unit>();
    private FragActCoon fragActCoon;
    private int HTTP_GET_HIERARCHY_LIST = 105;
    private MyHttpHelper httpHelper;
    private CustomProgress progress;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_HIERARCHY_LIST) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    FindUtilListModel um = (FindUtilListModel) msg.obj;
                    if (null != um.getData() && um.getData().getUnit().size() > 0) {
                        listdept.addAll(um.getData().getUnit());
                        listViewHierarchy.setAdapter(new HierarchyListAdapter(getActivity(), listdept));
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
        return R.layout.dt_fragment_hierarchy;
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
        AppConfig.IS_HIERARCHY = true;
        httpHelper = MyHttpHelper.getInstance(getActivity());
        getHierarchy();
        listViewHierarchy.setOnItemClickListener(this);
    }

    @Override
    public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentStarted(Fragment fragment) {

    }

    /**
     * 获取上下级组织关系
     */
    private void getHierarchy() {
        tvMidTitle.setText(getResources().getString(R.string.man_contact_header_up_down));
        if (listdept != null && listdept.size() > 0) {
            listdept.clear();
        }
        httpHelper.postStringBack(HTTP_GET_HIERARCHY_LIST, AppConfig.GET_HIERARHCY_LIST, getParames(), handler, FindUtilListModel.class);
    }

    /*获取上下级单位列表参数配置*/
    private HashMap<String, String> getParames() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(getActivity()));
        return parames;
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
                fragActCoon.fragToAct(this, new TopContactsFragment());
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AAAAActivity.otherUnitNo = listdept.get(position).getUnitNo();
        fragActCoon.fragToAct(this, new OrganizationFragment());
    }

    @Override
    public void actToFrag(List<BottomUserModel> listBottom) {

    }
}
