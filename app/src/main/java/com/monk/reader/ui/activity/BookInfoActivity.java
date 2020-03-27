package com.monk.reader.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.monk.reader.R;
import com.monk.reader.adapter.CatalogueAdapter;
import com.monk.reader.dao.ShelfBookDao;
import com.monk.reader.dao.bean.BookCatalogue;
import com.monk.reader.dao.model.StoryArticle;
import com.monk.reader.retrofit2.BookApi;
import com.monk.reader.retrofit2.BookCatalogueApi;
import com.monk.reader.retrofit2.bean.Book;
import com.monk.reader.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heyao on 2017/8/1.
 */
@Route(path = "/activity/info")
public class BookInfoActivity extends BaseActivity {
    private static final String TAG = "BookInfoActivity";

    @BindView(R.id.tv_book_info_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_author)
    TextView tvAuthor;

    @BindView(R.id.tv_book_info_download)
    TextView tv_book_info_download;

    @BindView(R.id.info_add_to_shelf_tv)
    TextView info_add_to_shelf_tv;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.tv_up_date)
    TextView tvUpDate;
    @BindView(R.id.rv_show_catalogue)
    RecyclerView rvShowCatalogue;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    private StoryArticle mStoryArticle;
    private int mDownloadState;
    @Inject
    ShelfBookDao shelfBookDao;
    @Inject
    BookApi bookApi;
    @Inject
    BookCatalogueApi bookCatalogueApi;

    @Autowired(name = "bookId")
    public long bookId = 1;

    @Override
    protected int inflateLayout() {
        return R.layout.activity_book_info;
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

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookApi.getBook(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter:" + result);
                    List<Book> data = result.getData();
                    if (data == null || data.size() == 0) return;
                    Book book = data.get(0);
                    String name = book.getName();
                    String author = book.getAuthor();
                    String introduction = book.getIntroduction();
                    String picture = book.getPicture();
                    String upDate = book.getUpDate();
                    int length = book.getLength();
                    String categoryName = book.getCategoryName();

                    tvTitle.setText(name);
                    tvName.setText(name);
                    SpannableString authorString = new SpannableString(author + " 著");
                    authorString.setSpan(new ForegroundColorSpan(Color.parseColor("#ee3f3f")), 0, authorString.length() - 1,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tvAuthor.setText(authorString);
                    tvIntro.setText(introduction);
                    tvLength.setText(length+" 万字");
                    tvCategory.setText(categoryName);
                    Glide.with(this).load(picture).into(ivPicture);
                    tvUpDate.setText("上传于 " + upDate);

                }, Throwable::printStackTrace);

        //获取目录
        bookCatalogueApi.getCatalogueByBookId(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter: " + result);
                    List<BookCatalogue> data = result.getData();
                    CatalogueAdapter catalogueAdapter = new CatalogueAdapter(data);
                    rvShowCatalogue.setLayoutManager(new LinearLayoutManager(this));
                    rvShowCatalogue.setAdapter(catalogueAdapter);

                }, Throwable::printStackTrace);
/*

        List<DownloadInfo> infos = DownloadInfo.DAO.queryBuilder().where(DownloadInfoDao.Properties.BookId.eq(mStoryArticle.getId())).list();
        if (infos != null && infos.size() > 0) {
            DownloadInfo info = infos.get(0);
            mDownloadState = info.getState();
            if (mDownloadState == DownloadInfo.FINISH) {
                tv_book_info_download.setText("已下载");
            } else if (mDownloadState == DownloadInfo.DOWNLOADING || mDownloadState == DownloadInfo.WAITING) {
                tv_book_info_download.setText("下载中...");
            } else {
                tv_book_info_download.setText("下载");
            }
        } else {
            tv_book_info_download.setText("下载");
        }
*/
/*
        List<StoryArticle> list = mDao.queryBuilder().where(StoryArticleDao.Properties.Id.eq(mStoryArticle.getId())).list();
        if (list != null && list.size() > 0) {
            info_add_to_shelf_tv.setEnabled(false);
            info_add_to_shelf_tv.setText("已加入书架");
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
/*
    @OnClick(R.id.tv_start_read)
    public void tv_start_read_click(View view) {
        switch (view.getId()) {
            case R.id.tv_start_read:
                int readCount = SharedPreferencesUtil.getInstance().getInt(Constant.BOOK_SHELF_SORT_KEY, 0) + 1;
                SharedPreferencesUtil.getInstance().putInt(Constant.BOOK_SHELF_SORT_KEY, readCount);
                mStoryArticle.setReadCount(readCount);

//                RxBus.getDefault().post(new AddToShelfEvent());
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.BOOK_INFO, mStoryArticle);
                toActivity(ReadActivity1.class, bundle);
                break;
        }
    }

    @OnClick(R.id.info_add_to_shelf_tv)
    public void info_add_to_shelf_tv_click(View view) {
        int readCount = SharedPreferencesUtil.getInstance().getInt(Constant.BOOK_SHELF_SORT_KEY, 0) + 1;
        SharedPreferencesUtil.getInstance().putInt(Constant.BOOK_SHELF_SORT_KEY, readCount);
        mStoryArticle.setReadCount(readCount);
        mDao.insertOrReplace(mStoryArticle);
        RxBus.getDefault().post(new AddToShelfEvent());
        info_add_to_shelf_tv.setEnabled(false);
        info_add_to_shelf_tv.setText("已加入书架");
    }

    @OnClick(R.id.tv_book_info_download)
    public void tv_book_info_download_click(View v) {
        if (mStoryArticle == null) return;
        if (mDownloadState == DownloadInfo.FINISH
                || mDownloadState == DownloadInfo.DOWNLOADING
                || mDownloadState == DownloadInfo.WAITING)
            return;
        DownloadInfo book = new DownloadInfo();
        book.setBookId(mStoryArticle.getId());
        book.setName(mStoryArticle.getArtName());
        book.setAuthor(mStoryArticle.getArtAuth());
        book.setStatus(mStoryArticle.getArtStat());
        book.setUrl("http://down.hunhun520.com/18/18109.txt");
        DownloadManager.download(book);
        tv_book_info_download.setText("下载中...");
    }*/
}
