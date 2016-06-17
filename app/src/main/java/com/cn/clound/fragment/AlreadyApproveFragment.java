package com.cn.clound.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.activity.BuyerApprovalDetailsActivity;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.view.SwipeMenuRecyclerView;
import com.cn.clound.bean.approval.ApprovalDetailsModel;
import com.cn.clound.bean.approval.ApprovalModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.interfaces.OnItemClickListener;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshBase;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshScrollView;
import com.hyphenate.easeui.widget.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 我已审批
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-17 09:12:28
 */
public class AlreadyApproveFragment extends BaseFragment implements OnItemClickListener {
    @Bind(R.id.ptrScrollView_home)
    PullToRefreshScrollView mPtrScrollView;
    @Bind(R.id.recycler_await_mine_approval)
    SwipeMenuRecyclerView recyclerViewAwait;
    private int HTTP_QUERY_ALREADY_APPROVAL = 170;
    private int HTTP_GET_APPROVAL_DETAILS = 174;
    private MyHttpHelper httpHelper;
    private AwaitApprovalAdapter awaitAdapter;
    private List<ApprovalModel.ApprovalInfo> listAi = new ArrayList<>();
    private int index = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_QUERY_ALREADY_APPROVAL) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    ApprovalModel am = (ApprovalModel) msg.obj;
                    if (am != null) {
                        listAi = am.getData().getResult();
                        awaitAdapter = new AwaitApprovalAdapter(getActivity(), listAi);
                        recyclerViewAwait.setAdapter(awaitAdapter);
                        awaitAdapter.setOnItemClickListener(AlreadyApproveFragment.this);
                    }
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_GET_APPROVAL_DETAILS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    ApprovalDetailsModel adm = (ApprovalDetailsModel) msg.obj;
                    if (adm != null) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), BuyerApprovalDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("approval_details_model", adm);
                        intent.putExtra("approval_come_from", "already_me_approval");
                        intent.putExtra("approval_id", listAi.get(index).getId());
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                    }
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_fragment_await;
    }

    @Override
    public void onFragmentAttach(Fragment fragment, Activity activity) {

    }

    @Override
    public void onFragmentCreated(Fragment fragment, Bundle savedInstanceState) {
    }

    @Override
    public void onFragmentCreateView(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
        init();
    }

    private void init() {
        httpHelper = MyHttpHelper.getInstance(getActivity());
        mPtrScrollView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        new GetDataTask().execute();
                    }
                });
        recyclerViewAwait.setLayoutManager(new LinearLayoutManager(getActivity()));
        httpHelper.postStringBack(HTTP_QUERY_ALREADY_APPROVAL, AppConfig.QUERY_AWAIT_APPROVAL, already(), handler, ApprovalModel.class);
    }

    private HashMap<String, String> already() {
        HashMap<String, String> already = new HashMap<String, String>();
        already.put("token", TelephoneUtil.getIMEI(getActivity()));
        already.put("flag", "1");
        already.put("pageNo", "1");
        already.put("pageSize", "1000");
        return already;
    }

    @Override
    public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentStarted(Fragment fragment) {

    }

    @Override
    public void onFragmentResumed(Fragment fragment) {

    }

    @Override
    public void onFragmentPaused(Fragment fragment) {

    }

    @Override
    public void onFragmentStopped(Fragment fragment) {

    }

    @Override
    public void onFragmentDestroyed(Fragment fragment) {

    }

    @Override
    public void onFragmentDetach(Fragment fragment) {

    }

    @Override
    public void onFragmentSaveInstanceState(Fragment fragment, Bundle outState) {

    }

    @Override
    public void onItemClick(View view, int position) {
        index = position;
        httpHelper.postStringBack(HTTP_GET_APPROVAL_DETAILS, AppConfig.GET_APPROVAL_DETAILS, details(listAi.get(position).getId()), handler, ApprovalDetailsModel.class);
    }

    private HashMap<String, String> details(String id) {
        HashMap<String, String> details = new HashMap<>();
        details.put("token", TelephoneUtil.getIMEI(getActivity()));
        details.put("id", id);
        details.put("pageNo", "1");
        details.put("pageSize", "1000");
        return details;
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    //刷新操作
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

    class AwaitApprovalAdapter extends RecyclerView.Adapter {
        private Context context;
        private List<ApprovalModel.ApprovalInfo> list;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public AwaitApprovalAdapter(Context context, List<ApprovalModel.ApprovalInfo> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AwaitViewHolder holder = new AwaitViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_recycler_delivered_approval_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((AwaitViewHolder) holder).tvApprovalTitle.setText(list.get(position).getName());
            ((AwaitViewHolder) holder).tvApprovalDept.setText(list.get(position).getUnitName());
            ((AwaitViewHolder) holder).tvApprovalTime.setText(list.get(position).getBeginAt());
            ((AwaitViewHolder) holder).tvApprovalState.setVisibility(View.GONE);
            ((AwaitViewHolder) holder).tvApprovalState.setTextColor(context.getResources().getColor(R.color.color_approval_tbd));
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list != null ? list.size() : 0;
        }

        class AwaitViewHolder extends RecyclerView.ViewHolder {
            CircleImageView imgHead;
            TextView tvApprovalTitle;
            TextView tvApprovalDept;
            TextView tvApprovalTime;
            TextView tvApprovalState;

            public AwaitViewHolder(View itemView) {
                super(itemView);
                imgHead = (CircleImageView) itemView.findViewById(R.id.civ_delivered_approval_avatar);
                tvApprovalTitle = (TextView) itemView.findViewById(R.id.tv_approval_title);
                tvApprovalDept = (TextView) itemView.findViewById(R.id.tv_approval_dept);
                tvApprovalTime = (TextView) itemView.findViewById(R.id.tv_approval_time);
                tvApprovalState = (TextView) itemView.findViewById(R.id.tv_approval_state);
            }
        }
    }
}
