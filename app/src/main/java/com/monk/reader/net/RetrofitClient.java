package com.monk.reader.net;



import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by heyao on 2017/7/18.
 */

public class RetrofitClient {

    private static Retrofit mRetrofit;

    private static Retrofit retrofitClient() {
        if (mRetrofit == null) {
            synchronized (RetrofitClient.class) {
                if (mRetrofit == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    //设置公共参数
//                    builder.addInterceptor(new Interceptor() {
//                        @Override
//                        public Response intercept(Chain chain) throws IOException {
//                            Request oldRequest = chain.request();
//                            HttpUrl httpUrl = oldRequest.url();
//                            HttpUrl newUrl = httpUrl.newBuilder().addEncodedQueryParameter("accessToken", "bab8c930-aec4-4e97-bfb8-d447b8ba3491").build();
//                            Request newRequest = oldRequest.newBuilder().url(newUrl).build();
//                            return chain.proceed(newRequest);
//                        }
//                    });
                    builder.connectTimeout(30, TimeUnit.SECONDS);
                    builder.readTimeout(30, TimeUnit.SECONDS);
                    builder.writeTimeout(30, TimeUnit.SECONDS);
                    OkHttpClient okHttpClient = builder.build();

                    Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
                    retrofitBuilder.client(okHttpClient);
                    retrofitBuilder.baseUrl("https://www.fastmock.site/mock/585d70a5d2130ab1fbcf2b7b65fc7b37/test/");
                    retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
                    mRetrofit = retrofitBuilder.build();
                }
            }
        }
        return mRetrofit;
    }

    public static <T> T getService(Class<T> clazz) {
        if (mRetrofit == null) {
            synchronized (RetrofitClient.class) {
                if (mRetrofit == null) {
                    mRetrofit = retrofitClient();
                }
            }
        }
        return mRetrofit.create(clazz);
    }

}
