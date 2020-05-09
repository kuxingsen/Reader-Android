package com.monk.reader.download;

import android.util.Log;

import com.monk.reader.BaseApplication;
import com.monk.reader.eventbus.DownloadChangedEvent;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.retrofit2.DownloadApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class DownloadTask implements Runnable {
    private static final String TAG = "DownloadTask";
    private DownloadInfo mDownloadInfo;
    private boolean mIsRemoved;

    @Inject
    DownloadApi downloadApi;

    public DownloadTask(DownloadInfo downloadInfo) {
        mDownloadInfo = downloadInfo;
        mDownloadInfo.setTask(this);
        BaseApplication.getInstance().getAppComponent().inject(this);
    }

    public void remove() {
        mIsRemoved = true;
    }

    @Override
    public void run() {
        mDownloadInfo.setState(DownloadInfo.DOWNLOADING);
        File file = mDownloadInfo.getFile();
        Log.i(TAG, "run: "+file.getAbsolutePath());
        long start = file.length();
        InputStream in = null;
        FileOutputStream out = null;
        try {
            Response<ResponseBody> response = downloadApi.download(mDownloadInfo.getUrl(), start).execute();
            if (mIsRemoved) return;
            if (response != null && response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    long current = start;
                    long total = body.contentLength() + start;
                    Log.i(TAG, "run: "+current+"   "+total);
                    in = body.byteStream();
                    out = new FileOutputStream(file, true);
                    byte[] buffer = new byte[10240];
                    int len;
                    while ((len = in.read(buffer)) != -1 && mDownloadInfo.getState() == DownloadInfo.DOWNLOADING && !mIsRemoved) {
                        Log.i(TAG, "run: "+len);
                        out.write(buffer, 0, len);
                        current += len;
                        mDownloadInfo.setProgress(current, total);
                    }
                    ThreadPoolManager.getInstance().remove(this);
                    Log.i(TAG, "run: "+current+"   "+total);
                    if (current == total) {
                        mDownloadInfo.setState(DownloadInfo.FINISH);
                        RxBus.getDefault().post(new DownloadChangedEvent());
                    }
                } else {
                    mDownloadInfo.setState(DownloadInfo.FAILED);
                }
            } else {
                mDownloadInfo.setState(DownloadInfo.FAILED);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mDownloadInfo.setState(DownloadInfo.FAILED);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
