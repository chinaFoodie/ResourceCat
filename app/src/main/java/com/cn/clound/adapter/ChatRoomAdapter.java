package com.cn.clound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.chat.ChatRoomModel;

import java.util.List;

/**
 * 群聊适配器
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-28 10:27:41
 */
public class ChatRoomAdapter extends BaseAdapter {
    private Context context;
    private List<ChatRoomModel.ChatRoom> list;

    public ChatRoomAdapter(Context context, List<ChatRoomModel.ChatRoom> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.dt_chat_room_list_item, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_chat_room_name);
            holder.tvNameSort = (TextView) convertView.findViewById(R.id.tv_chat_room_avatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(list.get(position).getName());
        holder.tvNameSort.setText(list.get(position).getName().length() > 2 ? list.get(position).getName().substring(0, 2) : list.get(position).getName());
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvNameSort;
    }
}
