package com.monk.reader.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.monk.reader.R;
import com.monk.reader.adapter.BookAdapter;
import com.monk.reader.retrofit2.BookApi;
import com.monk.reader.retrofit2.CategoryApi;
import com.monk.reader.retrofit2.bean.Book;
import com.monk.reader.retrofit2.bean.Category;
import com.monk.reader.ui.base.BaseActivity;
import com.monk.reader.view.CustomRadioGroup;
import com.monk.reader.view.LoadMoreRecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@Route(path = "/activity/search")
public class SearchActivity extends BaseActivity {
    private static final String TAG = "SearchActivity";

    @Inject
    CategoryApi categoryApi;
    @Inject
    BookApi bookApi;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sv_search)
    EditText svSearch;
    @BindView(R.id.rg_category)
    CustomRadioGroup rgCategory;
    @BindView(R.id.rg_length)
    CustomRadioGroup rgLength;
    @BindView(R.id.rg_order)
    CustomRadioGroup rgOrder;
    @BindView(R.id.rv_show_search)
    LoadMoreRecyclerView rvShowSearch;
//    @BindView(R.id.fl_show_book)
//    FrameLayout flShowBook;

    @Autowired(name = "categoryId")
    public long categoryId = 1;//类别
    private String search;//搜索内容
    private int length = 0;//字数
    private String order = "up_date";
    private int page = 1;
    private int limit = 10;


    private int[] lengthArr = {0,1,2,3};
    private String[] lengthTextArr = {"不限","100万字以下","100-200万字","200万字以上"};

    private String[] orderArr = {"up_date","star_avg","download_count","comment_count"};
    private String[] orderTextArr = {"入库时间","评分","下载量","评论数"};

    BookAdapter bookAdapter;
    List<Book> bookList;

    @Override
    protected int inflateLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initBefore() {
        super.initBefore();
        mApplication.getAppComponent().inject(this);

        ARouter.getInstance().inject(this);

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initAfter() {
        super.initAfter();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvShowSearch.setLayoutManager(new LinearLayoutManager(this));
        rvShowSearch.setmLoadMoreListener(()->{
            page++;
            bookApi.getSelectedBookList(page, limit, search, categoryId, length, order)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        Log.i(TAG, "initAfter: loadMoreListener" + result);
                        int l = bookList.size();
                        if(bookList.addAll(result.getData())){
                           bookAdapter.notifyItemInserted(l-1);
                           bookAdapter.notifyDataSetChanged();
                        }
                    }, Throwable::printStackTrace);
        });

        svSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search = s.toString();
                page = 1;
                update();
            }
        });

        initCategoryRadioGroup();
        initLengthRadioGroup();
        initOrderRadioGroup();


        update();
    }

    private void initOrderRadioGroup() {
        setSpacing(rgOrder,12,8);
        for (int i = 0; i < orderArr.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            radioButton.setTag(orderArr[i]);
            radioButton.setText(orderTextArr[i]);
            if(i==0) radioButton.setChecked(true);
            rgOrder.addView(radioButton);
        }
        rgOrder.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            order = (String) radioButton.getTag();
            page = 1;
            update();
        });
    }

    private void initLengthRadioGroup() {
        setSpacing(rgLength,12,8);
        for (int i = 0; i < lengthArr.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            radioButton.setTag(lengthArr[i]);
            radioButton.setText(lengthTextArr[i]);
            if(i==0) radioButton.setChecked(true);
            rgLength.addView(radioButton);
        }
        rgLength.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            length = (Integer) radioButton.getTag();
            page = 1;
            update();
        });
    }

    @SuppressLint("CheckResult")
    private void initCategoryRadioGroup() {
        categoryApi.getSubCategoryById(1L)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter: " + result);
                    List<Category> categoryList = result.getData();
                    setSpacing(rgCategory,12,8);
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setId(-1);
                    radioButton.setTag(1L);
                    radioButton.setText("不限");
                    if(1 == categoryId) radioButton.setChecked(true);
                    rgCategory.addView(radioButton);
                    for (int i = 0; i < categoryList.size(); i++) {
                        Category c = categoryList.get(i);
                        radioButton = new RadioButton(this);
                        radioButton.setId(i);
                        radioButton.setTag(c.getId());
                        radioButton.setText(c.getName());
                        if(c.getId() == categoryId) radioButton.setChecked(true);
                        rgCategory.addView(radioButton);
                    }
                    rgCategory.setOnCheckedChangeListener((group, checkedId) -> {
                        RadioButton checked = group.findViewById(checkedId);
                        categoryId = (Long) checked.getTag();
                        page = 1;
                        update();
                    });
                }, Throwable::printStackTrace);
    }

    @SuppressLint("CheckResult")
    private void update() {
        bookApi.getSelectedBookList(page, limit, search, categoryId, length, order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter: " + result);
                    bookList = result.getData();
                    bookAdapter = new BookAdapter(bookList);
                    rvShowSearch.setAdapter(bookAdapter);
                }, Throwable::printStackTrace);
    }
    private void setSpacing(CustomRadioGroup cg,int widthdp,int heightdp){
        cg.setHorizontalSpacing(widthdp);
        cg.setVerticalSpacing(heightdp);

    }
}
