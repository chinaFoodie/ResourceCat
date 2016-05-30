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
import com.cn.clound.adapter.HierarchyListAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.dept.DTDept;
import com.cn.clound.bean.hierarchy.FindUtilListModel;
import com.cn.clound.http.MyHttpHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 上下级单位详情界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-15 14:58:18
 */
public class HierarchyActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_right)
    LinearLayout llBaseRight;
    @Bind(R.id.lv_org_list_view)
    ListView listViewHierarchy;
    private List<FindUtilListModel.Unit> listdept = new ArrayList<FindUtilListModel.Unit>();

    private int HTTP_GET_HIERARCHY_LIST = 105;
    private MyHttpHelper httpHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_HIERARCHY_LIST) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    FindUtilListModel um = (FindUtilListModel) msg.obj;
                    if (null != um.getData() && um.getData().getUnit().size() > 0) {
                        listdept.addAll(um.getData().getUnit());
                        listViewHierarchy.setAdapter(new HierarchyListAdapter(HierarchyActivity.this, listdept));
                    }
                } else {
                    Toastor.showToast(HierarchyActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_hierarchy;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        llBaseRight.setOnClickListener(this);
        AppConfig.IS_HIERARCHY = true;
        httpHelper = MyHttpHelper.getInstance(this);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        getHierarchy();
        listViewHierarchy.setOnItemClickListener(this);
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
        parames.put("token", TelephoneUtil.getIMEI(this));
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
            case R.id.ll_base_right:
                startActivity(new Intent(HierarchyActivity.this, DeptSettingsActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*跳转到部门管理界面*/
        startActivity(new Intent(HierarchyActivity.this, DeptManagerActivity.class).putExtra("unit_id", listdept.get(position).getUnitNo()));
    }
}
