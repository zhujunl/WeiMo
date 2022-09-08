package com.miaxis.mr230m.miaxishttp.net;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author ZJL
 * @date 2022/9/6 16:47
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MiaxisRetrofit {
   private static final String TAG = "MiaxisRetrofit";

   public static WeiAPi getApiService(String token,String path) {
      Retrofit retrofit = new Retrofit.Builder()
              //设置OKHttpClient,如果不设置会提供一个默认的
              .client(genericClient(token))
              .baseUrl(path)
              .addConverterFactory(ScalarsConverterFactory.create())
              //添加Gson转换器
              .addConverterFactory(GsonConverterFactory.create())
              .build();

      WeiAPi service = retrofit.create(WeiAPi.class);
      return service;
   }


   public static OkHttpClient genericClient(String token) {
      OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).
              readTimeout(10, TimeUnit.SECONDS).
              writeTimeout(10, TimeUnit.SECONDS)
              .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
              .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
              .addInterceptor(new Interceptor() {
                 @Override
                 public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization",token)
                            .build();

                    return chain.proceed(request);
                 }

              })
              .build();

      return httpClient;
   }

   public static WeiAPi TokenService(String path){
       Retrofit retrofit = new Retrofit.Builder()
               .addConverterFactory(ScalarsConverterFactory.create())
               .baseUrl(path)
               .client(genericClient())
               //添加Gson转换器
               .addConverterFactory(GsonConverterFactory.create())
               .build();

       WeiAPi service = retrofit.create(WeiAPi.class);
       return service;
    }

    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).
                readTimeout(10, TimeUnit.SECONDS).
                writeTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
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

    public static class SSLSocketClient {

        //获取这个SSLSocketFactory
        public static SSLSocketFactory getSSLSocketFactory() {
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, getTrustManager(), new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //获取TrustManager
        private static TrustManager[] getTrustManager() {
            return new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};
        }

        //获取HostnameVerifier
        public static HostnameVerifier getHostnameVerifier() {
            return new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };
        }

    }
}
