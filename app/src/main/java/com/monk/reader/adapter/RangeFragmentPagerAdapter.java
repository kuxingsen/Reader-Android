package com.monk.reader.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.monk.reader.ui.fragment.RangeFragment;


public class RangeFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "RangeFragmentPagerAdapter";

    private long rangeId;
    private final String[] RANGE_DURATION={"week","month","total"};


    public RangeFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setRangeId(long rangeId) {
        this.rangeId = rangeId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new RangeFragment(rangeId,RANGE_DURATION[position]);
    }

    @Override
    public int getCount() {
        return RANGE_DURATION.length;
    }
}
