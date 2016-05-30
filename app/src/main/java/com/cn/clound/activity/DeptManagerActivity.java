package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.DeptmanagerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.dept.DeptManager;
import com.cn.clound.bean.dept.FindDepListModel;
import com.cn.clound.bean.dept.FindUnitMangerListModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
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
 * 部门管理界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-20 10:53:17
 */
public class DeptManagerActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.lv_dept_list_view)
    ListView listView;
    @Bind(R.id.tv_add_charge)
    TextView tvAddCharge;
    @Bind(R.id.tv_add_dept)
    TextView tvAddDept;
    @Bind(R.id.ll_add_dept_bottom)
    RelativeLayout rlDeptBootom;
    @Bind(R.id.dept_manager_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private MyHttpHelper httpHelper;
    private CustomProgress progress;
    private List<DeptManager> list = new ArrayList<DeptManager>();

    private int HTTP_GET_MANAGER_LIST = 101;
    private boolean NEED_REFURESE = false;
    private DeptmanagerAdapter adapter;

    private int JUMP_FLAG;
    private String unitId;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_MANAGER_LIST) {
                progress.dismiss();
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    FindUnitMangerListModel mm = (FindUnitMangerListModel) msg.obj;
                    if (null != mm.getData().getUser() && mm.getData().getUser().size() > 0) {
                        for (int i = 0; i < mm.getData().getUser().size(); i++) {
                            JUMP_FLAG = mm.getData().getUser().size();
                            DeptManager dm = new DeptManager();
                            dm.setUm(mm.getData().getUser().get(i));
                            dm.setCheckedHas(false);
                            list.add(i, dm);
                        }
                    }
                    adapter = new DeptmanagerAdapter(DeptManagerActivity.this, list, JUMP_FLAG, null);
                    listView.setAdapter(adapter);
                    NEED_REFURESE = false;
                } else {
                    Toastor.showToast(DeptManagerActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_dept_manager;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        httpHelper = MyHttpHelper.getInstance(DeptManagerActivity.this);
        progress = new CustomProgress(DeptManagerActivity.this, "加载中...");
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llBack.setVisibility(View.VISIBLE);
        progress.show();
        llBack.setOnClickListener(this);
        tvMidTitle.setText(getResources().getString(R.string.contact_org_list));
        tvAddCharge.setOnClickListener(this);
        tvAddDept.setOnClickListener(this);
        aboutSettings();
        unitId = this.getIntent().getStringExtra("unit_id");
        getDeptList(unitId);
        swipeRefreshLayout.setColorSchemeResources(com.hyphenate.easeui.R.color.holo_blue_bright, com.hyphenate.easeui.R.color.holo_green_light,
                com.hyphenate.easeui.R.color.holo_orange_light, com.hyphenate.easeui.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NEED_REFURESE = true;
                getDeptList(unitId);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void aboutSettings() {
        if (AppConfig.IS_HIERARCHY) {
            rlDeptBootom.setVisibility(View.GONE);
        } else {
            if ("1".equals(MyApplication.getInstance().getUm().getData().getUserInfo().getIsAdmin())) {
                rlDeptBootom.setVisibility(View.VISIBLE);
            } else {
                rlDeptBootom.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取部门列表
     */
    private void getDeptList(final String unitId) {
        OkHttpUtils.post().url(AppConfig.GET_DEPT_LIST_BY_ID).addParams("token", TelephoneUtil.getIMEI(this)).addParams("unitId", unitId).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toastor.showToast(DeptManagerActivity.this, e.toString());
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
                                dm.setCheckedHas(false);
                                list.add(dm);
                            }
                        }
                        httpHelper.postStringBack(HTTP_GET_MANAGER_LIST, AppConfig.GET_DEPT_MANAGER_LISTBY_ID, setParames(unitId), handler, FindUnitMangerListModel.class);
                    } else {
                        Toastor.showToast(DeptManagerActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setOnItemClickListener(this);
    }

    private HashMap<String, String> setParames(String unitId) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(DeptManagerActivity.this));
        parames.put("unitId", unitId);
        return parames;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if ("1".equals(AppConfig.DEPT_TRANSFER_FLAG)) {
            new AlertDialog(DeptManagerActivity.this).builder().setTitle("温馨提示")
                    .setMsg(AppConfig.DEPT_TRANSFER_USER + "已经被成功转移到" + AppConfig.DEPT_TRANSFER_DEPTNAME)
                    .setNegativeButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
            AppConfig.DEPT_TRANSFER_FLAG = "";
            AppConfig.DEPT_TRANSFER_USER = "";
            AppConfig.DEPT_TRANSFER_DEPTNAME = "";
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                finish();
                break;
            case R.id.tv_add_charge://跳转添加主管界面
                startActivity(new Intent(DeptManagerActivity.this, AddChargeActivity.class));
                break;
            case R.id.tv_add_dept://跳转添加部门界面
                startActivityForResult(new Intent(DeptManagerActivity.this, AddOrgActivity.class), 102);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == 1003) {
            NEED_REFURESE = true;
            getDeptList(unitId);
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
            } else {
                startActivity(new Intent(DeptManagerActivity.this, ContactsDetailsActivity.class).putExtra("user_no", list.get(position).getUm().getUserNo()));
            }
        } else if (!AppConfig.DEPT_MENBER_MUTIL) {
            Intent intent = new Intent(DeptManagerActivity.this, OrganizationListDetailsActivity.class);
            intent.putExtra("org_name", list.get(position).getDm().getDepName());
            intent.putExtra("org_id", list.get(position).getDm().getDepNo());
            startActivityForResult(intent, 102);
        } else {
            Intent intent = new Intent(DeptManagerActivity.this, DeptMenberChooseActivity.class);
            intent.putExtra("org_name", list.get(position).getDm().getDepName());
            intent.putExtra("org_id", list.get(position).getDm().getDepNo());
            startActivity(intent);
        }
    }
}
