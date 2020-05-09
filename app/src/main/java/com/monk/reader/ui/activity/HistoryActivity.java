package com.monk.reader.ui.activity;


import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.monk.reader.R;
import com.monk.reader.dao.ReadInfoDao;
import com.monk.reader.dao.bean.ReadInfo;
import com.monk.reader.eventbus.DownloadChangedEvent;
import com.monk.reader.eventbus.ReadBookEvent;
import com.monk.reader.eventbus.RxEvent;
import com.monk.reader.presenter.adapter.HistoryAdapter;
import com.monk.reader.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

@Route(path = "/activity/history")
public class HistoryActivity extends BaseActivity {
    private static final String TAG = "HistoryActivity";
    @BindView(R.id.history_recycler_view)
    RecyclerView mRecyclerView;

    private HistoryAdapter mHistoryAdapter;

    @Inject
    ReadInfoDao readInfoDao;

    @Override
    protected void initBefore() {
        super.initBefore();
        getmApplication().getAppComponent().inject(this);
    }

    @Override
    protected int inflateLayout() {
        return R.layout.activity_history;
    }

    @Override
    protected void initAfter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mHistoryAdapter = new HistoryAdapter(mApplication);
        mRecyclerView.setAdapter(mHistoryAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        List<ReadInfo> list = readInfoDao.queryRaw("order by Update_Time desc");
        Log.i(TAG, "onStart: "+list);
        mHistoryAdapter.setData(list);
    }
}
