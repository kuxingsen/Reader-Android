package com.monk.reader.presenter;


import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.monk.reader.BaseApplication;
import com.monk.reader.listener.ItemClickListener;


import butterknife.ButterKnife;


public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {

    protected String TAG = getClass().getSimpleName();
    protected Context context;
    protected ItemClickListener mClickListener;

    public BaseHolder(View itemView) {
        this(itemView, null);
    }

    public BaseHolder(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
    }

    public abstract void bindData(int position, T t);

    public void setClickListener(ItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }
}
