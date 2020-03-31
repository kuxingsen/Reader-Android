package com.monk.reader.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.monk.reader.BaseApplication;
import com.monk.reader.dagger.DaggerAppComponent;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.eventbus.RxEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    protected BaseActivity mActivity;
    protected BaseApplication mApplication;
    protected Bundle arguments;
    private Disposable mDisposable;
    @Inject
    ARouter aRouter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
        mApplication = mActivity.mApplication;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getArguments();
        registerRxBus();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initBefore();
        return inflater.inflate(layoutId(), container, false);
    }

    protected void initBefore() {
        mApplication.getAppComponent().inject(this);
    }

    protected abstract int layoutId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState,view);
        initAfter();
    }

    protected void initView(Bundle savedInstanceState,View view) {
        ButterKnife.bind(this, view);

    }

    protected abstract void initAfter();

    protected <T> void toActivityForResult(String path, Bundle bundle, int requestCode) {
        aRouter.build(path).with(bundle).navigation(mActivity,requestCode);
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
                        onEventMainThread(rxEvent);
                    }
                });
    }

    protected void onEventMainThread(RxEvent rxEvent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    public BaseApplication getmApplication() {
        return mApplication;
    }
}
