package com.monk.reader.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.monk.reader.retrofit2.bean.Category;
import com.monk.reader.ui.fragment.CategoryFragment;
import com.monk.reader.ui.fragment.DemoFragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "CategoryFragmentPagerAd";
    private List<Category> categoryList;
    private List<CategoryFragment> categoryFragmentList;


    public CategoryFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        categoryFragmentList = new ArrayList<>();
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        for (Category c: categoryList) {
            categoryFragmentList.add(new CategoryFragment(c.getId()));
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
//        return mFragmentManager.findFragmentByTag(""+categoryList.get(position).getId());
        Log.i(TAG, "getItem: "+position);
        return categoryFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

}
