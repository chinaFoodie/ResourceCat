package com.cn.clound.base;

import com.cn.clound.base.common.assist.Toastor;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/25.
 */
public class BaseUiListener implements IUiListener {

    @Override
    public void onComplete(Object response) {
        doComplete(response);
    }

    protected void doComplete(Object values) {

    }

    @Override
    public void onError(UiError e) {
    }

    @Override
    public void onCancel() {
    }
}
