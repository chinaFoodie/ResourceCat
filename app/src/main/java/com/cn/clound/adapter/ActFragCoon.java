package com.cn.clound.adapter;

import com.cn.clound.base.BaseFragment;
import com.cn.clound.bean.User.BottomUserModel;

import java.util.List;

/**
 * Activity和Fragment之间通讯接口
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-14 18:39:54
 */
public interface ActFragCoon {

    void actToFrag(List<BottomUserModel> listBottom);
}
