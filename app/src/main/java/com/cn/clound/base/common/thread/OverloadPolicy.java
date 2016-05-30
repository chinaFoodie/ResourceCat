package com.cn.clound.base.common.thread;

/**
 * Policy of thread-pool-executor overload.
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年3月29日11:50:37
 */
public enum OverloadPolicy {
    DiscardNewTaskInQueue,
    DiscardOldTaskInQueue,
    DiscardCurrentTask,
    CallerRuns,
    ThrowExecption
}