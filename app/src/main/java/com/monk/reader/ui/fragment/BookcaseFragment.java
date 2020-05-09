package com.monk.reader.ui.fragment;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.monk.reader.R;
import com.monk.reader.adapter.CategoryFragmentPagerAdapter;
import com.monk.reader.retrofit2.CategoryApi;
import com.monk.reader.retrofit2.bean.Category;
import com.monk.reader.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class BookcaseFragment extends BaseFragment {
    private static final String TAG = "BookcaseFragment";

    @Inject
    CategoryApi categoryApi;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected int layoutId() {
        return R.layout.fragment_bookcase;
    }


    @Override
    protected void initBefore() {
        Log.i(TAG, "initBefore: ");
        super.initBefore();

        getmApplication().getAppComponent().inject(this);

        ARouter.getInstance().inject(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initAfter() {

        Log.i(TAG, "initAfter: ");
        // 删除窗口背景
//        mActivity.getWindow().setBackgroundDrawable(null);

        long selectId = arguments.getLong("selectId");
        CategoryFragmentPagerAdapter categoryFragmentPagerAdapter =
                new CategoryFragmentPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);


        categoryApi.getSubCategoryById(selectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter: " + result);
                    List<Category> categoryList =result.getData();
                    categoryFragmentPagerAdapter.setCategoryList(categoryList);
                    viewPager.setAdapter(categoryFragmentPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                    Category c;
                    for (int i = 0;i < categoryList.size();i++) {
                        c = categoryList.get(i);
                        tabLayout.getTabAt(i).setText(c.getName()).setTag(c.getId());
                    }
                }, Throwable::printStackTrace);



    }

}
