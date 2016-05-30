package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.adapter.SignTimeCycleAdapter;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.utils.SharePreferceUtil;
import com.cn.clound.bean.singed.TimeCycleModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * 时间周期界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-17 14:18:21
 */
public class TimeCycleActivity extends BaseActivity implements View.OnClickListener, OnItemClickLitener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_left)
    LinearLayout llleft;
    @Bind(R.id.tv_base_left)
    TextView tvBaseLeft;
    @Bind(R.id.recycler_time_cycle)
    RecyclerView recyclerview;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    private String[] weeks = new String[]{"每周一", "每周二", "每周三", "每周四", "每周五", "每周六", "每周日"};
    private List<TimeCycleModel> list = new ArrayList<TimeCycleModel>();
    private SignTimeCycleAdapter adapter;
    private SharePreferceUtil share;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_time_cycle;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        share = SharePreferceUtil.getInstance(this);
        tvMidTitle.setText("时间周期");
        llleft.setVisibility(View.VISIBLE);
        llleft.setOnClickListener(this);
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("确定");
        tvBaseRight.setOnClickListener(this);
        tvBaseLeft.setText("取消");
        String temp = share.getString("set_sign_tip_cycle");
        for (int i = 0; i < weeks.length; i++) {
            TimeCycleModel tcm = new TimeCycleModel();
            tcm.setWeekName(weeks[i]);
            if (!temp.equals("")) {
                if (temp.substring(temp.length() - 1).equals(",")) {
                    String[] week = temp.substring(0, temp.length() - 1).split(",");
                    if (Arrays.asList(week).contains(i + 1 + "")) {
                        tcm.setChoosed(true);
                    } else {
                        tcm.setChoosed(false);
                    }
                } else {
                    String[] week = temp.split(",");
                    if (Arrays.asList(week).contains(i + 1 + "")) {
                        tcm.setChoosed(true);
                    } else {
                        tcm.setChoosed(false);
                    }
                }
            } else {
                tcm.setChoosed(false);
            }
            list.add(tcm);
        }
        adapter = new SignTimeCycleAdapter(this, list);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickLitener(this);
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
            case R.id.ll_base_left:
                this.finish();
                break;
            case R.id.tv_base_right:
                String temp = "";
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isChoosed()) {
                        temp += i + 1 + ",";
                    }
                }
                share.setCache("set_sign_tip_cycle", temp);
                this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (list.get(position).isChoosed()) {
            list.get(position).setChoosed(false);
        } else {
            list.get(position).setChoosed(true);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
