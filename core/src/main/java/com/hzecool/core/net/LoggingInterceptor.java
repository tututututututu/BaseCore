package com.hzecool.core.net;


import com.hzecool.core.log.L;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;


/**
 * retrofit日志拦截器
 * Created by tutu on 2017/1/10.
 */

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Response response = chain.proceed(request);


        ResponseBody responseBody = response.body();

        String responseBodyString = response.body().string();

        Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();


        if (!String.valueOf(newResponse.code()).startsWith("2")) {
            StringBuffer sb = new StringBuffer();
            sb.append("请求失败,返回码为" + newResponse.code() + "\n" +
                    "请求数据: " + response.request().url() + "&" + bodyToString(request))
                    .append("\n\n");
            L.logFile(sb.toString());
        }


        long t2 = System.nanoTime();
        //Timber.i("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers());

        StringBuffer sb = new StringBuffer();
        sb.append("请求数据: " + response.request().url() + "&" + bodyToString(request))
                .append("\n\n")
                .append("返回数据: " + responseBodyString)
                .append("\n\n")
                .append("耗时: " + (t2 - t1) / 1e6d + "毫秒 " + request.url());

        L.logFile(sb.toString());
        return newResponse;
    }

    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
//            return buffer.readUtf8();
            return URLDecoder.decode(buffer.readUtf8(), "utf-8");
        } catch (final IOException e) {
            L.logFile(e.getMessage());
            e.printStackTrace();
            return "请求参数解析失败";
        }
    }
}
