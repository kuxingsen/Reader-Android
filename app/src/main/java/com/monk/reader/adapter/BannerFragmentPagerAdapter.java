package com.monk.reader.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.monk.reader.retrofit2.bean.Banner;
import com.monk.reader.ui.fragment.BannerFragment;

import java.util.ArrayList;
import java.util.List;

public class BannerFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "BannerPagerAdapter";
    private List<Banner> bannerList;
    private List<BannerFragment> bannerFragmentList;
    public BannerFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        bannerFragmentList = new ArrayList<>();
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
        for (Banner b:bannerList) {
            bannerFragmentList.add(new BannerFragment(b));
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.i(TAG, "getItem: "+position);
//            return getFragmentManager().findFragmentByTag("" + bannerList.get(position).getId());
        return bannerFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

}
