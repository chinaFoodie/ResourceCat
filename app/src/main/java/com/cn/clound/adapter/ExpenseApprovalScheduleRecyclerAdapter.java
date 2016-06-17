package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.application.MyApplication;
import com.cn.clound.bean.approval.ApprovalDetailsModel;
import com.hyphenate.easeui.widget.CircleImageView;

import java.util.List;

/**
 * 报销审批流程适配器
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-13 14:15:11
 */
public class ExpenseApprovalScheduleRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ApprovalDetailsModel.Nodes> nodes;

    public ExpenseApprovalScheduleRecyclerAdapter(Context context, List<ApprovalDetailsModel.Nodes> nodes) {
        this.context = context;
        this.nodes = nodes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_expense_schedule_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).tvApprovalDept.setText("教育部");
        ((MyViewHolder) holder).tvApprovalTime.setText(nodes.get(position).getProcessingTime());
        ((MyViewHolder) holder).tvApprovalName.setText(nodes.get(position).getUserName());
        if (nodes.get(position).getStateStr().equals("待处理")) {
            ((MyViewHolder) holder).llAwait.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).llAlready.setVisibility(View.GONE);
        } else {
            ((MyViewHolder) holder).llAwait.setVisibility(View.GONE);
            ((MyViewHolder) holder).llAlready.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return nodes != null ? nodes.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvApprovalDept;
        private TextView tvApprovalTime;
        private CircleImageView civApprovalAvatar;
        private LinearLayout llAwait;
        private LinearLayout llAlready;
        private TextView tvApprovalName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvApprovalDept = (TextView) itemView.findViewById(R.id.tv_approval_dept_name);
            tvApprovalTime = (TextView) itemView.findViewById(R.id.tv_approval_time);
            civApprovalAvatar = (CircleImageView) itemView.findViewById(R.id.civ_approval_schedule_avatar);
            llAwait = (LinearLayout) itemView.findViewById(R.id.ll_await_approval);
            llAlready = (LinearLayout) itemView.findViewById(R.id.ll_already_approval);
            tvApprovalName = (TextView) itemView.findViewById(R.id.tv_approval_person_name);
        }
    }
}
