package com.cn.clound.base.common.thread;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年3月29日11:50:48
 */
public abstract class PriorityRunnable implements Runnable {

    int priority;

    protected PriorityRunnable(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}