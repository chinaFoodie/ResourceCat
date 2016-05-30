package com.cn.clound.view.wheelpicker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cn.clound.R;
import com.cn.clound.view.wheelpicker.adapters.AbstractWheelTextAdapter;

import java.util.List;

/**
 * 时间选择器适配器
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-14 14:51:09
 */
public class WheelDateAdapter extends AbstractWheelTextAdapter {
    private List<String> list;

    public WheelDateAdapter(Context context, List<String> list, int currentItem, int maxsize,
                            int minsize) {
        super(context, R.layout.dt_item_time_layout, NO_RESOURCE, currentItem, maxsize, minsize);
        this.list = list;
        setItemTextResource(R.id.tempValue);
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        return super.getItem(index % list.size(), convertView, parent);
    }

    @Override
    public CharSequence getItemText(int index) {
        return list.get(index % list.size()) + "";
    }

    @Override
    public int getItemsCount() {
        if (list != null && list.size() < 3) {
            return list.size();
        } else {
            return Integer.MAX_VALUE;
        }
    }
}
