package com.monk.reader.presenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.monk.reader.R;
import com.monk.reader.download.DownloadInfo;
import com.monk.reader.presenter.BaseAdapter;
import com.monk.reader.presenter.BaseHolder;
import com.monk.reader.presenter.holder.DownloadHolder;



public class DownloadAdapter extends BaseAdapter<DownloadInfo> {
    public DownloadAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownloadHolder(View.inflate(context, R.layout.item_download, null),context);
    }
}
