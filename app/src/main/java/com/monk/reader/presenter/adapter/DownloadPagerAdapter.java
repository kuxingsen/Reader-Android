package com.monk.reader.presenter.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.monk.reader.ui.fragment.DownloadItemFragment;


public class DownloadPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;

    public DownloadPagerAdapter(FragmentManager fm, String[] title) {
        super(fm);
        this.titles = title;
    }

    @Override
    public Fragment getItem(int position) {
        DownloadItemFragment fragment = new DownloadItemFragment();
        Bundle args = new Bundle();
        args.putBoolean(DownloadItemFragment.ARGS_DOWNLOAD, position == 0);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
