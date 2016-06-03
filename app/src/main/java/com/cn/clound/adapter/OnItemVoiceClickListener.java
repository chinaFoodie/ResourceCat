package com.cn.clound.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 语音消息播放接口
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-2 17:43:27
 */
public interface OnItemVoiceClickListener {
    void onItemClick(View holderItem, ImageView v, ImageView iv_read_status, int position);
}
