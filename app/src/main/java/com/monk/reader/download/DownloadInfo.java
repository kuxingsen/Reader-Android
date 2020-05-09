package com.monk.reader.download;


import android.os.Environment;

import androidx.annotation.IntDef;

import com.monk.reader.BaseApplication;
import com.monk.reader.dao.DownloadInfoDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Entity
public class DownloadInfo {
    public static final int NONE = 0;
    public static final int DOWNLOADING = 1;
    public static final int PAUSE = 2;
    public static final int FINISH = 3;
    public static final int FAILED = 4;
    public static final int WAITING = 5;

    public static final DownloadInfoDao DAO = BaseApplication.getInstance().getDaoSession().getDownloadInfoDao();

    @IntDef({NONE, DOWNLOADING, PAUSE, FINISH, FAILED, WAITING})
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
    @interface DownloadState {
    }

    @Id(autoincrement = true)
    private Long id;
    // Book 属性
    @NotNull
    @Unique
    private String bookId;
    private String name;
    private String author;
    private String category;
    private String picture;
    @NotNull
    private String url;
    // 下载属性
    private long current;
    private long total;
    @DownloadState
    private int state;
    @Transient
    private DownloadListener listener;
    @Transient
    private DownloadTask task;



    @Generated(hash = 414364509)
    public DownloadInfo(Long id, @NotNull String bookId, String name, String author, String category, String picture,
            @NotNull String url, long current, long total, int state) {
        this.id = id;
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.category = category;
        this.picture = picture;
        this.url = url;
        this.current = current;
        this.total = total;
        this.state = state;
    }

    @Generated(hash = 327086747)
    public DownloadInfo() {
    }



    public File getFile() {

        File download = new File(Environment.getExternalStorageDirectory()+ "/kuexun/reader/download/");
        if (!download.exists()) {
            download.mkdirs();
        }
        return new File(download, String.format("%s_%s.txt", name, id));
    }

    @DownloadState
    public int getState() {
        return state;
    }

    public void setState(@DownloadState final int state) {
        DownloadInfo.this.state = state;
        DAO.update(this);
        BaseApplication.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onDownloadStateChanged(state);
                }
            }
        });
    }

    public void setProgress(final long current, final long total) {
        DownloadInfo.this.current = current;
        DownloadInfo.this.total = total;
        DAO.update(this);
        BaseApplication.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onDownloadProgressChanged(current, total);
                }
            }
        });
    }

    public void setOnDownloadListener(DownloadListener listener) {
        this.listener = listener;
    }

    public void refreshState() {
        File file = getFile();
        this.current = file.length();
        if (!file.exists()) {
            setState(NONE);
        } else if (file.length() == 0) {
            setState(NONE);
        } else if (file.length() == total) {
            setState(FINISH);
        } else {
            setState(PAUSE);
        }
    }

    public interface DownloadListener {
        void onDownloadProgressChanged(long current, long total);

        void onDownloadStateChanged(int state);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCurrent() {
        return this.current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Long getId() {
        return this.id;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DownloadTask getTask() {
        return task;
    }

    public void setTask(DownloadTask task) {
        this.task = task;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
