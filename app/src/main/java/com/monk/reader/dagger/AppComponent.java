package com.monk.reader.dagger;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.monk.reader.AppManager;
import com.monk.reader.BaseApplication;
import com.monk.reader.adapter.RangeAdapter;
import com.monk.reader.adapter.ShelfAdapter;
import com.monk.reader.dao.DaoSession;
import com.monk.reader.dao.ShelfBookDao;
import com.monk.reader.download.DownloadTask;
import com.monk.reader.retrofit2.CategoryApi;
import com.monk.reader.retrofit2.UserApi;
import com.monk.reader.ui.activity.BookInfoActivity;
import com.monk.reader.ui.activity.FileChooserActivity;
import com.monk.reader.ui.activity.HistoryActivity;
import com.monk.reader.ui.activity.MainActivity;
import com.monk.reader.ui.activity.ReaderActivity;
import com.monk.reader.ui.activity.SearchActivity;
import com.monk.reader.ui.base.BaseActivity;
import com.monk.reader.ui.base.BaseFragment;
import com.monk.reader.ui.fragment.BookcaseFragment;
import com.monk.reader.ui.fragment.CategoryFragment;
import com.monk.reader.ui.fragment.DemoFragment;
import com.monk.reader.ui.fragment.FileFragment;
import com.monk.reader.ui.fragment.HomeFragment;
import com.monk.reader.ui.fragment.RangeFragment;
import com.monk.reader.utils.BookUtil;
import com.monk.reader.utils.PageFactory;
import com.monk.reader.utils.SharedPreferencesUtils;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(BaseApplication baseApplication);

    void inject(BaseActivity baseActivity);
    void inject(MainActivity mainActivity);
    void inject(FileChooserActivity fileChooserActivity);
    void inject(ReaderActivity readerActivity);
    void inject(SearchActivity searchActivity);
    void inject(BookInfoActivity bookInfoActivity);
    void inject(HistoryActivity historyActivity);

    void inject(ShelfAdapter shelfAdapter);

    void inject(BaseFragment baseFragment);
    void inject(HomeFragment homeFragment);
    void inject(DemoFragment demoFragment);
    void inject(FileFragment fileFragment);
    void inject(BookcaseFragment bookcaseFragment);
    void inject(CategoryFragment categoryFragment);
    void inject(RangeFragment rangeFragment);

    void inject(PageFactory pageFactory);
    void inject(BookUtil bookUtil);

    void inject(DownloadTask downloadTask);
}
