package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.http.MyHttpHelper;

import java.util.HashMap;

import butterknife.Bind;

/**
 * 添加部门界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-18 12:17:32
 */
public class AddOrgActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.et_add_dept_name)
    EditText etDeptName;

    private int HTTP_ADD_DEPT = 106;
    private MyHttpHelper httpHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_ADD_DEPT) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    AddOrgActivity.this.finish();
                } else {
                    Toastor.showToast(AddOrgActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_add_org_layout;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init(activity);
    }

    /**
     * 初始化视图
     */
    private void init(Activity activity) {
        llBack.setVisibility(View.VISIBLE);
        tvBaseRight.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText(getResources().getString(R.string.add_org));
        tvBaseRight.setOnClickListener(this);
        tvBaseRight.setText("保存");
        httpHelper = MyHttpHelper.getInstance(this);
        AddOrgActivity.this.setResult(1003, null);
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
                if (!"".equals(etDeptName.getText().toString())) {
                    doAddDept(etDeptName.getText().toString());
                } else {
                    Toastor.showToast(AddOrgActivity.this, "存在必填项为空，请填写完成再试!");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 添加部门操作
     */
    private void doAddDept(String depName) {
        httpHelper.postStringBack(HTTP_ADD_DEPT, AppConfig.ADD_DEPT, setParames(depName), handler, BaseModel.class);
    }

    private HashMap<String, String> setParames(String depName) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("depName", depName);
        parames.put("unitId", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
        return parames;
    }
}
