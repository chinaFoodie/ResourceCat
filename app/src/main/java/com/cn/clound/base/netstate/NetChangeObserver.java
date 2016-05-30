package com.cn.clound.base.netstate;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @Clase NetChangeObserver
 * @date 2016年3月29日11:33:54
 * @Description 是检测网络改变的观察者
 */
public class NetChangeObserver {
    /**
     * 网络连接连接时调用
     */
    public void onConnect(NetWorkUtil.netType type) {
    }

    /**
     * 当前没有网络连接
     */
    public void onDisConnect() {
    }
}
