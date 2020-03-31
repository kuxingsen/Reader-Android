package com.monk.reader.dagger;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.monk.reader.AppManager;
import com.monk.reader.dao.DaoMaster;
import com.monk.reader.dao.DaoSession;
import com.monk.reader.dao.ShelfBookDao;
import com.monk.reader.retrofit2.BannerApi;
import com.monk.reader.retrofit2.BookApi;
import com.monk.reader.retrofit2.BookCacheApi;
import com.monk.reader.retrofit2.BookCatalogueApi;
import com.monk.reader.retrofit2.CategoryApi;
import com.monk.reader.retrofit2.RangeApi;
import com.monk.reader.retrofit2.UserApi;
import com.monk.reader.utils.SharedPreferencesUtils;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    private static final String TAG = "AppModule";
    private Context context;
    public AppModule(Context context){
        Log.i(TAG, "AppModule: init");
        this.context = context;
    }
    @Singleton
    @Provides
    public Context getApplicationContext(){
        return context;
    }


    @Singleton
    @Provides
    public DaoMaster getDaoMaster(){
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context,"monk_reader_db");
        return new DaoMaster(openHelper.getWritableDb());
    }

    @Singleton
    @Provides
    public DaoSession getDaoSession(DaoMaster daoMaster){
        return daoMaster.newSession();
    }

    @Singleton
    @Provides
    public ShelfBookDao getShelfBookDao(DaoSession daoSession){
        return daoSession.getShelfBookDao();
    }
    /*
     * ARouter
     */
    @Singleton
    @Provides
    public ARouter getARouter(){
        return ARouter.getInstance();
    }
    /*
     * retrofit2
     */
    @Singleton
    @Named("retrofit")
    @Provides
    public Retrofit getRetrofit(){
        return new Retrofit.Builder()
//                .baseUrl("https://www.fastmock.site/mock/585d70a5d2130ab1fbcf2b7b65fc7b37/test/")
                .baseUrl("http://192.168.43.14:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public UserApi initUserApi(@Named("retrofit") Retrofit retrofit){
        return  retrofit.create(UserApi.class);
    }

    @Singleton
    @Provides
    public CategoryApi initCategoryApi(@Named("retrofit") Retrofit retrofit){
        return  retrofit.create(CategoryApi.class);
    }
    @Singleton
    @Provides
    public BannerApi initBannerApi(@Named("retrofit") Retrofit retrofit){
        return  retrofit.create(BannerApi.class);
    }
    @Singleton
    @Provides
    public BookApi initBookApi(@Named("retrofit") Retrofit retrofit){
        return  retrofit.create(BookApi.class);
    }
    @Singleton
    @Provides
    public RangeApi initRangeApi(@Named("retrofit") Retrofit retrofit){
        return  retrofit.create(RangeApi.class);
    }
    @Singleton
    @Provides
    public BookCatalogueApi initBookCatalogueApi(@Named("retrofit") Retrofit retrofit){
        return  retrofit.create(BookCatalogueApi.class);
    }
    @Singleton
    @Provides
    public BookCacheApi initBookCacheApi(@Named("retrofit") Retrofit retrofit){
        return  retrofit.create(BookCacheApi.class);
    }
    /*
     * SharedPreferences
     */
    @Named("preferencesName")
    @Provides
    public String getPreferencesName(){
        return "monk_reader_prefs";
    }
    @Singleton
    @Provides
    public SharedPreferencesUtils getSharedPreferencesUtils(@Named("preferencesName") String prefsname){
        Log.i(TAG, "getSharedPreferencesUtils: init");
        SharedPreferencesUtils.init(context,prefsname);
        return SharedPreferencesUtils.getInstance();
    }


    @Provides
    public AppManager getAppManager(){
        return AppManager.getAppManager();
    }
}
