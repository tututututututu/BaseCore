package com.hzecool.core.net;
import com.hzecool.core.data.AppData;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 提供retrofit apiService管理
 * 缓存使用过的ApiService实例
 * Created by tutu on 2017/3/15.
 */

public class ApiManager {
    private static Map<Class, Object> apiServiceMap = new HashMap<>();
    public static final int CONNECT_TIME_OUT_SECONDS = 15;
    public static final int READ_TIME_OUT_SECONDS = 15;


    /**
     * 获取ApiService
     *
     * @param service ApiService类型
     * @param <T>     泛型类型
     * @return
     */
    public static <T> T getService(final Class service) {
        T api = (T) apiServiceMap.get(service);
        if (api == null) {
            Object o = creatService(service);
            apiServiceMap.put(service, o);
            return (T) o;
        }
        return (T) apiServiceMap.get(service);
    }

    /**
     * 获取ApiService 可以修改BaseUrl
     *
     * @param service ApiService类型
     * @param baseUrl 指定的BaseUrl
     * @param <T>
     * @return
     */
    public static <T> T getServiceWithBaseUrl(final Class<T> service, String baseUrl) {
        T api = (T) apiServiceMap.get(service);
        if (api == null) {
            Object o = creatServiceWithBaseUrl(service, baseUrl);
            apiServiceMap.put(service, o);
            return (T) o;
        }
        return (T) apiServiceMap.get(service);
    }


    /**
     * 创建ApiService实例
     *
     * @param service Service类型
     * @param <T>
     * @return
     */
    private static <T> T creatService(final Class<T> service) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(AppData.LOG_DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT_SECONDS, TimeUnit.SECONDS)
//                .addInterceptor(logging)
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new ParamsInterceptord())
                .build();

        return new Retrofit.Builder()
                .baseUrl(AppData.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(service);
    }


    /**
     * 创建ApiService实例
     *
     * @param service Service类型
     * @param baseUrl baseUrl
     * @param <T>
     * @return
     */
    private static <T> T creatServiceWithBaseUrl(final Class<T> service, String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(AppData.LOG_DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT_SECONDS, TimeUnit.SECONDS)
//                .addInterceptor(logging)
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new ParamsInterceptord())
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(service);
    }

}
