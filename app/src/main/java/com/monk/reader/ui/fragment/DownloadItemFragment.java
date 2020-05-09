package com.monk.reader.ui.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.monk.reader.R;
import com.monk.reader.dao.DownloadInfoDao;
import com.monk.reader.download.DownloadInfo;
import com.monk.reader.eventbus.DownloadChangedEvent;
import com.monk.reader.eventbus.RxEvent;
import com.monk.reader.presenter.adapter.DownloadAdapter;
import com.monk.reader.ui.base.BaseFragment;

import java.util.List;

import butterknife.BindView;

public class DownloadItemFragment extends BaseFragment {
    public static final String ARGS_DOWNLOAD = "ARGS_DOWNLOAD";

    @BindView(R.id.download_recycler_view)
    RecyclerView download_recycler_view;
    @BindView(R.id.download_swipe_view)
    SwipeRefreshLayout download_swipe_view;

    private DownloadAdapter mDownloadAdapter;
    private boolean mFinished;

    @Override
    protected int layoutId() {
        return R.layout.fragment_download_item;
    }

    @Override
    protected void initBefore() {
        mDownloadAdapter = new DownloadAdapter(getContext());
        mFinished = getArguments().getBoolean(ARGS_DOWNLOAD);
    }

    @Override
    protected void initView(Bundle savedInstanceState, View view) {
        super.initView(savedInstanceState,view);
        download_swipe_view.setOnRefreshListener(this::refresh);
    }

    @Override
    protected void initAfter() {
        download_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        download_recycler_view.setAdapter(mDownloadAdapter);
        refresh();
    }

    private void refresh() {
        List<DownloadInfo> infos;
        if (mFinished) {
            infos = DownloadInfo.DAO.queryBuilder()
                    .where(DownloadInfoDao.Properties.State.eq(DownloadInfo.FINISH))
                    .orderDesc(DownloadInfoDao.Properties.Id).list();
        } else {
            infos = DownloadInfo.DAO.queryBuilder()
                    .where(DownloadInfoDao.Properties.State.notEq(DownloadInfo.FINISH))
                    .orderDesc(DownloadInfoDao.Properties.Id).list();
        }
        mDownloadAdapter.addDataBeforeClean(infos);
        mDownloadAdapter.notifyDataSetChanged();
        download_swipe_view.setRefreshing(false);
    }

    @Override
    protected void onEventMainThread(RxEvent rxEvent) {
        if (rxEvent instanceof DownloadChangedEvent) {
            refresh();
        }
    }
}
