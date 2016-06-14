package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.ExpenseApprovalRecyclerAdapter;
import com.cn.clound.adapter.ExpenseApprovalScheduleRecyclerAdapter;
import com.cn.clound.adapter.ExpensePictureRecyclerAdapter;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.utils.DividerItemDecoration;

import butterknife.Bind;

/**
 * 采购审批详情
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-13 10:19:53
 */
public class BuyerApprovalDetailsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.recycler_expense_details)
    RecyclerView recyclerViewExpense;
    @Bind(R.id.recycler_expense_picture)
    RecyclerView recyclerViewPicture;
    @Bind(R.id.recycler_approval_schedule)
    RecyclerView recyclerViewSchedule;
    @Bind(R.id.tv_rich_text_to_read)
    TextView tvRichTextRead;

    private ExpenseApprovalRecyclerAdapter expenseAdapter;
    private ExpensePictureRecyclerAdapter pictureAdapter;
    private ExpenseApprovalScheduleRecyclerAdapter scheduleAdapter;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_buyer_approval_details;
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
        llBack.setOnClickListener(this);
        tvRichTextRead.setOnClickListener(this);

        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseApprovalRecyclerAdapter(this);
        recyclerViewExpense.setAdapter(expenseAdapter);

        DividerItemDecoration dividerVERTICAL = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        dividerVERTICAL.setSize(2);
        dividerVERTICAL.setColor(0xFFDDDDDD);
        DividerItemDecoration dividerHORIZONTAL = new DividerItemDecoration(DividerItemDecoration.HORIZONTAL);
        dividerHORIZONTAL.setSize(2);
        dividerHORIZONTAL.setColor(0xFFDDDDDD);
        recyclerViewPicture.addItemDecoration(dividerVERTICAL);
        recyclerViewPicture.addItemDecoration(dividerHORIZONTAL);
        recyclerViewPicture.setLayoutManager(new GridLayoutManager(this, 2));
        pictureAdapter = new ExpensePictureRecyclerAdapter(this);
        recyclerViewPicture.setAdapter(pictureAdapter);

        recyclerViewSchedule.setLayoutManager(new LinearLayoutManager(this));
        scheduleAdapter = new ExpenseApprovalScheduleRecyclerAdapter(this);
        recyclerViewSchedule.setAdapter(scheduleAdapter);
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
                this.finish();
                break;
            case R.id.tv_rich_text_to_read:
//                Intent richIntent = new Intent(this, PDFReadActivity.class);
//                startActivity(richIntent);
                break;
            default:
                break;
        }
    }
}
