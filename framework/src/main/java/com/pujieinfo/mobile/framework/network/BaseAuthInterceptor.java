package com.pujieinfo.mobile.framework.network;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 验证 拦截器
 */
public class BaseAuthInterceptor implements Interceptor {

    private String name, psw;

    public BaseAuthInterceptor(String name, String psw) {
        this.name = name;
        this.psw = psw;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String credentials = name + ":" + psw;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", basic)
                .header("Accept", "application/json")
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
