package com.monk.reader.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.monk.reader.R;
import com.monk.reader.dagger.DaggerAppComponent;
import com.monk.reader.dao.DaoSession;
import com.monk.reader.dao.ShelfBookDao;
import com.monk.reader.dao.bean.ShelfBook;
import com.monk.reader.ui.base.BaseFragment;
import com.monk.reader.utils.SharedPreferencesUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class DemoFragment extends BaseFragment {
    @BindView(R.id.btn_fragment)
    Button btnFragment;
    @BindView(R.id.tv_text)
    TextView tvText;
    @Inject
    ShelfBookDao shelfBookDao;
    @Inject
    SharedPreferencesUtils sharedPreferencesUtils;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getmApplication().getAppComponent().inject(this);
    }


    @Override
    protected int layoutId() {
        return R.layout.fragment_butterknife;
    }

    @Override
    protected void initAfter() {
    }

    private static final String TAG = "DemoFragment";
    int i = 0;
    @OnClick(R.id.btn_fragment)
    public void onViewClicked() {
        getmApplication().getAppComponent().inject(this);

        shelfBookDao.deleteAll();
        sharedPreferencesUtils.putInt("shelf_count",0);
    }
}
