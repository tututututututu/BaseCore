package com.hzecool.core.update.provider;

import com.hzecool.core.bean.BaseResponseBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * 获取服务器最新版本信息，访问网络接口
 * Created by tutu on 2017/3/16.
 */
public interface ApiIterface {
    /**
     * 升级检测
     */
    @FormUrlEncoded
    @POST("checkUpgrade.do")
    Observable<BaseResponseBean<NetUpdateBean>> requestUpdate(
            @FieldMap Map<String, String> paramsMap
    );
}
