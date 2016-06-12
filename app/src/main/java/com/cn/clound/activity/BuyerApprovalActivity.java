package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;

import butterknife.Bind;

/**
 * 采购审批界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-8 15:29:56
 */
public class BuyerApprovalActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_left)
    LinearLayout llLeft;
    @Bind(R.id.tv_base_left)
    TextView tvBaseLeft;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.include_buyer_approval_first)
    View buyerFirst;
    @Bind(R.id.include_buyer_approval_next)
    View buyerNext;
    @Bind(R.id.ll_buyer_approval_card)
    LinearLayout llByCard;
    @Bind(R.id.ll_buyer_approval_cash)
    LinearLayout llByCash;
    @Bind(R.id.img_buyer_approval_cash)
    ImageView imgCash;
    @Bind(R.id.img_buyer_approval_card)
    ImageView imgCard;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_buyer_approval;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llLeft.setVisibility(View.VISIBLE);
        llLeft.setOnClickListener(this);
        tvMidTitle.setText("采购审批");

        tvBaseLeft.setVisibility(View.VISIBLE);
        tvBaseLeft.setText("取消");

        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("下一步");
        tvBaseRight.setOnClickListener(this);

        llByCard.setOnClickListener(this);
        llByCash.setOnClickListener(this);
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
                if ("下一步".equals(tvBaseRight.getText().toString())) {
                    this.finish();
                } else {
                    buyerNext.setVisibility(View.GONE);
                    buyerFirst.setVisibility(View.VISIBLE);
                    tvBaseRight.setText("下一步");
                }
                break;
            case R.id.tv_base_right:
                if ("下一步".equals(tvBaseRight.getText().toString())) {
                    buyerFirst.setVisibility(View.GONE);
                    buyerNext.setVisibility(View.VISIBLE);
                    tvBaseRight.setText("提交");
                } else {
                    Toastor.showToast(this, "提交审批");
                }
                break;
            case R.id.ll_buyer_approval_cash:
                imgCard.setImageResource(R.mipmap.img_choosed);
                imgCash.setImageResource(R.mipmap.img_no_choose);
                break;
            case R.id.ll_buyer_approval_card:
                imgCash.setImageResource(R.mipmap.img_choosed);
                imgCard.setImageResource(R.mipmap.img_no_choose);
                break;
            default:
                break;
        }
    }
    /**
     * 重写返回键
     */
}
