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
import com.cn.clound.adapter.DeptChooseAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.dept.DeptManager;
import com.cn.clound.bean.dept.FindDepListModel;
import com.cn.clound.bean.dept.FindDepUserListModel;
import com.cn.clound.http.MyHttpHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 选择部门列表
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-27 20:04:16
 */
public class DeptChooseListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.lv_dept_choose_list_view)
    ListView listView;
    private int HTTP_GET_DEPT_LIST = 115;
    private List<DeptManager> list = new ArrayList<DeptManager>();
    private MyHttpHelper httpHelper;
    private FindDepUserListModel.DepUser deptCharge;
    private DeptChooseAdapter adapter;
    private int index = -1;
    private int HTTPTRANSFER_MENBER = 117;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_DEPT_LIST) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    FindDepListModel dept = (FindDepListModel) msg.obj;
                    if (dept != null && dept.getData().getDep().size() > 0) {
                        if (list.size() > 0) {
                            list.clear();
                        }
                        if (null != dept.getData().getDep() && dept.getData().getDep().size() > 0) {
                            for (int i = 0; i < dept.getData().getDep().size(); i++) {
                                DeptManager dm = new DeptManager();
                                dm.setDm(dept.getData().getDep().get(i));
                                list.add(i, dm);
                            }
                        }
                        adapter = new DeptChooseAdapter(DeptChooseListActivity.this, list, index);
                        listView.setAdapter(adapter);
                    }
                } else {
                    Toastor.showToast(DeptChooseListActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTPTRANSFER_MENBER) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    AppConfig.DEPT_TRANSFER_FLAG = "1";
                    AppConfig.DEPT_TRANSFER_DEPTNAME = list.get(index).getDm().getDepName();
                    AppConfig.DEPT_TRANSFER_USER = deptCharge.getName();
                    startActivity(new Intent(DeptChooseListActivity.this, DeptManagerActivity.class));
                    DeptChooseListActivity.this.finish();
                } else {
                    Toastor.showToast(DeptChooseListActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_dept_choose_list;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        Intent intent = this.getIntent();
        deptCharge = (FindDepUserListModel.DepUser) intent.getSerializableExtra("dept_charge");
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvBaseRight.setOnClickListener(this);
        tvMidTitle.setText("部门列表");
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("确定");
        httpHelper.postStringBack(HTTP_GET_DEPT_LIST, AppConfig.GET_DEPT_LIST_BY_ID, setGetParames(MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo()), handler, FindDepListModel.class);
        listView.setOnItemClickListener(this);
    }

    private HashMap<String, String> setGetParames(String unitId) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("unitId", unitId);
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
                httpHelper.postStringBack(HTTPTRANSFER_MENBER, AppConfig.TRANSFER_DEPT_MENBER, setTransParames(index), handler, BaseModel.class);
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> setTransParames(int position) {
        HashMap<String, String> transParames = new HashMap<String, String>();
        transParames.put("depId", deptCharge.getDepId());
        transParames.put("toDepId", list.get(position).getDm().getDepNo());
        transParames.put("token", TelephoneUtil.getIMEI(this));
        transParames.put("userNo", deptCharge.getUserNo());
        return transParames;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter = new DeptChooseAdapter(DeptChooseListActivity.this, list, position);
        index = position;
        listView.setAdapter(adapter);
    }
}
