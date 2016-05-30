package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.OrganizationAdapter;
import com.cn.clound.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 设置部门主管
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-21 09:25:51
 */
public class SettingDeptChargeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.lv_org_list_view)
    ListView listViewHierarchy;
    private List<String> listString = new ArrayList<String>();
    private String[] name = new String[]{"手机号添加", "组织架构添加"};
    private OrganizationAdapter adapter;
    private String deptId, deptName;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_setting_charge;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llBack.setVisibility(View.VISIBLE);
        tvMidTitle.setText("设置部门主管");
        llBack.setOnClickListener(this);
        Intent preIntent = this.getIntent();
        deptId = preIntent.getStringExtra("dept_id");
        deptName = preIntent.getStringExtra("dept_name");
        initAddMethod();
    }

    private void initAddMethod() {
        if (listString != null && listString.size() > 0) {
            listString.clear();
        }
        for (int i = 0; i < name.length; i++) {
            listString.add(name[i]);
        }
        adapter = new OrganizationAdapter(SettingDeptChargeActivity.this, listString);
        listViewHierarchy.setAdapter(adapter);
        listViewHierarchy.setOnItemClickListener(this);
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
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent chargeIntent = new Intent(SettingDeptChargeActivity.this, AddChargeActivity.class);
                chargeIntent.putExtra("dept_id", deptId);
                chargeIntent.putExtra("dept_name", deptName);
                startActivity(chargeIntent);
                break;
            case 1:
                startActivity(new Intent(SettingDeptChargeActivity.this, DeptChooseActivity.class).putExtra("dept_id", deptId).putExtra("dept_name", deptName));
                break;
            default:
                break;
        }
    }
}
