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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.ChooseContactsAdapter;
import com.cn.clound.adapter.PhoneContactsAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.contacts.ContactModel;
import com.cn.clound.base.common.contacts.ContactsHelper;
import com.cn.clound.base.common.utils.PinyinUtils;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.User.PhoneContacts;
import com.cn.clound.bean.dept.UnCallocateMenberModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.utils.PhoneContactsComparator;
import com.cn.clound.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 选择联系人界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-21 11:25:28
 */
public class ChooseContactActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.join_contacts_list)
    ListView listViewJoin;
    @Bind(R.id.join_contacts_sidebar)
    SideBar sideBar;
    @Bind(R.id.join_contacts_floating_header)
    TextView tvFloat;
    @Bind(R.id.rl_choose_contact_un_allocated)
    RelativeLayout rlChooseUnAllocated;

    private ChooseContactsAdapter adapter;
    private int HTTP_GET_ALL_MENBER_BY_UNITID = 131;
    private String addWitch;
    private MyHttpHelper httpHelper;
    private PhoneContactsComparator pinyinComparator;
    private ContactsHelper contactsHelper = new ContactsHelper(ChooseContactActivity.this);
    private List<PhoneContacts> listPc = new ArrayList<PhoneContacts>();
    private List<UnCallocateMenberModel.CallocateMenberModel.UnCallocateMenber> listCheck = new ArrayList<UnCallocateMenberModel.CallocateMenberModel.UnCallocateMenber>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_ALL_MENBER_BY_UNITID) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    UnCallocateMenberModel ucmm = (UnCallocateMenberModel) msg.obj;
                    if (ucmm != null && ucmm.getData().getResult().size() > 0) {
                        listCheck = ucmm.getData().getResult();
                        adapter = new ChooseContactsAdapter(ChooseContactActivity.this, listPc, listCheck);
                        listViewJoin.setAdapter(adapter);
                    } else {

                    }
                } else {
                    Toastor.showToast(ChooseContactActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_choose_contact;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        tvMidTitle.setText("选择联系人");
        llBack.setOnClickListener(this);
        rlChooseUnAllocated.setOnClickListener(this);
        sideBar.setTextView(tvFloat);
        addWitch = this.getIntent().getStringExtra("add_witch");
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listViewJoin.setSelection(position);
                }
            }
        });
        pinyinComparator = new PhoneContactsComparator();
        getPhoneContact();
    }

    private HashMap<String, String> getMenberParemas() {
        HashMap<String, String> paremas = new HashMap<String, String>();
        paremas.put("token", TelephoneUtil.getIMEI(this));
        paremas.put("unitId", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
        return paremas;
    }

    private void getPhoneContact() {
        if (listPc != null && listPc.size() > 0) {
            listPc.clear();
        }
        PhoneContacts pc;
        List<ContactModel> listModel = contactsHelper.getContactsList();
        for (int i = 0; i < listModel.size(); i++) {
            pc = new PhoneContacts();
            String name = listModel.get(i).getContactName();
            pc.setName(name);
            pc.setPhoneNo(listModel.get(i).getPhoneNumber());
            String temp = PinyinUtils.getPinYin(listModel.get(i).getContactName()).toUpperCase();
            pc.setSortLetter(temp);
//            pc.setFilter(PinyinUtils.getPinYinHeadChar(listModel.get(i).getContactName()).substring(0, 1).toUpperCase());
            listPc.add(pc);
        }
        Collections.sort(listPc, pinyinComparator);
        httpHelper.postStringBack(HTTP_GET_ALL_MENBER_BY_UNITID, AppConfig.GET_ALL_UNIT_MENBER_BY_UNITID, getMenberParemas(), handler, UnCallocateMenberModel.class);
        listViewJoin.setOnItemClickListener(this);
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
            case R.id.rl_choose_contact_un_allocated:
                startActivity(new Intent(this, UnallocatedDeptMenberActivity.class).putExtra("add_witch", addWitch).putExtra("where_from", "choose").putExtra("unitId", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo()));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("contact_name", listPc.get(position).getName());
        //设置返回数据
        intent.putExtra("contact_phone", listPc.get(position).getPhoneNo());
        ChooseContactActivity.this.setResult(1000, intent);
        //关闭Activity
        ChooseContactActivity.this.finish();
        finish();
    }
}
