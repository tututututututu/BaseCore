package com.hzecool.core.update.provider;


import com.hzecool.core.bean.BaseResponseBean;
import com.hzecool.core.common.utils.AppUtils;
import com.hzecool.core.net.Api;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;


/**
 * 检查更新
 * Created by tutu on 2017/5/25.
 */

public class ProviderNet {
    public static Observable<BaseResponseBean<NetUpdateBean>> requestCheckUpdate(String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("devicetype", "12");
        params.put("productVersion", AppUtils.getAppVersionName());
        params.put("deviceno", phone);
        params.put("dlProductCode", "clerkAssistAndroid");

        return Api.getInstance(ApiIterface.class).requestUpdate(params);
    }
}
