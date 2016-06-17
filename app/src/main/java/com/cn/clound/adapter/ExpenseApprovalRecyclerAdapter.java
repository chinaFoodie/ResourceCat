package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.approval.ApprovalDetailsModel;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-13 14:15:11
 */
public class ExpenseApprovalRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private ApprovalDetailsModel approvalDetailsModel;

    public ExpenseApprovalRecyclerAdapter(Context context, ApprovalDetailsModel approvalDetailsModel) {
        this.context = context;
        this.approvalDetailsModel = approvalDetailsModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_expense_details_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (approvalDetailsModel.getData().getPurExamine() != null) {
            ((MyViewHolder) holder).tvApprovalDetails.setText(approvalDetailsModel.
                    getData().getPurExamine().getDetails().get(position).getpName() +
                    approvalDetailsModel.getData().getPurExamine().getDetails().get(position).getpNum() +
                    approvalDetailsModel.getData().getPurExamine().getDetails().get(position).getpUnit());
            ((MyViewHolder) holder).tvApprovalIndex.setText((position + 1) + "");
            ((MyViewHolder) holder).tvApprovalMoney.setText(approvalDetailsModel.
                    getData().getPurExamine().getDetails().get(position).getpPrice());
            ((MyViewHolder) holder).tvApprovalType.setText(approvalDetailsModel.getData().
                    getPurExamine().getDetails().get(position).getpSepc());
        } else if (approvalDetailsModel.getData().getExpExamine() != null) {
            ((MyViewHolder) holder).tvApprovalDetails.setText(approvalDetailsModel.
                    getData().getExpExamine().getDetails().get(position).getName() +
                    approvalDetailsModel.getData().getExpExamine().getDetails().get(position).getCharges_detail());
            ((MyViewHolder) holder).tvApprovalIndex.setText((position + 1) + "");
            ((MyViewHolder) holder).tvApprovalMoney.setText(approvalDetailsModel.
                    getData().getExpExamine().getDetails().get(position).getMoney());
            ((MyViewHolder) holder).tvApprovalType.setText(approvalDetailsModel.getData().
                    getExpExamine().getDetails().get(position).getType());
        }
    }

    @Override
    public int getItemCount() {
        if (approvalDetailsModel.getData().getPurExamine() != null) {
            return approvalDetailsModel.getData().getPurExamine().getDetails().size();
        } else if (approvalDetailsModel.getData().getExpExamine() != null) {
            return approvalDetailsModel.getData().getExpExamine().getDetails().size();
        } else {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvApprovalIndex;
        private TextView tvApprovalMoney;
        private TextView tvApprovalType;
        private TextView tvApprovalDetails;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvApprovalDetails = (TextView) itemView.findViewById(R.id.tv_approval_expense_details);
            tvApprovalType = (TextView) itemView.findViewById(R.id.tv_approval_expense_type);
            tvApprovalMoney = (TextView) itemView.findViewById(R.id.tv_approval_expense_money);
            tvApprovalIndex = (TextView) itemView.findViewById(R.id.tv_approval_expense_index);
        }
    }
}
