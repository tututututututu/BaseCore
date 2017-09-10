package com.hzecool.core.ActivityManager;

import com.hzecool.core.base.TAbsActivity;

import java.util.ArrayList;
import java.util.Stack;

/**
 * auther tuhui on 2017/3/21
 * activity堆栈管理，先入后出，创建activity时压入，销毁activity时移出
 *
 */
public class ActivityStack {

    private static Stack<TAbsActivity> activites = new Stack<TAbsActivity>();
    private static int stubIdx = Integer.MAX_VALUE;

    private ActivityStack() {

    }

    /**
     * 关闭activity，并从堆栈中移除
     */
    public static void finish(TAbsActivity activity) {
        if (activity != null) {
            activity.finish();
            // 从堆栈中移除
            activites.remove(activity);
            activity = null;
        }
    }

    /**
     * 获取的栈顶的activity
     */
    public static TAbsActivity getTopActivity() {
        if (!activites.isEmpty()) {
            return activites.pop();
        }
        return null;
    }

    /**
     * 获取的栈底的activity
     */
    public static TAbsActivity getLastActivity() {
        if (!activites.isEmpty()) {
            return activites.lastElement();
        }
        return null;
    }

    /**
     * 移除栈顶的activity
     */
    public synchronized static void pop(TAbsActivity activity) {
        if (activity != null && activites.size() > 0) {
            activites.remove(activity);
        }
    }

    /**
     * 返回栈顶的第几个activity(不移除)
     */
    public static TAbsActivity getLastActivityAt(int index) {
        if (index > 0 && index < activites.size()) {
            return activites.elementAt(activites.size() - index);
        }
        return null;
    }

    /**
     * 返回栈顶activity(不移除)
     */
    public static TAbsActivity getTopActivityNoRemove() {

        if (activites.empty()) {
            return null;
        }
        return activites.elementAt(activites.size() - 1);

    }

    public static TAbsActivity findActivity(String clazz) {
        if (clazz == null) {
            return null;
        }

        for (int i = activites.size() - 1; i >= 0; i--) {
            TAbsActivity activity = activites.elementAt(i);
            if (clazz.equals((activity).getClass().getName())) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 往栈中压入activity
     */
    public static void push(TAbsActivity activity) {
        if (activity != null && null != activites) {
            activites.push(activity);
        }
    }

    /**
     * 销毁栈的activity,并清空栈
     */
    public static void finishAll() {
        while (!activites.isEmpty()) {
            finish(getTopActivityNoRemove());
        }
        activites.clear();
    }

    /**
     * 销毁栈顶的若干activity
     */
    public static void finishStubTop() {
        while (size() > stubIdx) {
            finish(getTopActivity());
        }
        stubIdx = Integer.MAX_VALUE;
    }

    public static void tagStub() {
        stubIdx = size();
    }

    /**
     * 获取栈的大小
     */
    public static int size() {
        if (null != activites) {
            return activites.size();
        }
        return 0;
    }

    public static ArrayList<TAbsActivity> getActivityList() {
        return new ArrayList<>(activites);
    }
}
