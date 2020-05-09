package com.monk.reader.ui.activity;

import android.os.Environment;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.monk.reader.R;
import com.monk.reader.download.DownloadManager;
import com.monk.reader.presenter.adapter.DownloadPagerAdapter;
import com.monk.reader.ui.base.BaseActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/activity/download")
public class DownloadActivity extends BaseActivity {
    @BindView(R.id.download_clear_tv)
    TextView download_clear_tv;
    @BindView(R.id.download_back_iv)
    ImageView download_back_iv;
    @BindView(R.id.download_tab)
    TabLayout download_tab;
    @BindView(R.id.download_pager)
    ViewPager download_pager;
    @BindView(R.id.download_state_tv)
    TextView download_state_tv;

    private String[] mTitle = new String[]{"已完成", "下载中"};

    @Override
    protected int inflateLayout() {
        return R.layout.activity_download;
    }

    @Override
    protected void initAfter() {
        download_tab.setTabMode(TabLayout.MODE_FIXED);
        download_tab.addTab(download_tab.newTab().setText(mTitle[0]), 0);
        download_tab.addTab(download_tab.newTab().setText(mTitle[1]), 1);
        DownloadPagerAdapter pagerAdapter = new DownloadPagerAdapter(getSupportFragmentManager(), mTitle);
        download_pager.setAdapter(pagerAdapter);
        download_tab.setupWithViewPager(download_pager);

        File storageDirectory = Environment.getExternalStorageDirectory();
        String free = Formatter.formatFileSize(getApplicationContext(), storageDirectory.getUsableSpace());
        String used = Formatter.formatFileSize(getApplicationContext(), storageDirectory.getTotalSpace() - storageDirectory.getUsableSpace());
        download_state_tv.setText(String.format("已占用%s 可用空间%s", used, free));
    }

    @OnClick(R.id.download_back_iv)
    public void download_back_iv_OnClick(View v) {
        onBackPressed();
    }

    @OnClick(R.id.download_clear_tv)
    public void download_clear_tv_OnClick(View v) {
        DownloadManager.deleteAllTask();
    }
}
