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
import com.cn.clound.adapter.HierarchyListAdapter;
import com.cn.clound.adapter.OrganizationAdapter;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 组织架构界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-14 18:01:44
 */
public class OrganizationActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.lv_org_list_view)
    ListView listViewHierarchy;
    private OrganizationAdapter adapter;

    private List<String> listString = new ArrayList<String>();

    private String[] name = new String[]{"部门管理", "单位负责人"};

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_organization_layout;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        tvMidTitle.setText(getResources().getString(R.string.man_contact_header_org));
        getOrganization();
    }

    /**
     * 初始化数据
     */
    private void getOrganization() {
        if (listString != null && listString.size() > 0) {
            listString.clear();
        }
        for (int i = 0; i < name.length; i++) {
            listString.add(name[i]);
        }
        adapter = new OrganizationAdapter(OrganizationActivity.this, listString);
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
                startActivity(new Intent(OrganizationActivity.this, DeptManagerActivity.class).putExtra("unit_id", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo()));
                break;
            case 1:
                startActivity(new Intent(OrganizationActivity.this, ResponsibleActivity.class));
                break;
            default:
                break;
        }
    }
}
