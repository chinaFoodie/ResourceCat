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
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.http.MyHttpHelper;

import java.util.HashMap;

import butterknife.Bind;

/**
 * 添加单位负责人
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-21 12:34:33
 */
public class AddResponsibleActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.img_responsible_contact)
    ImageView imgChooseContact;
    @Bind(R.id.et_responsible_name)
    EditText etContactName;
    @Bind(R.id.et_responsible_phone)
    EditText etContactPhone;
    @Bind(R.id.et_responsible_duty)
    EditText etResponbileDuty;

    private int HTTP_ADD_RESPONBILE = 104;
    private MyHttpHelper httpHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_ADD_RESPONBILE) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    AddResponsibleActivity.this.finish();
                } else {
                    Toastor.showToast(AddResponsibleActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_add_responsible;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(AddResponsibleActivity.this);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText(getResources().getString(R.string.add_responsible));
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText(getResources().getString(R.string.dt_sure));
        tvBaseRight.setOnClickListener(this);
        imgChooseContact.setOnClickListener(this);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (PublicDataUtil.addMenberData != null) {
            etContactName.setText(PublicDataUtil.addMenberData.getName());
            etContactPhone.setText(PublicDataUtil.addMenberData.getPhone());
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
        if (requestCode == 100 && resultCode == 1000) {
            etContactName.setText(data.getStringExtra("contact_name"));
            etContactPhone.setText(data.getStringExtra("contact_phone"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                finish();
                break;
            case R.id.tv_base_right:
                if ("".equals(etContactName.getText().toString()) || "".equals(etContactPhone.getText().toString()) || "".equals(etResponbileDuty.getText().toString())) {
                    Toastor.showToast(AddResponsibleActivity.this, "存在必填项为空，请填写完成再试!");
                } else {
                    addDeptResponbileByPhone(etResponbileDuty.getText().toString(), etContactName.getText().toString(), etContactPhone.getText().toString());
                }
                break;
            case R.id.img_responsible_contact:
                startActivityForResult(new Intent(AddResponsibleActivity.this, ChooseContactActivity.class).putExtra("add_witch", "responsible"), 100);
                break;
            default:
                break;
        }
    }

    private void addDeptResponbileByPhone(String duty, String name, String username) {
        httpHelper.postStringBack(HTTP_ADD_RESPONBILE, AppConfig.ADD_RESPONBILE, setParames(duty, name, username), handler, BaseModel.class);
    }

    private HashMap<String, String> setParames(String duty, String name, String username) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("duty", duty);
        parames.put("name", name);
        parames.put("token", TelephoneUtil.getIMEI(AddResponsibleActivity.this));
        parames.put("unitId", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
        parames.put("userName", username);
        return parames;
    }
}
