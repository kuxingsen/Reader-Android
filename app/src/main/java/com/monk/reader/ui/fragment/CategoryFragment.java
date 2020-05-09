package com.monk.reader.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.monk.reader.R;
import com.monk.reader.adapter.BannerFragmentPagerAdapter;
import com.monk.reader.adapter.BookAdapter;
import com.monk.reader.adapter.CategoryAdapter;
import com.monk.reader.adapter.RangeAdapter;
import com.monk.reader.adapter.RangeFragmentPagerAdapter;
import com.monk.reader.retrofit2.BannerApi;
import com.monk.reader.retrofit2.BookApi;
import com.monk.reader.retrofit2.CategoryApi;
import com.monk.reader.retrofit2.RangeApi;
import com.monk.reader.retrofit2.bean.Banner;
import com.monk.reader.retrofit2.bean.Book;
import com.monk.reader.retrofit2.bean.Category;
import com.monk.reader.retrofit2.bean.Range;
import com.monk.reader.ui.activity.SearchActivity;
import com.monk.reader.ui.base.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class CategoryFragment extends BaseFragment {
    private static final String TAG = "CategoryFragment";
    @BindView(R.id.view_pager_banner)
    com.monk.reader.view.Banner viewPagerBanner;

    @Inject
    BannerApi bannerApi;
    @Inject
    CategoryApi categoryApi;
    @Inject
    BookApi bookApi;
    @Inject
    RangeApi rangeApi;


    @BindView(R.id.rv_show_category)
    RecyclerView rvShowCategory;
    @BindView(R.id.sv_search)
    EditText svSearch;
    @BindView(R.id.rv_show_new)
    RecyclerView rvShowNew;
    @BindView(R.id.tv_range_name)
    TextView tvRangeName;
    @BindView(R.id.tl_range)
    TabLayout tlRange;
    @BindView(R.id.vp_range)
    ViewPager vpRange;

    private long categoryId;

    public CategoryFragment(long id) {
        this.categoryId = id;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initBefore() {
        super.initBefore();
        getmApplication().getAppComponent().inject(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initAfter() {
        Log.i(TAG, "initAfter: ");
        svSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                Intent intent = new Intent(getContext(), SearchActivity.class);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), svSearch, "simple_search_name");
                ActivityCompat.startActivity(getActivity(), intent, compat.toBundle());
            }
        });
        bannerApi.getBannerByCategoryId(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter: getBannerByCategoryId " + result);
                    BannerFragmentPagerAdapter bannerFragmentPagerAdapter =
                            new BannerFragmentPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                    List<Banner> data = result.getData();
                    bannerFragmentPagerAdapter.setBannerList(data);
                    viewPagerBanner.setAdapter(bannerFragmentPagerAdapter);
                }, Throwable::printStackTrace);
        categoryApi.getSubCategoryById(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter: getSubCategoryById " + result);
                    List<Category> data = result.getData();
//                    rvShowCategory.setLayoutManager(new GridLayoutManager(getContext(),2, RecyclerView.HORIZONTAL,false));
                    rvShowCategory.setLayoutManager(new GridLayoutManager(getContext(), 4));
                    rvShowCategory.setAdapter(new CategoryAdapter(data));

                }, Throwable::printStackTrace);
        bookApi.getNewBookList(7,categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter: getNewBookList " + result);
                    List<Book> data = result.getData();
                    rvShowNew.setLayoutManager(new LinearLayoutManager(getContext()));

                    BookAdapter adapter = new BookAdapter(data);
                    rvShowNew.setAdapter(adapter);

                }, Throwable::printStackTrace);
        rangeApi.getRangeList(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter: getRangeList " + result);
                    List<Range> data = result.getData();
                    Range range = data.get(0);

                    tvRangeName.setText(range.getName());

                    RangeFragmentPagerAdapter rangeFragmentPagerAdapter =
                            new RangeFragmentPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                    rangeFragmentPagerAdapter.setRangeId(range.getId());

                    vpRange.setAdapter(rangeFragmentPagerAdapter);
                    tlRange.setupWithViewPager(vpRange);
                    TabLayout.Tab tab = tlRange.getTabAt(0);
                    tab.setText("周榜");
                    tab = tlRange.getTabAt(1);
                    tab.setText("月榜");
                    tab = tlRange.getTabAt(2);
                    tab.setText("总榜");
                }, Throwable::printStackTrace);

    }
}
