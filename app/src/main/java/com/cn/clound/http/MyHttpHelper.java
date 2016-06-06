package com.cn.clound.http;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.common.utils.AndroidUtil;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.SharePreferceUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

import okhttp3.Call;

/**
 * HTTP请求封装类
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年3月29日17:03:09
 */
public class MyHttpHelper<T> {
    public static MyHttpHelper instance = null;
    public Context context;

    private SharePreferceUtil share;

    public static MyHttpHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MyHttpHelper(context);
        }
        return instance;
    }

    public MyHttpHelper(Context context) {
        // When I wrote this, only God and I understood what I was doing. Now, God only knows
        this.context = context;
        OkHttpUtils.head().headers(setHeaders());
    }

    private HashMap<String, String> setHeaders() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Device_Id", AndroidUtil.getMacAddress(context));
        headers.put("APP-Key", AppConfig.APP_KEY);
        return headers;
    }

    /**
     * Request By PostString
     */
    public synchronized void postStringBack(final int flag, final String url, HashMap<String, String> parames, final Handler handler, final Class<T> obj) {
        OkHttpUtils.post().url(url).headers(setHeaders()).params(parames).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                String erroMsg = e.toString();
            }

            @Override
            public void onResponse(String response) {
                try {
                    SharePreferceUtil share = SharePreferceUtil.getInstance(context);
                    JSONObject json = new JSONObject(response);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    if (AppConfig.SUCCESS.equals(status)) {
                        if (url.equals(AppConfig.QUERY_MY_SIGN_TIP)) {
                            share.setCache("my_sign_tip_list", response);
                        }
                        T t = (T) GsonTools.getPerson(response, obj);
                        Message msg = new Message();
                        msg.what = Integer.parseInt(status);
                        msg.obj = t;
                        msg.arg1 = flag;
                        handler.sendMessage(msg);
                    } else {
                        Message erroMsg = new Message();
                        erroMsg.what = Integer.parseInt(status);
                        erroMsg.obj = message;
                        erroMsg.arg1 = flag;
                        handler.sendMessage(erroMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Request By PostString
     */
    public void post(String url, StringCallback stringCallback) {
        OkHttpUtils.post().url(url).headers(setHeaders()).addFile("", "", null).build().execute(stringCallback);
    }
}
