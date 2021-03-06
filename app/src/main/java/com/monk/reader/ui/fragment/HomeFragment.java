package com.monk.reader.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.monk.reader.R;
import com.monk.reader.adapter.ShelfAdapter;
import com.monk.reader.dao.DaoSession;
import com.monk.reader.dao.bean.ShelfBook;
import com.monk.reader.eventbus.AddToShelfEvent;
import com.monk.reader.eventbus.HideShelfDeleteButton;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.eventbus.RxEvent;
import com.monk.reader.eventbus.ShowActionBarCancel;
import com.monk.reader.ui.activity.ReaderActivity;
import com.monk.reader.ui.base.BaseFragment;
import com.monk.reader.utils.SharedPreferencesUtils;
import com.monk.reader.view.DragGridView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.bookShelf)
    DragGridView bookShelf;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    DaoSession daoSession;
    @Inject
    SharedPreferencesUtils sharedPreferencesUtils;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    private List<ShelfBook> shelfBookList;

    private ShelfAdapter adapter;

    private int itemPosition;


    @Override
    protected int layoutId() {
        return R.layout.fragment_shelf;
    }

    @Override
    protected void initBefore() {
        super.initBefore();

        getmApplication().getAppComponent().inject(this);

        ARouter.getInstance().inject(this);
    }

    @Override
    protected void initAfter() {
        Log.i(TAG, "initAfter: ");

//        daoSession.deleteAll(ShelfBook.class);
//        sharedPreferencesUtils.putInt("shelf_count",0);
        shelfBookList = daoSession.queryBuilder(ShelfBook.class).orderRaw("position").list();
        adapter = new ShelfAdapter(mActivity, shelfBookList);

        fab.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putString("filenameFilter", "txt");
            HomeFragment.this.toActivity("/activity/file/chooser", bundle);
//            ARouter.getInstance().build("/activity/file/chooser")
//                    .withString("filenameFilter","txt")
//                    .navigation();
        });

        tvCancel.setOnClickListener(v->{
            RxBus.getDefault().post(new HideShelfDeleteButton());
            tvCancel.setVisibility(View.GONE);
        });

        bookShelf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (shelfBookList.size() > position) {
                    itemPosition = position;

                    adapter.setItemToFirst(itemPosition);

                    final ShelfBook shelfBook = HomeFragment.this.shelfBookList.get(0);
                    final String path = shelfBook.getPath();
                    String from = shelfBook.getFrom();
                    Log.i(TAG, "onItemClick: " + shelfBook);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("inShelf",true);
                    bundle.putString("from", from);
                    bundle.putLong("begin",shelfBook.getBegin());
                    if("local".equals(from)){
                        File file = new File(path);
                        if (!file.exists()) {
                            new AlertDialog.Builder(mActivity)
                                    .setTitle(mActivity.getString(R.string.app_name))
                                    .setMessage(path + "文件不存在,是否删除该书本？")
                                    .setPositiveButton("删除", (dialog, which) -> adapter.removeItem(0))
                                    .setCancelable(true)
                                    .show();
                            return;
                        }
                        bundle.putLong(ReaderActivity.EXTRA_BOOK_ID, shelfBook.getId());
                    }else {
                        bundle.putLong(ReaderActivity.EXTRA_BOOK_ID, Long.valueOf(shelfBook.getPath()));
                    }
                    mActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    ARouter.getInstance().build("/activity/reader")
                            .with(bundle)
                            .navigation();
                }
            }
        });
    }


    @Override
    public void onStart() {
        Log.i(TAG, "onStart: ");
        super.onStart();
        bookShelf.setAdapter(adapter);
        bookShelf.setIsShowDeleteButton(false);
    }

    @Override
    protected void onEventMainThread(RxEvent rxEvent) {
        super.onEventMainThread(rxEvent);
        Log.i(TAG, "onEventMainThread: ???");
        if (rxEvent instanceof AddToShelfEvent) {
            ShelfBook newBook = ((AddToShelfEvent) rxEvent).getNewBook();
            Log.i(TAG, "onEventMainThread: newbook"+newBook);
            adapter.addNewBook(newBook);
        }
        if (rxEvent instanceof HideShelfDeleteButton) {
            bookShelf.setIsShowDeleteButton(false);
        }
        if (rxEvent instanceof ShowActionBarCancel) {
            tvCancel.setVisibility(View.VISIBLE);
        }
    }

}
