package com.monk.reader.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WrapperAdapter extends RecyclerView.Adapter {
    private static final String TAG = "WrapperAdapter";
    private RecyclerView.Adapter mAdapter;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;
    private List<View> headerViewList;
    private List<View> footerViewList;

    public WrapperAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
        headerViewList = new ArrayList<>();
        footerViewList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        int headerCount = headerViewList.size();
        //头部
        if(position< headerCount){
            return TYPE_HEADER;
        }
        //原数据
        int adjPosition = position - headerCount;
        int adapterCount = mAdapter.getItemCount();
        if(adjPosition < adapterCount){
            return super.getItemViewType(adjPosition);
        }
        //尾部
        return TYPE_FOOTER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        if(TYPE_HEADER == viewType ){
            //这样的做法就是只有第一个header生效
            return new ViewHolder(headerViewList.get(0));
        }
        if(TYPE_FOOTER == viewType){
            return new ViewHolder(footerViewList.get(0));
        }

        return mAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int headerCount = headerViewList.size();
        //头部
        if(position< headerCount){
            //默认header是设置好了才放到适配器中
            //todo 多个头部时怎么搞
            return ;
        }
        //原数据
        int adjPosition = position - headerCount;
        int adapterCount = mAdapter.getItemCount();
        if(adjPosition < adapterCount){
            mAdapter.onBindViewHolder(holder,adjPosition);
        }
        //尾部
        //默认footer是设置好了才放到适配器中
        //todo 多个尾部时怎么搞
    }

    @Override
    public int getItemCount() {
        return headerViewList.size()+mAdapter.getItemCount()+footerViewList.size();
    }
    public void addHeaderView(View view){
        headerViewList.add(view);
    }
    public void addHeaderView(View view,int index){
        headerViewList.add(index,view);
    }

    public void addFooterView(View view){
        footerViewList.add(view);
    }
    public void addFooterView(View view,int index){
        footerViewList.add(index,view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
