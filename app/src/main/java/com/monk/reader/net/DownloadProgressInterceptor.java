package com.monk.reader.net;


import com.monk.reader.listener.ProgressResponseListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by heyao on 2017/7/28.
 */

public class DownloadProgressInterceptor implements Interceptor {

    private ProgressResponseListener listener;

    public DownloadProgressInterceptor(ProgressResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), listener)).build();
    }
}
