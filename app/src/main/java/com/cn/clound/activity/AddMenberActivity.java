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
import com.cn.clound.base.common.utils.AndroidUtil;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.http.MyHttpHelper;

import java.util.HashMap;

import butterknife.Bind;

/**
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月21日 10:19:58
 */
public class AddMenberActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.img_menber_contact)
    ImageView imgContact;
    @Bind(R.id.et_menber_name)
    EditText etMenberName;
    @Bind(R.id.et_menber_phone)
    EditText etMenberPhone;
    @Bind(R.id.et_menber_duty)
    EditText etMenberDuty;

    private int HTTP_ADD_MENBER_BY_PHONE = 90001;
    private String depiId = "";
    private MyHttpHelper httpHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_ADD_MENBER_BY_PHONE) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    AddMenberActivity.this.finish();
                } else {
                    Toastor.showToast(AddMenberActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_add_menber;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(AddMenberActivity.this);
        llBack.setVisibility(View.VISIBLE);
        depiId = getIntent().getStringExtra("dept_id");
        tvMidTitle.setText("添加成员");
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("确定");
        tvBaseRight.setOnClickListener(this);
        llBack.setOnClickListener(this);
        imgContact.setOnClickListener(this);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (PublicDataUtil.addMenberData != null) {
            etMenberName.setText(PublicDataUtil.addMenberData.getName());
            etMenberPhone.setText(PublicDataUtil.addMenberData.getPhone());
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
        if (requestCode == 101 && resultCode == 1000) {
            etMenberName.setText(data.getStringExtra("contact_name").replace(" ", ""));
            etMenberPhone.setText(data.getStringExtra("contact_phone").replace(" ", ""));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                finish();
                break;
            case R.id.img_menber_contact:
                startActivityForResult(new Intent(AddMenberActivity.this, ChooseContactActivity.class).putExtra("add_witch", "menber").putExtra("deptId", depiId), 101);
                break;
            case R.id.tv_base_right:
                if ("".equals(etMenberName.getText().toString()) || "".equals(etMenberPhone.getText().toString()) || "".equals(etMenberDuty.getText().toString())) {
                    Toastor.showToast(AddMenberActivity.this, "存在必填项为空，请填写完成再试!");
                } else {
                    addDeptMenberByPhone();
                }
                break;
            default:
                break;
        }
    }

    private void addDeptMenberByPhone() {
        httpHelper.postStringBack(HTTP_ADD_MENBER_BY_PHONE, AppConfig.INSERT_DEP_USER_BY_PHONE, setParames(), handler, BaseModel.class);
    }

    /**
     * 通过手机号添加部门成员接口参数
     **/
    private HashMap<String, String> setParames() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("duty", etMenberDuty.getText().toString());
        parames.put("depId", depiId);
        parames.put("name", etMenberName.getText().toString());
        parames.put("userName", etMenberPhone.getText().toString());
        return parames;
    }
}
