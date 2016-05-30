package com.cn.clound.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.activity.ApprovalActivity;
import com.cn.clound.activity.CloudMeetingActivity;
import com.cn.clound.activity.DataCenterActivity;
import com.cn.clound.activity.MainActivity;
import com.cn.clound.activity.SignedActivity;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.utils.DividerItemDecoration;
import com.cn.clound.view.BaseViewPager;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @version 1.00
 * @date 2016年3月30日13:15:52
 */
public class IndexMainFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.bvp_index_banner)
    BaseViewPager viewPager;
    @Bind(R.id.index_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.civ_base_avatar)
    CircleImageView circleImageViewAvatar;
    @Bind(R.id.ll_base_left_info)
    LinearLayout llUserAvatar;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    private List<String> mDatas;
    private HomeAdapter mAdapter;

    private ArrayList<String> imageUrls = new ArrayList<String>();
    /**
     * 状态模式初始化ImageLoader
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_launcher) // resource or drawable
            .showImageForEmptyUri(R.mipmap.ic_launcher) // resource or drawable
            .showImageOnFail(R.mipmap.ic_launcher) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_index_main_fragment;
    }

    @Override
    public void onFragmentAttach(Fragment fragment, Activity activity) {
    }

    @Override
    public void onFragmentCreated(Fragment fragment, Bundle savedInstanceState) {
    }

    private void init() {
        tvMidTitle.setText(getResources().getString(R.string.mid_title_work));
        llUserAvatar.setVisibility(View.VISIBLE);
        imageUrls.add("http://pic.58pic.com/58pic/14/75/16/73z58PICfJR_1024.jpg");
        imageUrls.add("http://pic2.ooopic.com/12/52/76/42bOOOPIC81_1024.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/16/73/95/63E58PICQh7_1024.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/16/93/40/65E58PICnVz_1024.jpg");
        circleImageViewAvatar.setOnClickListener(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void onFragmentCreateView(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    }

    @Override
    public void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
        init();
        viewPager.setImageResources(imageUrls, new MyPagerBannersListener());
        viewPager.startImageCycle();
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        initData();
    }

    private void initData() {
        mDatas = new ArrayList<String>();
        mDatas.add("数据中心");
        mDatas.add("云会议");
        mDatas.add("审批");
        mDatas.add("签到");
        DividerItemDecoration dividerVERTICAL = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        dividerVERTICAL.setSize(1);
        dividerVERTICAL.setColor(0xFFDDDDDD);
        DividerItemDecoration dividerHORIZONTAL = new DividerItemDecoration(DividerItemDecoration.HORIZONTAL);
        dividerHORIZONTAL.setSize(1);
        dividerHORIZONTAL.setColor(0xFFDDDDDD);
        mRecyclerView.addItemDecoration(dividerVERTICAL);
        mRecyclerView.addItemDecoration(dividerHORIZONTAL);
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
        mAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), DataCenterActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), CloudMeetingActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), ApprovalActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), SignedActivity.class));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toastor.showToast(getActivity(), "长按position=" + position);
            }
        });
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_base_avatar:
                MainActivity.leftMenu.toggle();
                break;
            default:
                break;
        }
    }

    public class MyPagerBannersListener implements BaseViewPager.PagerBannersListener {

        @Override
        public void ShowImage(String imageURL, final ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView, options);
        }

        @Override
        public void onImageClick(int position, View imageView) {

        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        private OnItemClickLitener mOnItemClickLitener;
        private int[] images = new int[]{R.mipmap.dt_main_work_data_center, R.mipmap.dt_main_work_meeting, R.mipmap.dt_main_work_examine, R.mipmap.dt_main_work_location};

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.dt_item_recycler, parent,
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
