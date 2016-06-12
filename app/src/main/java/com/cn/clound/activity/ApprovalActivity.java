package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 审批界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月15日 17:54:50
 */
public class ApprovalActivity extends BaseActivity implements View.OnClickListener, OnItemClickLitener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.recycler_approval_menu)
    RecyclerView recyclerView;

    private List<String> mDatas;
    private HomeAdapter mAdapter;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_approval;
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
        tvMidTitle.setText(getResources().getString(R.string.activity_approval));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mDatas = new ArrayList<String>();
        mDatas.add("采购审批");
        mDatas.add("报销");
        mDatas.add("通用审批");
        DividerItemDecoration dividerVERTICAL = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        dividerVERTICAL.setSize(1);
        dividerVERTICAL.setColor(0xFFDDDDDD);
        DividerItemDecoration dividerHORIZONTAL = new DividerItemDecoration(DividerItemDecoration.HORIZONTAL);
        dividerHORIZONTAL.setSize(1);
        dividerHORIZONTAL.setColor(0xFFDDDDDD);
        recyclerView.addItemDecoration(dividerVERTICAL);
        recyclerView.addItemDecoration(dividerHORIZONTAL);
        mAdapter = new HomeAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(this);
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

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, BuyerApprovalActivity.class));
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        private OnItemClickLitener mOnItemClickLitener;
        private int[] images = new int[]{R.mipmap.img_aooroval_buyer, R.mipmap.img_aooroval_paid, R.mipmap.img_aooroval_common};

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ApprovalActivity.this).inflate(R.layout.dt_item_recycler, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.tvMenuName.setText(mDatas.get(position));
            holder.imgMenu.setBackgroundResource(images[position]);
            //设置Item事件监听
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvMenuName;
            ImageView imgMenu;

            public MyViewHolder(View view) {
                super(view);
                tvMenuName = (TextView) view.findViewById(R.id.tv_index_menu);
                imgMenu = (ImageView) view.findViewById(R.id.img_index_menu);
            }
        }
    }
}
