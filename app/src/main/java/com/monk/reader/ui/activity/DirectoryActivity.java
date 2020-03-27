package com.monk.reader.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.monk.reader.R;
import com.monk.reader.dao.bean.BookCatalogue;
import com.monk.reader.ui.base.BaseActivity;
import com.monk.reader.ui.fragment.CatalogueFragment;
import com.monk.reader.utils.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class DirectoryActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    public static final int CODE_RESULT = 0x6858;//mulu
    private List<BookCatalogue> directoryList;

    @Override
    protected int inflateLayout() {
        return R.layout.activity_directory;
    }

    @Override
    protected void initAfter() {
        super.initAfter();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        directoryList = PageFactory.getInstance().getDirectoryList();

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tab0 = tabLayout.getTabAt(0);
        tab0.setText("目录");

        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        tab1.setText("书签");
    }
    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            CatalogueFragment listFragment = new CatalogueFragment();

            ArrayAdapter arrayAdapter = null;
            switch (position){
                case 0:
                    listFragment.setOnItemClickCallBack((l,v,p,id)->{
                        Intent intent = new Intent();
                        intent.putExtra("start", directoryList.get(p).getBookCatalogueStartPos());
                        DirectoryActivity.this.setResult(CODE_RESULT,intent);
                        DirectoryActivity.this.finish();
                    });
                    List<String> directoryNameList = new ArrayList<>();
                    for(BookCatalogue catalogue:directoryList){
                        directoryNameList.add(catalogue.getBookCatalogue());
                    }
                    arrayAdapter = new ArrayAdapter<>(DirectoryActivity.this, android.R.layout.simple_list_item_1,directoryNameList);
                    break;
                case 1:
                    directoryNameList = new ArrayList<>();
                    for(BookCatalogue catalogue:directoryList){
                        directoryNameList.add(catalogue.getBookCatalogue());
                    }
                    arrayAdapter = new ArrayAdapter<>(DirectoryActivity.this, android.R.layout.simple_list_item_1,directoryNameList);

                    break;
            }
            listFragment.setListAdapter(arrayAdapter);
            return listFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
