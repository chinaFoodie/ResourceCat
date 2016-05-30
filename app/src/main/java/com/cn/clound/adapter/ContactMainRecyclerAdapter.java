package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.view.SwipeMenuLayout;
import com.cn.clound.bean.User.DTUser;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-6 12:47:20
 */
public class ContactMainRecyclerAdapter extends RecyclerView.Adapter {
    private OnItemClickLitener onItemClickLitener;
    private Handler handler;

    public OnItemClickLitener getOnItemClickLitener() {
        return onItemClickLitener;
    }

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    private Context context;
    private List<DTUser> listUser;
    int[] imgResource = new int[]{R.mipmap.img_contact_header_join, R.mipmap.img_contact_header_up_down, R.mipmap.img_contact_header_org, R.mipmap.img_contact_header_group, R.mipmap.img_contact_header_unallocted};
    /**
     * 状态模式初始化ImageLoader
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.img_empty_defalt) // resource or drawable
            .showImageForEmptyUri(R.mipmap.img_empty_defalt) // resource or drawable
            .showImageOnFail(R.mipmap.img_empty_defalt) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public ContactMainRecyclerAdapter(Context context, List<DTUser> listUser, Handler handler) {
        this.context = context;
        this.listUser = listUser;
        this.handler = handler;
    }

    public void updateListView(List<DTUser> list) {
        this.listUser = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeadViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_main_contacts_header_menu, parent,
                    false));
        } else {
            return new ContentViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_recycler_main_contacts, parent,
                    false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            ((HeadViewHolder) holder).tvName.setText(listUser.get(position).getCm().getName());
            ((HeadViewHolder) holder).imgAvatar.setImageResource(imgResource[position]);
        } else {
            int section = getSectionForPosition(position);
            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                ((ContentViewHolder) holder).tvLetter.setVisibility(View.VISIBLE);
                ((ContentViewHolder) holder).tvSwipeTitle.setVisibility(View.VISIBLE);
                String temp = listUser.get(position).getSortLetters().toUpperCase();
                ((ContentViewHolder) holder).tvLetter.setText(temp.substring(0, 1));
            } else {
                ((ContentViewHolder) holder).tvSwipeTitle.setVisibility(View.GONE);
                ((ContentViewHolder) holder).tvLetter.setVisibility(View.GONE);
            }
            if ("".equals(listUser.get(position).getCm().getDepName())) {
                ((ContentViewHolder) holder).tvName.setText(listUser.get(position).getCm().getName());
            } else {
                ((ContentViewHolder) holder).tvName.setText(listUser.get(position).getCm().getName() + " (" + listUser.get(position).getCm().getDepName() + ")");
            }
            ((ContentViewHolder) holder).tvLevel.setText(listUser.get(position).getCm().getUnitName());
            ImageLoader.getInstance().displayImage(listUser.get(position).getCm().getHead(), ((ContentViewHolder) holder).imgAvatar, options);
            ((ContentViewHolder) holder).tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SwipeMenuLayout) holder.itemView).smoothCloseMenu();
                    Message msg = new Message();
                    msg.what = 1000;
                    msg.obj = position;
                    handler.sendMessage(msg);
                }
            });
        }
        //设置Item事件监听
        if (onItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listUser != null ? listUser.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position < 5 ? 0 : 1;
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CircleImageView imgAvatar;

        public HeadViewHolder(View view) {
            super(view);
            imgAvatar = (CircleImageView) view.findViewById(R.id.img_contact_header);
            tvName = (TextView) view.findViewById(R.id.tv_contact_header);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvLevel;
        TextView tvLetter;
        CircleImageView imgAvatar;
        TextView tvDelete;
        TextView tvSwipeTitle;

        public ContentViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.contact_name);
            tvLevel = (TextView) view.findViewById(R.id.contact_phone);
            imgAvatar = (CircleImageView) view.findViewById(R.id.contact_circleimage);
            tvLetter = (TextView) view.findViewById(R.id.catalog);
            tvDelete = (TextView) view.findViewById(R.id.btDelete);
            tvSwipeTitle = (TextView) view.findViewById(R.id.swipe_catalog);
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return listUser.get(position).getSortLetters().toUpperCase().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = listUser.get(i).getSortLetters();
            if (sortStr != null & !"".equals(sortStr)) {
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
        }
        return -1;
    }
}
