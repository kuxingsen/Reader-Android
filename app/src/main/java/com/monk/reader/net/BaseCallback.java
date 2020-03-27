package com.monk.reader.net;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heyao on 8/6/17.
 */

public class BaseCallback<T> implements Callback<T> {

    private CallBackListener listener;
    private int requestCode;

    public BaseCallback(CallBackListener listener, int requestCode) {
        this.listener = listener;
        this.requestCode = requestCode;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            listener.onSuccess(requestCode, response.body());
        } else {
            listener.onFailed(requestCode, response.message());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        listener.onFailed(requestCode, t.getMessage());
    }

    public interface CallBackListener {
        void onSuccess(int requestCode, Object object);

        void onFailed(int requestCode, String message);
    }
}
