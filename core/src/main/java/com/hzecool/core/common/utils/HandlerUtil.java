package com.hzecool.core.common.utils;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil {

    public static Handler mHandler = new Handler(Looper.getMainLooper());

    // 在主线程执行
    public static void post(Runnable task) {

        mHandler.post(task);
    }

    // 在主线程执行
    public static void postDelay(Runnable task, long time) {

        //容易报空指针异常
        try {
            mHandler.postDelayed(task, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //清除任务
    public static void clearTask(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }


}
