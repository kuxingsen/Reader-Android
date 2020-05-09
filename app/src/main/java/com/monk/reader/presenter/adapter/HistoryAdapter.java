package com.monk.reader.presenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monk.reader.R;
import com.monk.reader.dao.bean.ReadInfo;
import com.monk.reader.presenter.BaseAdapter;
import com.monk.reader.presenter.BaseHolder;
import com.monk.reader.presenter.holder.HistoryHolder;

import butterknife.BindView;


public class HistoryAdapter extends BaseAdapter<ReadInfo> {

    public HistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_history, parent, false);
        return new HistoryHolder(view, mApp);
    }
}
