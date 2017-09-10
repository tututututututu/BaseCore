package com.tutu.basecore.net;

import com.hzecool.core.bean.BaseResponseBean;
import com.hzecool.core.net.Api;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;


/**
 * Created by tutu on 2017/9/10.
 */

public class NetProvider {
    public static Observable<BaseResponseBean<RouterBean>> requestSlhRouter(String mobile) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", mobile);
        params.put("apiKey", "ec-assistant-oa-get-staff");
        params.put("sessionid","xxxxxxxxx");
        return Api.getInstance(ApiInterface.class).requestSlhRouter(params);
    }
}
