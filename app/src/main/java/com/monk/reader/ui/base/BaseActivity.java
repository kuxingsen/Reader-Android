package com.monk.reader.ui.base;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.monk.reader.AppManager;
import com.monk.reader.BaseApplication;
import com.monk.reader.dagger.DaggerAppComponent;
import com.monk.reader.eventbus.FinishEvent;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.eventbus.RxEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by heyao on 2017/7/17.
 */

public abstract class BaseActivity extends AppCompatActivity{
    private static final String TAG = "BaseActivity";

    protected BaseApplication mApplication;
    private Unbinder bind;
    private Disposable mDisposable;

    @Inject
    AppManager appManager;
    @Inject
    ARouter aRouter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBefore();
        setContentView(inflateLayout());

        initView(savedInstanceState);


        initAfter();
        registerRxBus();
    }

    protected void initBefore() {
        mApplication = (BaseApplication) getApplication();

        mApplication.getAppComponent().inject(this);
        // 尝试将inject行为留给子类实现 失败
        appManager.addActivity(this);
        requestPermission();
    }

    protected abstract int inflateLayout();

    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                appManager.finishActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initAfter(){}

    protected <T> void toActivityForResult(String path, Bundle bundle, int requestCode) {
        aRouter.build(path).with(bundle).navigation(this,requestCode);
    }
    protected <T> void toActivity(String path, Bundle bundle) {
        aRouter.build(path).with(bundle).navigation();
    }

    private void registerRxBus() {
        mDisposable = RxBus.getDefault().toObservable(RxEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxEvent -> {
                    if (rxEvent != null) {
                        if (rxEvent instanceof FinishEvent) {
                            finish();
                        } else {
                            onEventMainThread(rxEvent);
                        }
                    }
                });
    }

    protected void onEventMainThread(RxEvent rxEvent) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public BaseApplication getmApplication() {
        return mApplication;
    }

    public void setmApplication(BaseApplication mApplication) {
        this.mApplication = mApplication;
    }


    protected void requestPermission() {}
}
