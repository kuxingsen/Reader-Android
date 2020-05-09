package com.monk.reader.download;


import com.monk.reader.dao.DownloadInfoDao;
import com.monk.reader.eventbus.DownloadChangedEvent;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DownloadManager {
    private static ThreadPoolManager mThreadPoolManager = ThreadPoolManager.getInstance();
    private static Map<String, DownloadInfo> MAP = new HashMap<>();

    public static DownloadInfo map(DownloadInfo downloadInfo) {
        String bookId = downloadInfo.getBookId();
        if (MAP.containsKey(bookId)) {
            downloadInfo = MAP.get(bookId);
        } else {
            MAP.put(bookId, downloadInfo);
        }
        return downloadInfo;
    }

    public static DownloadInfo download(DownloadInfo downloadInfo) {
        downloadInfo = map(downloadInfo);
        if (downloadInfo.getState() == DownloadInfo.DOWNLOADING || downloadInfo.getState() == DownloadInfo.WAITING) {
            ToastUtils.showToast("Downloading...");
            return downloadInfo;
        }
        DownloadInfo.DAO.insertOrReplace(downloadInfo);
        downloadInfo.setState(DownloadInfo.WAITING);
        mThreadPoolManager.remove(downloadInfo.getTask());
        DownloadTask task = new DownloadTask(downloadInfo);
        mThreadPoolManager.execute(task);
        return downloadInfo;
    }

    public static void recoveryTask() {
        List<DownloadInfo> infos = DownloadInfo.DAO.queryBuilder().orderDesc(DownloadInfoDao.Properties.Id).list();
        if (infos != null) {
            for (DownloadInfo info : infos) {
                MAP.put(info.getBookId(), info);
                if (info.getState() == DownloadInfo.DOWNLOADING || info.getState() == DownloadInfo.WAITING) {
                    info.setState(DownloadInfo.PAUSE);
                    download(info);
                }
            }
        }
    }

    public static void deleteAllTask() {
        Set<String> keys = MAP.keySet();
        for (String key : keys) {
            mThreadPoolManager.remove(MAP.get(key).getTask());
        }
        MAP.clear();
        List<DownloadInfo> list = DownloadInfo.DAO.loadAll();
        for (DownloadInfo info : list) {
            File file = info.getFile();
            if (file.exists()) {
                file.delete();
            }
        }
        DownloadInfo.DAO.deleteAll();
        RxBus.getDefault().post(new DownloadChangedEvent());
    }

    public static void pause(DownloadInfo downloadInfo) {
        downloadInfo.setState(DownloadInfo.PAUSE);
    }
}
