package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.dept.FindDepUserListModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;

import java.util.HashMap;

import butterknife.Bind;

/**
 * 部门设置界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月18日 18:38:56
 */
public class DeptSettingsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.ll_dept_manager)
    RelativeLayout rlDeptManager;
    @Bind(R.id.tv_dept_manager_value)
    TextView tvDeptManagerValue;
    @Bind(R.id.img_dept_manager_value)
    ImageView imgDeptManager;
    @Bind(R.id.et_dept_name_value)
    EditText etDeptName;
    @Bind(R.id.ll_dept_delete)
    RelativeLayout llDeleteDept;
    private FindDepUserListModel.DepUser deptCharge;
    private String deptName, deptId;
    private int HTTP_UPDATE_DEPT = 107;
    private int HTTP_DELETE_DEPT = 108;
    private MyHttpHelper httpHelper;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_DELETE_DEPT) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    DeptSettingsActivity.this.setResult(1001);
                    DeptSettingsActivity.this.finish();
                } else {
                    Toastor.showToast(DeptSettingsActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_UPDATE_DEPT) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    DeptSettingsActivity.this.finish();
                } else {
                    Toastor.showToast(DeptSettingsActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_dept;
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
        Intent intent = this.getIntent();
        deptCharge = (FindDepUserListModel.DepUser) intent.getSerializableExtra("dept_charge");
        llBack.setVisibility(View.VISIBLE);
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText(getResources().getString(R.string.dt_save));
        tvMidTitle.setText(getResources().getString(R.string.dept_settings));
        tvBaseRight.setOnClickListener(this);
        llBack.setOnClickListener(this);
        rlDeptManager.setOnClickListener(this);
        llDeleteDept.setOnClickListener(this);
        deptName = intent.getStringExtra("dept_name");
        deptId = intent.getStringExtra("dept_id");
        etDeptName.setText(deptName);
        etDeptName.setSelection(deptName.length());
        if (null != deptCharge) {
            tvDeptManagerValue.setVisibility(View.VISIBLE);
            imgDeptManager.setVisibility(View.GONE);
            tvDeptManagerValue.setText(deptCharge.getName());
        } else {
            tvDeptManagerValue.setVisibility(View.GONE);
            imgDeptManager.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    private String name = "";
    private String phone = "";
    private String duty = "";

    @Override
    public void onActivityResumed(Activity activity) {
        if (!AppConfig.DEPT_MANAGER_NAME.equals("")) {
            name = AppConfig.DEPT_MANAGER_NAME;
            phone = AppConfig.DEPT_MANAGER_PHONE;
            duty = AppConfig.DEPT_NAMAGER_DUTY;
            tvDeptManagerValue.setVisibility(View.VISIBLE);
            imgDeptManager.setVisibility(View.GONE);
            tvDeptManagerValue.setText(name);
            AppConfig.DEPT_MANAGER_NAME = "";
        } else {
            name = phone = duty = AppConfig.DEPT_MANAGER_NAME;
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
            case R.id.tv_base_right:
                updateDeptSettings(deptId);
                break;
            case R.id.ll_dept_manager:
                Intent settingIntent = new Intent(DeptSettingsActivity.this, SettingDeptChargeActivity.class);
                settingIntent.putExtra("dept_id", deptId);
                settingIntent.putExtra("dept_name", deptName);
                startActivity(settingIntent);
                break;
            case R.id.ll_dept_delete:
                new AlertDialog(DeptSettingsActivity.this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Todo 删除部门
                        deleteDeptSettings(deptId);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setMsg("是否确认删除部门？").show();
                break;
            default:
                break;
        }
    }

    /*修改部门设置*/
    private void updateDeptSettings(String deptId) {
        if (TextUtils.isEmpty(name)) {
            if (deptCharge != null) {
                name = deptCharge.getName();
                phone = deptCharge.getUserPhone();
                duty = deptCharge.getDutyName();
                deptName = etDeptName.getText().toString();
                httpHelper.postStringBack(HTTP_UPDATE_DEPT, AppConfig.ADD_DEPT_MANAGER_BY_PHONE, setUpdateParames(deptId, etDeptName.getText().toString(), duty, name, phone), handler, BaseModel.class);
            } else {
                Toastor.showToast(DeptSettingsActivity.this, "存在必填项未填写，请填写后重试!");
            }
        } else {
            deptName = etDeptName.getText().toString();
            httpHelper.postStringBack(HTTP_UPDATE_DEPT, AppConfig.ADD_DEPT_MANAGER_BY_PHONE, setUpdateParames(deptId, etDeptName.getText().toString(), duty, name, phone), handler, BaseModel.class);
        }
    }

    /**
     * 更新部门参数配置
     *
     * @param deptId
     * @return
     */
    private HashMap<String, String> setUpdateParames(String deptId, String deptName, String duty, String name, String phone) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("depId", deptId);
        parames.put("duty", duty);
        parames.put("depName", deptName);
        parames.put("name", name);
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("userName", phone);
        return parames;
    }

    private void deleteDeptSettings(String deptid) {
        httpHelper.postStringBack(HTTP_DELETE_DEPT, AppConfig.DELETE_DEPT, setDeleteParames(deptId), handler, BaseModel.class);
    }

    /**
     * 删除部门参数设置
     *
     * @param deptId
     * @return
     */
    private HashMap<String, String> setDeleteParames(String deptId) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("depId", deptId);
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("unitId", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
        return parames;
    }
}
