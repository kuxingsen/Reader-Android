package com.monk.reader.presenter;

import android.content.Context;
import android.view.LayoutInflater;


import androidx.recyclerview.widget.RecyclerView;

import com.monk.reader.BaseApplication;
import com.monk.reader.listener.ItemClickListener;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {

    private List<T> list;
    protected LayoutInflater inflater;
    protected Context context;
    protected BaseApplication mApp;
    protected ItemClickListener mClickListener;

    public BaseAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        if (context instanceof BaseApplication) {
            this.mApp = (BaseApplication) context;
        }
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        holder.bindData(position, list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void setData(List<T> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addDatas(List<T> list) {

        this.list.addAll(list);
    }
    public void addData(T d) {
        list.add(d);
    }

    public void addDataBeforeClean(List<T> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }
}
