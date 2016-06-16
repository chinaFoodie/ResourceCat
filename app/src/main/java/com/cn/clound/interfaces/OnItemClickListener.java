package com.cn.clound.interfaces;

import android.view.View;

/**
 * 事件点击接口
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-14 18:39:54
 */
public interface OnItemClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
}
