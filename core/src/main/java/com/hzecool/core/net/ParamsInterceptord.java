package com.hzecool.core.net;


import com.hzecool.core.init.CoreInit;
import com.hzecool.core.log.L;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;


/**
 * retrofit参数拦截器  这里可以统一添加访问参数
 * Created by tutu on 2017/1/10.
 */

public class ParamsInterceptord implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        HttpUrl.Builder builder = originalHttpUrl.newBuilder();

        Iterator<Map.Entry<String, String>> paramsIt = CoreInit.getInstance().getRequestParamsSet();
        if (paramsIt != null) {
            while (paramsIt.hasNext()) {
                builder.addQueryParameter(paramsIt.next().getKey()
                        , paramsIt.next().getValue());
            }
        }
        HttpUrl url = builder.build();
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);
        Request request = requestBuilder.build();

        StringBuffer sb = new StringBuffer();
        sb.append("请求数据: " + request.url() + "&" + bodyToString(request))
                .append("\n\n");
        L.iTag("请求拦截器", sb.toString());
        return chain.proceed(request);
    }


    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return URLDecoder.decode(buffer.readUtf8(), "utf-8");
        } catch (final IOException e) {
            L.logFile(e.getMessage());
            e.printStackTrace();
            return "请求参数解析失败";
        }
    }
}
