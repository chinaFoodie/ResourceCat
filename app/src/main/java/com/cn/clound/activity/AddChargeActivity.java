package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.http.MyHttpHelper;

import java.util.HashMap;

import butterknife.Bind;

/**
 * 添加主管界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-21 10:06:21
 */
public class AddChargeActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.img_charge_contact)
    ImageView imgChooseContact;
    @Bind(R.id.et_charge_name)
    EditText etChargeName;
    @Bind(R.id.et_charge_phone)
    EditText etChargePhone;
    @Bind(R.id.et_charge_duty)
    EditText etChargeDuty;
    private String deptId, deptName;

    private int HTTP_ADD_MANAGER_BY_PHONE = 114;
    private MyHttpHelper httpHelper;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_ADD_MANAGER_BY_PHONE) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Intent backIntent = new Intent(AddChargeActivity.this, OrganizationListDetailsActivity.class);
                    startActivity(backIntent);
                    finish();
                } else {
                    Toastor.showToast(AddChargeActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_add_charge;
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
        tvMidTitle.setText("添加主管");
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("确定");
        tvBaseRight.setOnClickListener(this);
        llBack.setOnClickListener(this);
        imgChooseContact.setOnClickListener(this);
        Intent preIntent = this.getIntent();
        deptId = preIntent.getStringExtra("dept_id");
        deptName = preIntent.getStringExtra("dept_name");
        httpHelper = MyHttpHelper.getInstance(this);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (PublicDataUtil.addMenberData != null) {
            etChargeName.setText(PublicDataUtil.addMenberData.getName());
            etChargePhone.setText(PublicDataUtil.addMenberData.getPhone());
            PublicDataUtil.addMenberData = null;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6701 && resultCode == 1000) {
            etChargeName.setText(data.getStringExtra("contact_name").replace(" ", ""));
            etChargePhone.setText(data.getStringExtra("contact_phone").replace(" ", ""));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                finish();
                break;
            case R.id.img_charge_contact:
                startActivityForResult(new Intent(AddChargeActivity.this, ChooseContactActivity.class).putExtra("add_witch", "charge"), 6701);
                break;
            case R.id.tv_base_right:
                if ("".equals(etChargeName) || "".equals(etChargePhone.getText().toString()) || "".equals(etChargeDuty.getText().toString())) {
                    Toastor.showToast(AddChargeActivity.this, "存在必填项为空，请填写完成再试!");
                } else {
                    Intent backIntent = new Intent(AddChargeActivity.this, DeptSettingsActivity.class);
                    AppConfig.DEPT_MANAGER_NAME = etChargeName.getText().toString().replace(" ", "");
                    AppConfig.DEPT_MANAGER_PHONE = etChargePhone.getText().toString().replace(" ", "");
                    AppConfig.DEPT_NAMAGER_DUTY = etChargeDuty.getText().toString().replace(" ", "");
                    startActivity(backIntent);
                    finish();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 通过手机号添加部门主管配置参数
     *
     * @param deptId
     * @param deptName
     * @return
     */
    private HashMap<String, String> setParames(String deptId, String deptName) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("depId", deptId);
        parames.put("depName", deptName);
        parames.put("duty", etChargeDuty.getText().toString().replace(" ", ""));
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("name", etChargeName.getText().toString().replace(" ", ""));
        parames.put("userName", etChargePhone.getText().toString().replace(" ", ""));
        return parames;
    }
}
