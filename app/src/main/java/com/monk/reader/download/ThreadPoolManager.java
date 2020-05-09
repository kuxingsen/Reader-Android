package com.monk.reader.download;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    private static final int CORE_PORE_SIZE = 2; // 多线程最大个数
    private static ThreadPoolManager mThreadPoolManager;
    private ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolManager() {
        mThreadPoolExecutor = new ThreadPoolExecutor(CORE_PORE_SIZE,
                CORE_PORE_SIZE, 1, TimeUnit.HOURS,
                new LinkedBlockingDeque<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static ThreadPoolManager getInstance() {
        if (mThreadPoolManager == null) {
            mThreadPoolManager = new ThreadPoolManager();
        }
        return mThreadPoolManager;
    }

    public void execute(DownloadTask r) {
        if (mThreadPoolExecutor != null && r != null) {
            mThreadPoolExecutor.execute(r);
        }
    }

    public void remove(DownloadTask r) {
        if (mThreadPoolExecutor != null && r != null) {
            mThreadPoolExecutor.remove(r);
            r.remove();
        }
    }
}
