package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.adapter.UnAllocatedDeptRecyclerAdapter;
import com.cn.clound.adapter.UnAllocatedDeptSwipeRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.netstate.NetWorkUtil;
import com.cn.clound.base.view.SwipeMenuRecyclerView;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.dept.UnCallocateMenberModel;
import com.cn.clound.easemob.db.InviteMessgeDao;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
import com.cn.clound.view.CustomProgress;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 未分配部门成员列表
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-19 14:55:08
 */
public class UnallocatedDeptMenberActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.unallocated_swipeRefreshLayout1)
    SwipeRefreshLayout swipeRefreshLayout1;
    @Bind(R.id.unallocated_swipeRefreshLayout2)
    SwipeRefreshLayout swipeRefreshLayout2;
    @Bind(R.id.recycler_unallocated_dept)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_menu_recyclerview)
    SwipeMenuRecyclerView swipeRecycler;
    private int HTTP_GET_UN_ALLOCATED_MENBER = 130;
    private int HTTP_DEL_UN_ALLOCATED_MENBER = 140;
    private UnAllocatedDeptRecyclerAdapter adapter;
    private UnAllocatedDeptSwipeRecyclerAdapter swipeAdapter;
    private MyHttpHelper httpHelper;
    private CustomProgress progress;
    private String addWitch;
    private String whereFrom;
    private List<UnCallocateMenberModel.CallocateMenberModel.UnCallocateMenber> list = new ArrayList<UnCallocateMenberModel.CallocateMenberModel.UnCallocateMenber>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_UN_ALLOCATED_MENBER) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    UnCallocateMenberModel ucmm = (UnCallocateMenberModel) msg.obj;
                    if (ucmm.getData().getResult() != null && ucmm.getData().getResult().size() > 0) {
                        list = ucmm.getData().getResult();
                        if (MyApplication.getInstance().getUm() != null && MyApplication.getInstance().getUm().getData().getUserInfo().getIsAdmin().equals("1")) {
                            swipeAdapter = new UnAllocatedDeptSwipeRecyclerAdapter(UnallocatedDeptMenberActivity.this, list, handler);
                            swipeRecycler.setAdapter(swipeAdapter);
                            swipeAdapter.setOnItemClickLitener(new OnItemClickLitener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if (!"fragment".equals(whereFrom)) {
                                        switch (addWitch) {
                                            case "menber":
                                                Intent intent1 = new Intent(UnallocatedDeptMenberActivity.this, AddMenberActivity.class);
                                                PublicDataUtil.addMenberData = list.get(position);
                                                startActivity(intent1);
                                                break;
                                            case "charge":
                                                Intent intent2 = new Intent(UnallocatedDeptMenberActivity.this, AddChargeActivity.class);
                                                PublicDataUtil.addMenberData = list.get(position);
                                                startActivity(intent2);
                                                break;
                                            case "responsible":
                                                Intent intent3 = new Intent(UnallocatedDeptMenberActivity.this, AddResponsibleActivity.class);
                                                PublicDataUtil.addMenberData = list.get(position);
                                                startActivity(intent3);
                                                break;
                                        }
                                        UnallocatedDeptMenberActivity.this.finish();
                                    } else {
                                        startActivity(new Intent(UnallocatedDeptMenberActivity.this, ContactsDetailsActivity.class).putExtra("user_no", list.get(position).getUserNo()));
                                    }
                                }

                                @Override
                                public void onItemLongClick(View view, int position) {

                                }
                            });
                        } else {
                            adapter = new UnAllocatedDeptRecyclerAdapter(UnallocatedDeptMenberActivity.this, list);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickLitener(new OnItemClickLitener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    if (!"fragment".equals(whereFrom)) {
                                        Intent intent = new Intent(UnallocatedDeptMenberActivity.this, AddMenberActivity.class);
                                        PublicDataUtil.addMenberData = list.get(position);
                                        startActivity(intent);
                                        UnallocatedDeptMenberActivity.this.finish();
                                    } else {
                                        startActivity(new Intent(UnallocatedDeptMenberActivity.this, ContactsDetailsActivity.class).putExtra("user_no", list.get(position).getUserNo()));
                                    }
                                }

                                @Override
                                public void onItemLongClick(View view, int position) {

                                }
                            });
                        }
                    } else {
                        Toastor.showToast(UnallocatedDeptMenberActivity.this, "未查询到数据");
                    }
                } else {
                    Toastor.showToast(UnallocatedDeptMenberActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_DEL_UN_ALLOCATED_MENBER) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    getUnAllocatedMenber(MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
                } else {
                    Toastor.showToast(UnallocatedDeptMenberActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 100) {
                final int position = (int) msg.obj;
                new AlertDialog(UnallocatedDeptMenberActivity.this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        httpHelper.postStringBack(HTTP_DEL_UN_ALLOCATED_MENBER, AppConfig.DEL_UN_ALLOCATED_MENBER, delMenber(position), handler, BaseModel.class);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setMsg("是否确认删除成员？").show();
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    private HashMap<String, String> delMenber(int position) {
        HashMap<String, String> del = new HashMap<String, String>();
        del.put("token", TelephoneUtil.getIMEI(this));
        del.put("unitId", MyApplication.getInstance().getUm().getData().getUserInfo().getUnitNo());
        del.put("userNo", list.get(position).getUserNo());
        return del;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_unallocated_dept_menber;
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
        tvMidTitle.setText("未分配部门成员");
        llBack.setOnClickListener(this);
        httpHelper = MyHttpHelper.getInstance(this);
        whereFrom = this.getIntent().getStringExtra("where_from");
        progress = new CustomProgress(this, "加载中...");
        progress.show();
        addWitch = this.getIntent().getStringExtra("add_witch");
        final String unitId = this.getIntent().getStringExtra("unitId");
        if (NetWorkUtil.isNetworkAvailable(this)) {
            if (!"".equals(unitId)) {
                getUnAllocatedMenber(unitId);
            }
        } else {
            Toastor.showToast(this, "当前网络不可用");
        }
        if (MyApplication.getInstance().getUm() != null && MyApplication.getInstance().getUm().getData().getUserInfo().getIsAdmin().equals("1")) {
            swipeRefreshLayout1.setVisibility(View.GONE);
            swipeRefreshLayout2.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout2.setVisibility(View.GONE);
            swipeRefreshLayout1.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout1.setColorSchemeResources(com.hyphenate.easeui.R.color.holo_blue_bright, com.hyphenate.easeui.R.color.holo_green_light,
                com.hyphenate.easeui.R.color.holo_orange_light, com.hyphenate.easeui.R.color.holo_red_light);
        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progress.show();
                getUnAllocatedMenber(unitId);
                swipeRefreshLayout1.setRefreshing(false);
            }
        });
        swipeRefreshLayout2.setColorSchemeResources(com.hyphenate.easeui.R.color.holo_blue_bright, com.hyphenate.easeui.R.color.holo_green_light,
                com.hyphenate.easeui.R.color.holo_orange_light, com.hyphenate.easeui.R.color.holo_red_light);
        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progress.show();
                getUnAllocatedMenber(unitId);
                swipeRefreshLayout2.setRefreshing(false);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 获取未分配部门成员
     */
    private void getUnAllocatedMenber(String unitId) {
        httpHelper.postStringBack(HTTP_GET_UN_ALLOCATED_MENBER, AppConfig.GET_UN_ALLOCATED_MENBER, getUnAllocated(unitId), handler, UnCallocateMenberModel.class);
    }

    private HashMap<String, String> getUnAllocated(String unitId) {
        HashMap<String, String> unAllocated = new HashMap<String, String>();
        unAllocated.put("token", TelephoneUtil.getIMEI(this));
        unAllocated.put("unitId", unitId);
        return unAllocated;
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
}
