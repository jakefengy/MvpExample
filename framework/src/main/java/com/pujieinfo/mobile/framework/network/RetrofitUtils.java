package com.pujieinfo.mobile.framework.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 */
public class RetrofitUtils {

    //

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(getLogInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    private static Interceptor getLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, Interceptor... interceptors) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(getLogInterceptor());

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        OkHttpClient okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    //

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static RequestBody create(String descriptionString) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    public static MultipartBody.Part create(String partName, File file) {

        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static MultipartBody.Part create(String image, String fileName, String partName) {

        File file = new File(image);

        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        return MultipartBody.Part.createFormData(partName, fileName, requestFile);
    }

    //

    public static <T> Observable<T> checkConnected(Context context, Observable<T> observable) {

        return Observable.just(isConnected(context))
                .flatMap(b -> b ? observable : Observable.error(new NetworkUnAvailableThrowable()));

    }

    public static boolean isConnected(Context ctx) {
        Context appContext = ctx.getApplicationContext();
        ConnectivityManager mgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mgr != null) {
            NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isConnected();
            }
        }
        return false;
    }

    public static <T extends BaseResult> ObservableTransformer<T, T> check() {
        return httpResultObservable -> httpResultObservable.flatMap(t -> {

            if (t == null) {
                return Observable.error(new ContentGetFailThrowable());
            } else {
                if (t.isSuccess()) {
                    return Observable.just(t);
                } else {
                    return Observable.error(new Throwable(t.getMsg()));
                }
            }
        });
    }

}
