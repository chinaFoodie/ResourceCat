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
import com.cn.clound.adapter.DeptmanagerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.dept.DeptManager;
import com.cn.clound.bean.dept.FindDepListModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 部门选择列表
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-27 15:58:35
 */
public class DeptChooseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.lv_dept_choose_view)
    ListView listView;
    private int HTTP_GET_DEPT_LIST = 115;
    private List<DeptManager> list = new ArrayList<DeptManager>();
    private MyHttpHelper httpHelper;
    private CustomProgress progress;

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
                        DeptmanagerAdapter adapter = new DeptmanagerAdapter(DeptChooseActivity.this, list, 0, null);
                        listView.setAdapter(adapter);
                    }
                } else {
                    Toastor.showToast(DeptChooseActivity.this, msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_dept_choose;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        progress = new CustomProgress(this, "加载中...");
        progress.show();
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText("部门列表");
        tvBaseRight.setVisibility(View.GONE);
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
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(DeptChooseActivity.this, DeptMenberChooseActivity.class);
        intent.putExtra("org_name", list.get(position).getDm().getDepName());
        intent.putExtra("org_id", list.get(position).getDm().getDepNo());
        startActivity(intent);
    }
}
