package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.interfaces.OnItemClickListener;
import com.cn.clound.adapter.ResponsibleAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.view.SwipeMenuRecyclerView;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.dept.FindUnitMangerListModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
import com.cn.clound.view.CustomProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 单位负责人
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-21 12:20:10
 */
public class ResponsibleActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {
    @Bind(R.id.responsible_list_view)
    SwipeMenuRecyclerView listView;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_add_responsible_bottom)
    RelativeLayout rlAddResponsible;
    @Bind(R.id.responsible_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int HTTP_GET_RESPONSIBLE_LIST = 103;
    private int HTTP_DELETE_DEPT_MANAGER = 113;
    private MyHttpHelper httpHelper;
    private CustomProgress progress;
    private List<FindUnitMangerListModel.UserListModel> listUser = new ArrayList<FindUnitMangerListModel.UserListModel>();
    private ResponsibleAdapter adapter;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_RESPONSIBLE_LIST) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    FindUnitMangerListModel mm = (FindUnitMangerListModel) msg.obj;
                    if (listUser != null && listUser.size() > 0) {
                        listUser.clear();
                    }
                    if (null != mm.getData().getUser() && mm.getData().getUser().size() > 0) {
                        listUser.addAll(mm.getData().getUser());
                    }
                    adapter = new ResponsibleAdapter(ResponsibleActivity.this, listUser, handler);
                    listView.setAdapter(adapter);
                    adapter.setOnItemClickLitener(ResponsibleActivity.this);
                } else {
                    Toastor.showToast(ResponsibleActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_DELETE_DEPT_MANAGER) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    getResponsible();
                } else {
                    Toastor.showToast(ResponsibleActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 201) {
                final int position = (int) msg.obj;
                new AlertDialog(ResponsibleActivity.this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Todo 删除部门成员
                        deleteDeptManager(position);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setMsg("是否确认删除此单位负责人？").show();
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    /**
     * 删除单位负责人
     *
     * @param position
     */
    private void deleteDeptManager(int position) {
        httpHelper.postStringBack(HTTP_DELETE_DEPT_MANAGER, AppConfig.DELETE_DEPT_MANAGER, setDeleteParames(position), handler, BaseModel.class);
    }

    /**
     * 删除单位负责人设置参数
     *
     * @param position
     * @return
     */
    private HashMap<String, String> setDeleteParames(int position) {
        HashMap<String, String> delParames = new HashMap<String, String>();
        delParames.put("token", TelephoneUtil.getIMEI(this));
        delParames.put("userNo", listUser.get(position).getUserNo());
        return delParames;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_responsible;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        httpHelper = MyHttpHelper.getInstance(ResponsibleActivity.this);
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        progress = new CustomProgress(this, "加载中...");
        progress.show();
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText(getResources().getString(R.string.dt_responsible));
        rlAddResponsible.setOnClickListener(this);
        getResponsible();
        listView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setColorSchemeResources(com.hyphenate.easeui.R.color.holo_blue_bright, com.hyphenate.easeui.R.color.holo_green_light,
                com.hyphenate.easeui.R.color.holo_orange_light, com.hyphenate.easeui.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getResponsible();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 获取单位负责人
     */
    private void getResponsible() {
        httpHelper.postStringBack(HTTP_GET_RESPONSIBLE_LIST, AppConfig.GET_DEPT_MANAGER_LISTBY_ID, setParames(MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo()), handler, FindUnitMangerListModel.class);
    }

    private HashMap<String, String> setParames(String unitId) {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(ResponsibleActivity.this));
        parames.put("unitId", unitId);
        return parames;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        getResponsible();
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
            case R.id.ll_add_responsible_bottom:
                startActivity(new Intent(ResponsibleActivity.this, AddResponsibleActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(this, ContactsDetailsActivity.class).putExtra("user_no", listUser.get(position).getUserNo()));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
