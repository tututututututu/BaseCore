package com.tutu.basecore.net;

import com.hzecool.core.bean.BaseResponseBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tutu on 2017/9/10.
 */

public interface ApiInterface {
    /**
     * 助手获取路由
     */
    @FormUrlEncoded
    @POST("api.do")
    Observable<BaseResponseBean<RouterBean>> requestSlhRouter(
            @FieldMap Map<String, String> paramsMap
    );
}
