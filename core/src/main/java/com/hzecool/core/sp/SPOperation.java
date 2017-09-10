package com.hzecool.core.sp;


import com.hzecool.core.common.utils.SPUtils;
import com.hzecool.core.data.AppConstants;


/**
 * SP常用操作
 * Created by tutu on 2017/3/6.
 */

public class SPOperation {

    /**
     * 清除所有的sp数据
     */
    public static void clearSp() {
        SPUtils.clear();
    }

    /**
     * 保存sessionid
     */
    public static void saveSessionId(String sessionId) {
        SPUtils.putString(AppConstants.SESSIONID, sessionId);
    }

    /**
     * 获取sessionid
     */
    public static String getSessionId() {
        return SPUtils.getString(AppConstants.SESSIONID);
    }
}
