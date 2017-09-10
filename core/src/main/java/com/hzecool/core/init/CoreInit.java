package com.hzecool.core.init;

import android.text.TextUtils;

import com.hzecool.core.data.AppData;
import com.hzecool.core.log.L;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by 47066 on 2017/9/8.
 */

public class CoreInit {

    private static final CoreInit instance = new CoreInit();

    /**
     * 参数拦截器初始化  所有接口必穿参数设置(根据需求调用)
     */
    private Map<String, String> mRequestParams;

    private CoreInit() {
    }


    public static CoreInit getInstance() {
        return instance;
    }

    public void setRequestParams(Map<String, String> requestParams) {
        if (requestParams == null || requestParams.isEmpty()) {
            return;
        }
        this.mRequestParams = requestParams;
    }

    public Iterator<Map.Entry<String, String>> getRequestParamsSet() {
        if (mRequestParams == null) {
            return null;
        }
        return mRequestParams.entrySet().iterator();
    }

    public void setDebugMode(boolean isDebug) {
        AppData.LOG_DEBUG = isDebug;
    }

    public void setBaseUrl(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            L.e("baseUrl不能传空");
            return;
        }
        AppData.BaseUrl = baseUrl;
    }
}
