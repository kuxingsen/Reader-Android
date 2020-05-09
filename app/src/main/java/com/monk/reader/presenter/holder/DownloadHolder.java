package com.monk.reader.presenter.holder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.core.content.FileProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.monk.reader.BaseApplication;
import com.monk.reader.R;
import com.monk.reader.dao.bean.ShelfBook;
import com.monk.reader.download.DownloadInfo;
import com.monk.reader.download.DownloadManager;
import com.monk.reader.eventbus.AddToShelfEvent;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.presenter.BaseHolder;
import com.monk.reader.utils.ToastUtils;

import butterknife.BindView;

public class DownloadHolder extends BaseHolder<DownloadInfo> {
    public DownloadHolder(View itemView, Context context) {
        super(itemView,context);
    }

    @BindView(R.id.item_download_cover_iv)
    ImageView item_download_cover_iv;
    @BindView(R.id.item_download_name_tv)
    TextView item_download_name_tv;
    @BindView(R.id.item_download_author_tv)
    TextView item_download_author_tv;
    @BindView(R.id.item_download_category_tv)
    TextView item_download_category_tv;
    @BindView(R.id.item_download_progress_pb)
    ProgressBar item_download_progress_pb;
    @BindView(R.id.item_download_button_iv)
    ImageView item_download_button_iv;
    @BindView(R.id.item_download_state_tv)
    TextView item_download_state_tv;

    @Override
    public void bindData(int position, DownloadInfo downloadInfo) {
        item_download_name_tv.setText(downloadInfo.getName());
        item_download_author_tv.setText(downloadInfo.getAuthor());
        item_download_progress_pb.setProgress((int) (downloadInfo.getCurrent() * 100.0 / downloadInfo.getTotal()));
        item_download_category_tv.setText(downloadInfo.getCategory());
        Glide.with(context).load("http://192.168.43.14:8080"+downloadInfo.getPicture()).into(item_download_cover_iv);
        itemView.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putLong("bookId", Long.valueOf(downloadInfo.getBookId()));
            bundle.putString("from", "network");
            ARouter.getInstance().build("/activity/info").with(bundle).navigation();
        });
        setStateUI(downloadInfo.getState());
        final DownloadInfo map = DownloadManager.map(downloadInfo);
        map.setOnDownloadListener(new DownloadInfo.DownloadListener() {
            @Override
            public void onDownloadProgressChanged(long current, long total) {
                int progress = (int) (current * 100.0 / total);
                item_download_progress_pb.setProgress(progress);
                item_download_state_tv.setText(String.format("下载中%s%%", progress));
                item_download_button_iv.setImageResource(R.mipmap.download_pause);
            }

            @Override
            public void onDownloadStateChanged(int state) {
                setStateUI(state);
            }
        });
        item_download_button_iv.setOnClickListener(view -> {
            switch (map.getState()) {
                case DownloadInfo.NONE:
                case DownloadInfo.PAUSE:
                case DownloadInfo.FAILED:
                    DownloadManager.download(map);
                    break;
                case DownloadInfo.WAITING:
                case DownloadInfo.DOWNLOADING:
                    DownloadManager.pause(map);
                    break;
                case DownloadInfo.FINISH:
                    open(downloadInfo);
                    break;
            }
        });
    }

    private void setStateUI(int state) {
        switch (state) {
            case DownloadInfo.NONE:
                item_download_state_tv.setText("下载");
                item_download_button_iv.setImageResource(R.mipmap.download_start);
                break;
            case DownloadInfo.DOWNLOADING:
                item_download_state_tv.setText("连接中...");
                item_download_button_iv.setImageResource(R.mipmap.download_pause);
                break;
            case DownloadInfo.PAUSE:
                item_download_state_tv.setText("继续");
                item_download_button_iv.setImageResource(R.mipmap.download_start);
                break;
            case DownloadInfo.FINISH:
                item_download_state_tv.setText("已完成");
                item_download_progress_pb.setProgress(100);
                item_download_button_iv.setImageResource(R.mipmap.download_ok);
                break;
            case DownloadInfo.WAITING:
                item_download_state_tv.setText("等待中");
                item_download_button_iv.setImageResource(R.mipmap.download_pause);
                break;
            case DownloadInfo.FAILED:
                item_download_state_tv.setText("失败");
                item_download_button_iv.setImageResource(R.mipmap.download_error);
                break;
        }
    }

    private void open(DownloadInfo downloadInfo) {

        ShelfBook newBook = new ShelfBook();
        newBook.setName(downloadInfo.getName());
        newBook.setBegin(0L);
        newBook.setCharset(null);
        newBook.setPath(downloadInfo.getFile().getAbsolutePath());
        newBook.setFrom("local");
        Log.i(TAG, "initAfter: newBook"+newBook);
        RxBus.getDefault().post(new AddToShelfEvent(newBook));

        ToastUtils.showToast("已加入书架");
    }
}
