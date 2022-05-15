package com.tuanche.directselling.util;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author ZhangYiXin
 * @version 1.0
 * @date 2020/9/29 11:54
 **/
public class StatisticsTaskThread implements Runnable {

    private CyclicBarrier barrier;
    private StatisticsTask statisticsTask;

    public StatisticsTaskThread(CyclicBarrier barrier, StatisticsTask statisticsTask) {
        this.barrier = barrier;
        this.statisticsTask = statisticsTask;
    }

    @Override
    public void run() {
        try {
            statisticsTask.runTask();
            barrier.await();
        } catch (Exception e) {
            CommonLogUtil.addError("B/C数据概览", "数据提取线程错误", e);
        }
    }
}
