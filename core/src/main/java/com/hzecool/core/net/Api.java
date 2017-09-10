package com.hzecool.core.net;


/**
 * 对外提供统一的获取请求服务实例
 * Created by tutu on 16-11-17
 */
public class Api {

    /**
     * 获取一般请求的服务实例
     *
     * @param clazz 服务实例类型
     * @param <T>   泛型
     * @return 指定的服务类型的实例
     */
    public static <T> T getInstance(Class<T> clazz) {
        return ApiManager.getService(clazz);
    }

    /**
     * 获取指定Url请求的服务实例
     *
     * @param clazz   服务实例类型
     * @param baseUrl 指定的BaseUrl
     * @param <T>     泛型
     * @return 指定的服务类型和Url的实例
     */
    public static <T> T getInstanceWithBaseUrl(Class<T> clazz, String baseUrl) {
        return ApiManager.getServiceWithBaseUrl(clazz, baseUrl);
    }
}
