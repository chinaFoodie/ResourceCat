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
import com.cn.clound.activity.AddChargeActivity;
import com.cn.clound.activity.AddOrgActivity;
import com.cn.clound.activity.ContactsDetailsActivity;
import com.cn.clound.interfaces.ActFragCoon;
import com.cn.clound.adapter.DeptmanagerAdapter;
import com.cn.clound.interfaces.FragActCoon;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.dept.DeptManager;
import com.cn.clound.bean.dept.FindDepListModel;
import com.cn.clound.bean.dept.FindUnitMangerListModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * 组织架构碎片界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-28 12:04:42
 */
public class OrganizationFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ActFragCoon {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.lv_dept_list_view)
    ListView listView;
    private FragActCoon fragActCoon;
    private MyHttpHelper httpHelper;
    private List<DeptManager> list = new ArrayList<DeptManager>();

    private int HTTP_GET_MANAGER_LIST = 101;
    private boolean NEED_REFURESE = false;
    private DeptmanagerAdapter adapter;
    private int JUMP_FLAG;
    public static DeptManager deptManager = null;
    private CustomProgress progress;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_MANAGER_LIST) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    FindUnitMangerListModel mm = (FindUnitMangerListModel) msg.obj;
                    if (null != mm.getData().getUser() && mm.getData().getUser().size() > 0) {
                        for (int i = 0; i < mm.getData().getUser().size(); i++) {
                            JUMP_FLAG = mm.getData().getUser().size();
                            DeptManager dm = new DeptManager();
                            dm.setUm(mm.getData().getUser().get(i));
                            if (PublicDataUtil.listBottom.size() > 0) {
                                if (PublicDataUtil.isContacs(PublicDataUtil.listBottom, dm.getUm().getUserNo())) {
                                    dm.setCheckedHas(true);
                                } else {
                                    dm.setCheckedHas(false);
                                }
                            } else {
                                dm.setCheckedHas(false);
                            }
                            list.add(i, dm);
                        }
                    }
                    adapter = new DeptmanagerAdapter(getActivity(), list, JUMP_FLAG, PublicDataUtil.listHasCunZai);
                    listView.setAdapter(adapter);
                    NEED_REFURESE = false;
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
                if (progress.isShowing() && progress != null) {
                    progress.dismiss();
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_fragment_chose_organization;
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
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText(getResources().getString(R.string.contact_org_list));
        progress = new CustomProgress(getActivity(), "加载中...");
        progress.show();
        if (AAAAActivity.otherUnitNo.equals("")) {
            getDeptList(MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
        } else {
            getDeptList(AAAAActivity.otherUnitNo);
        }
    }

    private void getDeptList(final String unitNo) {
        OkHttpUtils.post().url(AppConfig.GET_DEPT_LIST_BY_ID).addParams("token", TelephoneUtil.getIMEI(getActivity())).addParams("unitId", unitNo).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toastor.showToast(getActivity(), e.toString());
            }

            @Override
            public void onResponse(String response) {
                String temp = response;
                try {
                    JSONObject json = new JSONObject(temp);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    if (AppConfig.SUCCESS.equals(status)) {
                        FindDepListModel dept = GsonTools.getPerson(temp, FindDepListModel.class);
                        if (dept != null && dept.getData().getDep().size() > 0) {
                            if (NEED_REFURESE && list.size() > 0) {
                                list.clear();
                            }
                            for (int i = 0; i < dept.getData().getDep().size(); i++) {
                                DeptManager dm = new DeptManager();
                                dm.setDm(dept.getData().getDep().get(i));
//                                dm.setCheckedHas(false);
                                list.add(dm);
                            }
                        }
                        httpHelper.postStringBack(HTTP_GET_MANAGER_LIST, AppConfig.GET_DEPT_MANAGER_LISTBY_ID, setParames(unitNo), handler, FindUnitMangerListModel.class);
                    } else {
                        Toastor.showToast(getActivity(), message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setOnItemClickListener(this);
    }

    private HashMap<String, String> setParames(String unitNo) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(getActivity()));
        parames.put("unitId", unitNo);
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
                fragActCoon.fragToAct(new OrganizationFragment(), new TopContactsFragment());
                break;
            case R.id.tv_add_charge://跳转添加主管界面
                startActivity(new Intent(getActivity(), AddChargeActivity.class));
                break;
            case R.id.tv_add_dept://跳转添加部门界面
                startActivityForResult(new Intent(getActivity(), AddOrgActivity.class), 102);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < JUMP_FLAG) {
            if (AppConfig.DEPT_MENBER_MUTIL) {
                if (list.get(position).isCheckedHas()) {
                    list.get(position).setCheckedHas(false);
                } else {
                    list.get(position).setCheckedHas(true);
                }
                adapter.notifyDataSetChanged();
                changeBottomMenber(position);
            } else {
                startActivity(new Intent(getActivity(), ContactsDetailsActivity.class).putExtra("user_no", list.get(position).getUm().getUserNo()));
            }
        } else {
            fragActCoon.fragToAct(new OrganizationFragment(), new DeptMenberFragment());
            deptManager = list.get(position);
        }
    }

    /**
     * 改变底部成员列表数据
     */
    private void changeBottomMenber(int index) {
        BottomUserModel bum = new BottomUserModel();
        bum.setUserHead(list.get(index).getUm().getUserHead());
        bum.setUserId(list.get(index).getUm().getUserNo());
        bum.setUserName(list.get(index).getUm().getName());
        if (!PublicDataUtil.isContacs(PublicDataUtil.listBottom, bum.getUserId()) && list.get(index).isCheckedHas()) {
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
        for (DeptManager dm : list) {
            if (dm.getUm() != null && PublicDataUtil.isContacs(listBottom, dm.getUm().getUserNo())) {
                dm.setCheckedHas(true);
            } else {
                dm.setCheckedHas(false);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}