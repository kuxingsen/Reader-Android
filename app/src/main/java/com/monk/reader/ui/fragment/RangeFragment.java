package com.monk.reader.ui.fragment;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.monk.reader.R;
import com.monk.reader.adapter.BookAdapter;
import com.monk.reader.retrofit2.BookApi;
import com.monk.reader.retrofit2.bean.Book;
import com.monk.reader.ui.base.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RangeFragment extends BaseFragment {
    private static final String TAG = "RangeFragment";

    @Inject
    BookApi bookApi;

    @BindView(R.id.rv_range)
    RecyclerView rvShowRange;

    private long rangeId;
    private String rangeDuration;

    public RangeFragment(long rangeId, String rangeDuration){
        this.rangeId = rangeId;
        this.rangeDuration = rangeDuration;
    }


    @Override
    protected void initBefore() {
        super.initBefore();
        getmApplication().getAppComponent().inject(this);
    }
    @Override
    protected int layoutId() {
        return R.layout.fragment_range;
    }
    @SuppressLint("CheckResult")
    @Override
    protected void initAfter() {
        bookApi.getRangeBookList(rangeId,rangeDuration)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter: " + result);
                    List<Book> data = result.getData();
                    rvShowRange.setLayoutManager(new LinearLayoutManager(getContext()));
                    BookAdapter bookAdapter = new BookAdapter(data);
                    bookAdapter.setShowRangeContentListener((tvRange, book) -> tvRange.setText(book.getRangeValue()));
                    rvShowRange.setAdapter(bookAdapter);
                }, Throwable::printStackTrace);

    }
}
