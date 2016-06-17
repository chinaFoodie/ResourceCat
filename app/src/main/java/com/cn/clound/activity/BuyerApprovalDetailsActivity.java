package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.approval.ApprovalDetailsModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.utils.DividerItemDecoration;
import com.cn.clound.view.CustomProgress;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

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
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.recycler_expense_details)
    RecyclerView recyclerViewExpense;
    @Bind(R.id.recycler_expense_picture)
    RecyclerView recyclerViewPicture;
    @Bind(R.id.recycler_approval_schedule)
    RecyclerView recyclerViewSchedule;
    @Bind(R.id.tv_rich_text_to_read)
    TextView tvRichTextRead;
    @Bind(R.id.civ_buyer_approval_header_avatar)
    CircleImageView civAvatar;

    private ExpenseApprovalRecyclerAdapter expenseAdapter;
    private ExpensePictureRecyclerAdapter pictureAdapter;
    private ExpenseApprovalScheduleRecyclerAdapter scheduleAdapter;
    private ApprovalDetailsModel approvalDetailsModel;
    private String comeFrom;
    private int HTTP_PASS_APPROVAL = 171;
    private int HTTP_REFUSE_APPROVAL = 172;
    private int HTTP_REVOKE_APPROVAL = 177;
    private MyHttpHelper httpHelper;
    private CustomProgress progress;
    private String approvalId;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_PASS_APPROVAL) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(BuyerApprovalDetailsActivity.this, "操作成功");
                    BuyerApprovalDetailsActivity.this.finish();
                } else {
                    Toastor.showToast(BuyerApprovalDetailsActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_REFUSE_APPROVAL) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(BuyerApprovalDetailsActivity.this, "操作成功");
                    BuyerApprovalDetailsActivity.this.finish();
                } else {
                    Toastor.showToast(BuyerApprovalDetailsActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_REVOKE_APPROVAL) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(BuyerApprovalDetailsActivity.this, "操作成功");
                    BuyerApprovalDetailsActivity.this.finish();
                } else {
                    Toastor.showToast(BuyerApprovalDetailsActivity.this, msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

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
        progress = new CustomProgress(this, "请稍候");
        httpHelper = MyHttpHelper.getInstance(this);
        approvalDetailsModel = (ApprovalDetailsModel) this.getIntent().getSerializableExtra("approval_details_model");
        Intent pre = this.getIntent();
        comeFrom = pre.getStringExtra("approval_come_from");
        approvalId = pre.getStringExtra("approval_id");
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvRichTextRead.setOnClickListener(this);

        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseApprovalRecyclerAdapter(this, approvalDetailsModel);
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
        scheduleAdapter = new ExpenseApprovalScheduleRecyclerAdapter(this, approvalDetailsModel.getData().getNodes());
        recyclerViewSchedule.setAdapter(scheduleAdapter);
        setValue();
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
            case R.id.tv_approval_bottom_left:
                if (tvBottomLeft.getText().toString().equals("同意")) {
                    progress.show();
                    httpHelper.postStringBack(HTTP_PASS_APPROVAL, AppConfig.PASS_APPROVAL, pass(), handler, BaseModel.class);
                } else {
                    switch (approvalDetailsModel.getData().getType()) {
                        case "1":
                            startActivity(new Intent(this, CommonApprovalActivity.class));
                            break;
                        case "2":
                            startActivity(new Intent(this, BuyerApprovalActivity.class));
                            break;
                        case "3":
                            startActivity(new Intent(this, WipedApprovalActivity.class));
                            break;
                    }
                }
                break;
            case R.id.tv_approval_bottom_mid:
                if (tvBottomMid.getText().toString().equals("拒绝")) {
                    progress.show();
                    httpHelper.postStringBack(HTTP_PASS_APPROVAL, AppConfig.REFUSE_APPROVAL, pass(), handler, BaseModel.class);
                }
                break;
            case R.id.tv_approval_bottom_right:
                if (tvBottomRight.getText().toString().equals("转审")) {
                    AppConfig.APPROVAL = approvalId;
                    startActivity(new Intent(this, DeptChooseActivity.class));
                } else {
                    progress.show();
                    httpHelper.postStringBack(HTTP_REVOKE_APPROVAL, AppConfig.REVOKE_APPROVAL, pass(), handler, BaseModel.class);
                }
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> pass() {
        HashMap<String, String> pass = new HashMap<String, String>();
        pass.put("token", TelephoneUtil.getIMEI(this));
        pass.put("id", approvalId);
        return pass;
    }

    @Bind(R.id.tv_approval_unit_name)
    TextView tvApprovalUnitName;
    @Bind(R.id.tv_approval_name)
    TextView tvApprovalName;
    @Bind(R.id.tv_approval_user_name)
    TextView tvApprovalUserName;
    @Bind(R.id.tv_approval_type)
    TextView tvApprovalType;
    @Bind(R.id.tv_approval_total_money)
    TextView tvApprovalTotalMoney;
    @Bind(R.id.ll_approval_total_money)
    LinearLayout llApprovalTotalMoney;
    @Bind(R.id.tv_approval_bottom_left)
    TextView tvBottomLeft;
    @Bind(R.id.tv_approval_bottom_mid)
    TextView tvBottomMid;
    @Bind(R.id.tv_approval_bottom_right)
    TextView tvBottomRight;
    @Bind(R.id.include_approval_bottom)
    View includeBottom;

    private void setValue() {
        //设置头像
        ImageLoader.getInstance().displayImage(approvalDetailsModel.getData().getUserHead(), civAvatar);
        tvApprovalUserName.setText(approvalDetailsModel.getData().getUserName());
        tvApprovalType.setText(approvalDetailsModel.getData().getTypeStr());
        tvApprovalUnitName.setText(approvalDetailsModel.getData().getUnitName());
        tvApprovalName.setText(approvalDetailsModel.getData().getUserName() + "的" +
                approvalDetailsModel.getData().getTypeStr());
        tvMidTitle.setText(approvalDetailsModel.getData().getTypeStr());
        if (approvalDetailsModel.getData().getExpExamine() != null) {
            tvApprovalTotalMoney.setText(approvalDetailsModel.getData().getExpExamine()
                    .getTotalMoney());
        } else if (approvalDetailsModel.getData().getPurExamine() != null) {
            tvApprovalTotalMoney.setText(approvalDetailsModel.getData().getPurExamine()
                    .getTotalMoney());
        } else {
            llApprovalTotalMoney.setVisibility(View.GONE);
        }
        tvBottomLeft.setOnClickListener(this);
        tvBottomMid.setOnClickListener(this);
        tvBottomRight.setOnClickListener(this);
        if (comeFrom.equals("await_me_approval")) {
            tvBottomLeft.setText("同意");
            tvBottomMid.setText("拒绝");
            tvBottomRight.setText("转审");
        } else if (comeFrom.equals("already_me_approval")) {
            includeBottom.setVisibility(View.GONE);
        } else {
            tvBottomLeft.setText("重新提交");
            tvBottomMid.setText("打印");
            tvBottomRight.setText("撤销");
        }
    }
}
