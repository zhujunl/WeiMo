package com.miaxis.mr230m.http.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * @author ZJL
 * @date 2022/6/17 16:51
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MyRetrofit {
    private static final String TAG = "MyRetrofit";


    public static Api getApiService(String path) {
        Retrofit retrofit = new Retrofit.Builder()
                //设置OKHttpClient,如果不设置会提供一个默认的
                .client(genericClient())
                .baseUrl(path)
                .addConverterFactory(ScalarsConverterFactory.create())
                //添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api service = retrofit.create(Api.class);
        return service;
    }


    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).
                readTimeout(10, TimeUnit.SECONDS).
                writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .build();

                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }


}
