package com.cn.clound.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.view.SwipeMenuRecyclerView;
import com.cn.clound.bean.approval.DeliveredApprovalsModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshBase;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshScrollView;

import java.util.HashMap;

import butterknife.Bind;

/**
 * 发出的审批界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-12 18:17:50
 */
public class DeliveredApprovalActivity extends BaseActivity implements View.OnClickListener, OnItemClickLitener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.recycler_delivered_approval)
    SwipeMenuRecyclerView recyclerView;
    @Bind(R.id.ptrScrollView_home)
    PullToRefreshScrollView mPtrScrollView;

    private DeliveredRecyclerAdapter adapter;
    private MyHttpHelper httpHelper;
    private CustomProgress progress;
    private int HTTP_MINE_DELIVERED_APPROVAL = 165;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_MINE_DELIVERED_APPROVAL) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    DeliveredApprovalsModel mam = (DeliveredApprovalsModel) msg.obj;
                    if (mam != null) {

                    }
                } else {
                    Toastor.showToast(DeliveredApprovalActivity.this, msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_delivered_approval;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        progress = new CustomProgress(this, "请稍候...");
        progress.show();
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText("我的审批");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeliveredRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        mPtrScrollView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        new GetDataTask().execute();
                    }
                });
        adapter.setOnItemClickLitener(this);
        httpHelper.postStringBack(HTTP_MINE_DELIVERED_APPROVAL, AppConfig.QUERY_MINE_DELIVERED_APPROVAL, apprival(), handler, DeliveredApprovalsModel.class);
    }

    /**
     * 查询我发出的审批列表
     *
     * @return
     */
    private HashMap<String, String> apprival() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        parames.put("pageSize", "10000");
        parames.put("pageNo", "1");
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
                this.finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(this, BuyerApprovalDetailsActivity.class));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    /**
     * 我的审批列表适配器
     */
    class DeliveredRecyclerAdapter extends RecyclerView.Adapter {
        private Context context;
        private OnItemClickLitener onItemClickLitener;

        public DeliveredRecyclerAdapter(Context context) {
            this.context = context;
        }

        public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
            this.onItemClickLitener = onItemClickLitener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_recycler_delivered_approval_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (onItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        onItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            public MyViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String[] result) {
            mPtrScrollView.onRefreshComplete();
        }
    }
}
