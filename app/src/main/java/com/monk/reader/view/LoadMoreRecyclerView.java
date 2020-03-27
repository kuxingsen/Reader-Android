package com.monk.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoadMoreRecyclerView extends RecyclerView {
    private static final String TAG = "LoadMoreRecyclerView";


    private LoadMoreListener mLoadMoreListener;

    public LoadMoreRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreListener != null) {
            Log.d("LoadMoreRecyclerView", "run in onScrollStateChanged");
            LayoutManager layoutManager = getLayoutManager();
            int lastVisiblePosition = -1;
            if (layoutManager instanceof LinearLayoutManager) {
                lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            Log.d("LoadMoreRecyclerView", "ChildCount: " + layoutManager.getChildCount() + " lastvisiblePosition: "
                    + lastVisiblePosition + " ItemCount: " + layoutManager.getItemCount());
            if (layoutManager.getChildCount() > 0             //当当前显示的item数量>0
                    && lastVisiblePosition >= layoutManager.getItemCount() - 1           //当当前屏幕最后一个加载项位置>=所有item的数量
                    && layoutManager.getItemCount() > layoutManager.getChildCount()) { // 当当前总Item数大于可见Item数
                Log.d("LoadMoreRecyclerView", "run onLoadMore");

                mLoadMoreListener.onLoadMore();
            }
        }
    }

    public void setmLoadMoreListener(LoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

}
