package com.monk.reader;

import android.app.Application;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.monk.reader.constant.Config;
import com.monk.reader.dagger.AppComponent;
import com.monk.reader.dagger.AppModule;
import com.monk.reader.dagger.DaggerAppComponent;
import com.monk.reader.utils.AppUtils;
import com.monk.reader.utils.PageFactory;

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: init");
        //dagger2注入配置，主要放单例对象
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(getApplicationContext())).build();

        //ARouter路由跳转配置，主要用于跳转activity
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);

        Config.createConfig(this);
        PageFactory.createPageFactory(this);


        AppUtils.init(getApplicationContext());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
