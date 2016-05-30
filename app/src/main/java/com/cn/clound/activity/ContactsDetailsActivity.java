package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
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
import com.cn.clound.bean.User.UserModel;
import com.cn.clound.easemob.Constant;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;
import com.hyphenate.easeui.model.ExtendedChatModel;

import java.util.HashMap;

import butterknife.Bind;

/**
 * Class Of ContactsDtailsActivity
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-15 15:10:05
 */
public class ContactsDetailsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    private int HTTP_GET_USERINFO = 104;
    private MyHttpHelper httpHelper;
    private UserModel um;
    @Bind(R.id.tv_contact_duty)
    TextView tvContactDuty;
    @Bind(R.id.tv_contact_address)
    TextView tvContactAddress;
    @Bind(R.id.tv_contact_dept)
    TextView tvContactDept;
    @Bind(R.id.contact_details_call)
    RelativeLayout rlCall;
    @Bind(R.id.contact_details_send_msg)
    RelativeLayout rlSendMessage;
    @Bind(R.id.img_contact_details_add_or_delete)
    ImageView imgAddOrDelete;
    @Bind(R.id.ll_bottom)
    RelativeLayout rlBottom;

    private int HTTP_ADD_TOP_CONTACTS = 111;
    public static boolean TOP_NEED_LOAD = false;
    private CustomProgress progress;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_USERINFO) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    um = (UserModel) msg.obj;
                    tvMidTitle.setText(um.getData().getUserInfo().getName());
                    tvContactDuty.setText(um.getData().getUserInfo().getDutyName());
                    tvContactAddress.setText(um.getData().getUserInfo().getCityName());
                    tvContactDept.setText(um.getData().getUserInfo().getUnitName());
                    if ("1".equals(um.getData().getUserInfo().getIsTop()) || um.getData().getUserInfo().getUserNo().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo())) {
                        imgAddOrDelete.setVisibility(View.GONE);
                    } else {
                        imgAddOrDelete.setVisibility(View.VISIBLE);
                        imgAddOrDelete.setImageResource(R.mipmap.dt_img_add_contacts);
                    }
                } else {
                    Toastor.showToast(ContactsDetailsActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_ADD_TOP_CONTACTS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    TOP_NEED_LOAD = true;
                    Toastor.showToast(ContactsDetailsActivity.this, "添加成功");
                    getUserInfo(um.getData().getUserInfo().getUserNo());
                } else {
                    Toastor.showToast(ContactsDetailsActivity.this, msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_contacts_details;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        progress = new CustomProgress(this, "加载中...");
        httpHelper = MyHttpHelper.getInstance(this);
        Intent intent = getIntent();
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        rlCall.setOnClickListener(this);
        imgAddOrDelete.setOnClickListener(this);
        rlSendMessage.setOnClickListener(this);
        progress.show();
        if (intent.getStringExtra("user_no").equals(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo())) {
            rlBottom.setVisibility(View.GONE);

        } else {
            rlBottom.setVisibility(View.VISIBLE);
        }
        getUserInfo(intent.getStringExtra("user_no"));
    }

    private void getUserInfo(String user_no) {
        httpHelper.postStringBack(HTTP_GET_USERINFO, AppConfig.GET_USER_INFO, setParames(user_no), handler, UserModel.class);
    }

    private HashMap<String, String> setParames(String user_no) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("userNo", user_no);
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
            case R.id.contact_details_call:
                doCall(um.getData().getUserInfo().getPhone());
                break;
            case R.id.contact_details_send_msg:
                Intent intent = new Intent(this, DtChatActivity.class);
                ExtendedChatModel ecm = new ExtendedChatModel();
                ecm.setMsgType("0");
                ecm.setSessionName(um.getData().getUserInfo().getName());
                ecm.setToUSerNick(um.getData().getUserInfo().getName());
                ecm.setToUserNo(um.getData().getUserInfo().getUserNo());
                ecm.setToUserAvatar(um.getData().getUserInfo().getHead());
                ecm.setFromUserAvatar(MyApplication.getInstance().getUm().getData().getUserInfo().getHead());
                ecm.setFromUserNick(MyApplication.getInstance().getUm().getData().getUserInfo().getName());
                ecm.setFromUserNo(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                intent.putExtra(Constant.EXTRA_USER_ID, um.getData().getUserInfo().getImUser());
                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_SINGLE);
                Bundle bundle = new Bundle();
                bundle.putSerializable("extended_chat_model", ecm);
                intent.putExtras(bundle);
                startActivity(intent);
                this.finish();
                break;
            case R.id.img_contact_details_add_or_delete:
                if ("1".equals(um.getData().getUserInfo().getIsTop())) {
                    //Todo 删除常用联系人
                } else {
                    // Todo 添加常用联系人
                    httpHelper.postStringBack(HTTP_ADD_TOP_CONTACTS, AppConfig.ADD_TOP_CONTACTS, addTopParames(um.getData().getUserInfo().getUserNo()), handler, BaseModel.class);
                }
                break;
            default:
                break;
        }
    }

    /***
     * 添加常用联系人接口
     **/
    private HashMap<String, String> addTopParames(String userNo) {
        HashMap<String, String> addParames = new HashMap<String, String>();
        addParames.put("token", TelephoneUtil.getIMEI(this));
        addParames.put("toUser", userNo);
        return addParames;
    }


    /**
     * 打电话
     *
     * @param phone
     */
    private void doCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}
