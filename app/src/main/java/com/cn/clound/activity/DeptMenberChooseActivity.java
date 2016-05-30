package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.DeptDetailsChooseListViewAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.choose.RadioOrMultDeptUser;
import com.cn.clound.bean.dept.FindDepUserListModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 部门成员选择列表
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-27 19:09:39
 */
public class DeptMenberChooseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.dept_menber_choose_list_view)
    ListView listView;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
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
                            user.setHadChecked(false);
                            listUser.add(user);
                        }
                        adapter = new DeptDetailsChooseListViewAdapter(DeptMenberChooseActivity.this, listUser, null);
                        listView.setAdapter(adapter);
                    } else {
                        Toastor.showToast(DeptMenberChooseActivity.this, msg.obj.toString());
                    }
                } else {
                    Toastor.showToast(DeptMenberChooseActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 10000) {
                for (int i = 0; i < listUser.size(); i++) {
                    if (index == i) {
                        listUser.get(i).setHadChecked(true);
                    } else {
                        listUser.get(i).setHadChecked(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_dept_menber_choose;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        httpHelper = MyHttpHelper.getInstance(this);
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        progress = new CustomProgress(this, "加载中...");
        progress.show();
        Intent intent = getIntent();
        llBack.setVisibility(View.VISIBLE);
        tvBaseRight.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvBaseRight.setText("确定");
        tvBaseRight.setOnClickListener(this);
        deptName = intent.getStringExtra("org_name");
        tvMidTitle.setText(deptName);
        deptId = intent.getStringExtra("org_id");
        httpHelper.postStringBack(HTTP_FLAG_GET_MENBERS, AppConfig.GET_DEPT_MENBERS_BY_ID, setParames(deptId), handler, FindDepUserListModel.class);
        listView.setOnItemClickListener(DeptMenberChooseActivity.this);
    }

    private HashMap<String, String> setParames(String deptId) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("depId", deptId);
        parames.put("token", TelephoneUtil.getIMEI(DeptMenberChooseActivity.this));
        parames.put("unitId", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
        return parames;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

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
            case R.id.tv_base_right:
                if (index != -1) {
                    if (AppConfig.DEPT_MENBER_MUTIL) {
                        for (RadioOrMultDeptUser user : listUser) {
                            if (user.isHadChecked()) {
                                AppConfig.listTemp.add(user);
                                startActivity(new Intent(DeptMenberChooseActivity.this, ChoseChatRoomMenberActivity.class));
                                finish();
                            }
                        }
                    } else {
                        Intent backIntent = new Intent(DeptMenberChooseActivity.this, DeptSettingsActivity.class);
                        AppConfig.DEPT_MANAGER_NAME = listUser.get(index).getUser().getName();
                        AppConfig.DEPT_MANAGER_PHONE = listUser.get(index).getUser().getUserPhone();
                        AppConfig.DEPT_NAMAGER_DUTY = listUser.get(index).getUser().getDutyName();
                        startActivity(backIntent);
                        finish();
                    }
                } else {
                    Toastor.showToast(DeptMenberChooseActivity.this, "请选择要添加的成员!");
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
        handler.sendEmptyMessage(10000);
    }

    private void muiltSelecte(int position) {
        if (listUser.get(position).isHadChecked()) {
            listUser.get(position).setHadChecked(false);
        } else {
            listUser.get(position).setHadChecked(true);
        }
        adapter.notifyDataSetChanged();
    }
}
