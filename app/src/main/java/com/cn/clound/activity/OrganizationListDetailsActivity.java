package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.DeptDetailsListViewAdapter;
import com.cn.clound.adapter.DeptPepoleAdapter;
import com.cn.clound.adapter.DeptPepoleRecyclerAdapter;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.view.SwipeMenuRecyclerView;
import com.cn.clound.base.view.VerticalSpaceItemDecoration;
import com.cn.clound.bean.dept.FindDepUserListModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
import com.cn.clound.view.CustomProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 部门列表详情界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-18 16:57:53
 */
public class OrganizationListDetailsActivity extends BaseActivity implements View.OnClickListener, OnItemClickLitener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.swipe_menu_listView)
    SwipeMenuRecyclerView swipeListView;
    @Bind(R.id.tv_add_menber)
    TextView tvAddMenber;
    @Bind(R.id.tv_dept_settings)
    TextView tvDeptSettings;
    @Bind(R.id.ll_org_details_list)
    RelativeLayout rlOrgBootom;
    @Bind(R.id.ll_menu_mid_view)
    View midMenu;
    @Bind(R.id.normal_list_view)
    ListView listView;
    private List<FindDepUserListModel.DepUser> listUser = new ArrayList<FindDepUserListModel.DepUser>();
    private DeptPepoleRecyclerAdapter adapter;
    private DeptDetailsListViewAdapter listViewAdapter;
    private CustomProgress progress;
    private MyHttpHelper httpHelper;
    private int HTTP_FLAG_GET_MENBERS = 102;
    private int HTTP_FLAG_DELETE_MENBER = 103;
    private String deptId, deptName;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_FLAG_GET_MENBERS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    FindDepUserListModel um = (FindDepUserListModel) msg.obj;
                    if (listUser != null && listUser.size() > 0) {
                        listUser.clear();
                    }
                    if (um != null && um.getData().getDepUser().size() > 0) {
                        listUser.addAll(um.getData().getDepUser());
                        adapter = new DeptPepoleRecyclerAdapter(OrganizationListDetailsActivity.this, listUser, handler);
                        listViewAdapter = new DeptDetailsListViewAdapter(OrganizationListDetailsActivity.this, listUser);
                        adapter.setOnItemClickLitener(OrganizationListDetailsActivity.this);
                        listView.setAdapter(listViewAdapter);
                        swipeListView.setAdapter(adapter);
                    } else {
                        Toastor.showToast(OrganizationListDetailsActivity.this, "暂无数据");
                    }
                } else {
                    Toastor.showToast(OrganizationListDetailsActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_FLAG_DELETE_MENBER) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    OrganizationListDetailsActivity.this.setResult(1003, null);
                    getOrgList(deptId);
                } else {
                    Toastor.showToast(OrganizationListDetailsActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 200) {
                final int position = (int) msg.obj;
                new AlertDialog(OrganizationListDetailsActivity.this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MyApplication.getInstance().getUm().getData().getUserInfo().getDepNo().equals(listUser.get(position).getDepId())) {
                            //Todo 转移部门成员
                            transferDeptMenber(position);
                        } else {
                            Toastor.showToast(OrganizationListDetailsActivity.this, "您不是本部门的管理不能转移此部门成员");
                        }
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setMsg("是否将该成员转移到其他部门？").show();
            } else if (msg.what == 201) {
                final int position = (int) msg.obj;
                new AlertDialog(OrganizationListDetailsActivity.this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MyApplication.getInstance().getUm().getData().getUserInfo().getDepNo().equals(listUser.get(position).getDepId())) {
                            //Todo 删除部门成员
                            deleteDeptMenber(position);
                        } else {
                            Toastor.showToast(OrganizationListDetailsActivity.this, "您不是本部门的管理不能删除此部门成员");
                        }
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setMsg("是否确认删除部门成员").show();
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    /**
     * 转移部门成员
     *
     * @param position
     */
    private void transferDeptMenber(int position) {
        Intent transIntent = new Intent(this, DeptChooseListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dept_charge", listUser.get(position));
        transIntent.putExtras(bundle);
        OrganizationListDetailsActivity.this.setResult(1003, null);
        startActivity(transIntent);
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_org_list_details;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        httpHelper = MyHttpHelper.getInstance(OrganizationListDetailsActivity.this);
        init(activity);
    }

    /**
     * 初始化视图
     */
    private void init(Activity activity) {
        progress = new CustomProgress(this, "加载中...");
        progress.show();
        Intent intent = getIntent();
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        deptName = intent.getStringExtra("org_name");
        tvMidTitle.setText(deptName);
        deptId = intent.getStringExtra("org_id");
        aboutSettings();
        getOrgList(deptId);
        tvAddMenber.setOnClickListener(this);
        tvDeptSettings.setOnClickListener(this);
        swipeListView.setLayoutManager(new LinearLayoutManager(this));
        swipeListView.addItemDecoration(new VerticalSpaceItemDecoration(0));
        if (!AppConfig.IS_HIERARCHY && "1".equals(MyApplication.getInstance().getUm().getData().getUserInfo().getDuty())) {
            if (!MyApplication.getInstance().getUm().getData().getUserInfo().getDepNo().equals(deptId)) {
                listView.setVisibility(View.VISIBLE);
                swipeListView.setVisibility(View.GONE);
            } else {
                listView.setVisibility(View.GONE);
                swipeListView.setVisibility(View.VISIBLE);
            }
        } else {
            listView.setVisibility(View.VISIBLE);
            swipeListView.setVisibility(View.GONE);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(OrganizationListDetailsActivity.this, ContactsDetailsActivity.class).putExtra("user_no", listUser.get(position).getUserNo()));
            }
        });
    }

    /**
     * 获取部门联系人列表
     *
     * @return
     */
    private void getOrgList(String deptId) {
        httpHelper.postStringBack(HTTP_FLAG_GET_MENBERS, AppConfig.GET_DEPT_MENBERS_BY_ID, setParames(deptId), handler, FindDepUserListModel.class);
    }

    private HashMap<String, String> setParames(String deptId) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("depId", deptId);
        parames.put("token", TelephoneUtil.getIMEI(OrganizationListDetailsActivity.this));
        parames.put("unitId", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
        return parames;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        getOrgList(deptId);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6700 && resultCode == 1001) {
            OrganizationListDetailsActivity.this.setResult(1003, null);
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                finish();
                break;
            case R.id.tv_add_menber:
                OrganizationListDetailsActivity.this.setResult(1003, null);
                startActivity(new Intent(OrganizationListDetailsActivity.this, AddMenberActivity.class).putExtra("dept_id", deptId));
                break;
            case R.id.tv_dept_settings://部门设置
                Intent intent = new Intent(OrganizationListDetailsActivity.this, DeptSettingsActivity.class);
                Bundle bundle = new Bundle();
                if (listUser != null && listUser.size() > 0) {
                    if (listUser.get(0).getIsDepManger().equals("1")) {
                        bundle.putSerializable("dept_charge", listUser.get(0));
                    }
                }
                bundle.putString("dept_name", deptName);
                bundle.putString("dept_id", deptId);
                intent.putExtras(bundle);
                startActivityForResult(intent, 6700);
                break;
            default:
                break;
        }
    }

    private void aboutSettings() {
        if (AppConfig.IS_HIERARCHY) {
            rlOrgBootom.setVisibility(View.GONE);
        } else {
            if (("-1".equals(MyApplication.getInstance().getUm().getData().getUserInfo().getDuty()) || "2".equals(MyApplication.getInstance().getUm().getData().getUserInfo().getDuty())) && !"1".equals(MyApplication.getInstance().getUm().getData().getUserInfo().getIsAdmin())) {
                rlOrgBootom.setVisibility(View.GONE);
            } else {
                rlOrgBootom.setVisibility(View.VISIBLE);
                if ("1".equals(MyApplication.getInstance().getUm().getData().getUserInfo().getDuty()) && "-1".equals(MyApplication.getInstance().getUm().getData().getUserInfo().getIsAdmin())) {
                    tvDeptSettings.setVisibility(View.GONE);
                    midMenu.setVisibility(View.GONE);
                    if (!MyApplication.getInstance().getUm().getData().getUserInfo().getDepNo().equals(deptId)) {
                        rlOrgBootom.setVisibility(View.GONE);
                    } else {
                        rlOrgBootom.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvDeptSettings.setVisibility(View.VISIBLE);
                    midMenu.setVisibility(View.VISIBLE);
                    if (MyApplication.getInstance().getUm().getData().getUserInfo().getIsAdmin().equals("1")) {
                        tvDeptSettings.setVisibility(View.VISIBLE);
                        midMenu.setVisibility(View.VISIBLE);
                        tvAddMenber.setVisibility(View.VISIBLE);
                    } else {
                        if (!MyApplication.getInstance().getUm().getData().getUserInfo().getDepNo().equals(deptId)) {
                            tvAddMenber.setVisibility(View.GONE);
                            midMenu.setVisibility(View.GONE);
                        } else {
                            midMenu.setVisibility(View.VISIBLE);
                            tvAddMenber.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(OrganizationListDetailsActivity.this, ContactsDetailsActivity.class).putExtra("user_no", listUser.get(position).getUserNo()));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    /**
     * 删除部门成员
     *
     * @param
     */
    private void deleteDeptMenber(int position) {
        httpHelper.postStringBack(HTTP_FLAG_DELETE_MENBER, AppConfig.DELETE_DEPT_MENBER, setDeleteParames(position), handler, FindDepUserListModel.class);
    }

    private HashMap<String, String> setDeleteParames(int position) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("depId", deptId);
        parames.put("token", TelephoneUtil.getIMEI(OrganizationListDetailsActivity.this));
        parames.put("userNo", listUser.get(position).getUserNo());
        return parames;
    }
}
