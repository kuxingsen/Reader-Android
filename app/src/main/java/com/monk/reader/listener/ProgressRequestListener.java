package com.monk.reader.listener;

/**
 * Created by heyao on 2017/7/28.
 */

public interface ProgressRequestListener {

    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
