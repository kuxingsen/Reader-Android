package com.monk.reader.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
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
import com.monk.reader.dao.DownloadInfoDao;
import com.monk.reader.dao.ShelfBookDao;
import com.monk.reader.dao.bean.BookCatalogue;
import com.monk.reader.dao.bean.ShelfBook;
import com.monk.reader.download.DownloadInfo;
import com.monk.reader.download.DownloadManager;
import com.monk.reader.eventbus.AddToShelfEvent;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.retrofit2.BookApi;
import com.monk.reader.retrofit2.BookCatalogueApi;
import com.monk.reader.retrofit2.bean.Book;
import com.monk.reader.ui.base.BaseActivity;
import com.monk.reader.utils.BookUtil;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    @Inject
    ShelfBookDao shelfBookDao;
    @Inject
    BookApi bookApi;
    @Inject
    BookCatalogueApi bookCatalogueApi;

    @Autowired(name = "bookId")
    public long bookId = 1;
    private String bookName;
    private String bookPicture;
    private long bookSize;
    private String bookCharSet;
    private Long begin=0L;
    private boolean inShelf=false;
    private String bookAuthor;
    private int mDownloadState;
    private String bookPath;
    private String categoryName;
    private CatalogueAdapter catalogueAdapter;

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

        toolBar.setTitle("");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isInShelf();
        hasDownload();
        bookApi.getBook(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter:" + result);
                    List<Book> data = result.getData();
                    if (data == null || data.size() == 0) return;
                    Book book = data.get(0);
                    bookName = book.getName();
                    bookPicture = book.getPicture().replaceAll("\\\\","/");
                    bookSize = book.getSize();
                    bookCharSet = book.getCharSet();
                    bookAuthor = book.getAuthor();
                    bookPath = book.getPath();
                    String introduction = book.getIntroduction();
                    String upDate = book.getUpDate();
                    categoryName = book.getCategoryName();

                    tvTitle.setText(bookName);
                    tvName.setText(bookName);
                    SpannableString authorString = new SpannableString(bookAuthor + " 著");
                    authorString.setSpan(new ForegroundColorSpan(Color.parseColor("#ee3f3f")), 0, authorString.length() - 1,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tvAuthor.setText(authorString);
                    tvIntro.setText(introduction);
                    tvLength.setText(bookSize+" 字");
                    tvCategory.setText(categoryName);
                    Glide.with(this).load("http://192.168.43.14:8080"+bookPicture).into(ivPicture);
                    tvUpDate.setText("上传于 " + upDate);

                }, Throwable::printStackTrace);

        //获取目录
        bookCatalogueApi.getCatalogueByBookId(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "initAfter: " + result);
                    List<BookCatalogue> data = result.getData();
                    catalogueAdapter = new CatalogueAdapter(data,inShelf);
                    rvShowCatalogue.setLayoutManager(new LinearLayoutManager(this));
                    rvShowCatalogue.setAdapter(catalogueAdapter);

                }, Throwable::printStackTrace);
  }

    private void hasDownload() {

        List<DownloadInfo> infos = DownloadInfo.DAO.queryBuilder().where(DownloadInfoDao.Properties.BookId.eq(bookId)).list();
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
    }

    private void isInShelf() {
        List<ShelfBook> queryRaw = shelfBookDao.queryRaw("where path=?", "" + bookId);
        if(null != queryRaw && 0<queryRaw.size()){
            info_add_to_shelf_tv.setEnabled(false);
            info_add_to_shelf_tv.setText("已加入书架");
            begin = queryRaw.get(0).getBegin();
            inShelf = true;
        }
    }


    @OnClick(R.id.tv_start_read)
    public void tv_start_read_click(View view) {
        switch (view.getId()) {
            case R.id.tv_start_read:

                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                Bundle bundle = new Bundle();
                bundle.putLong(ReaderActivity.EXTRA_BOOK_ID, bookId);
                bundle.putString("from", "network");
                bundle.putLong("begin",begin);
                bundle.putBoolean("inShelf",inShelf);
                ARouter.getInstance().build("/activity/reader")
                        .with(bundle)
                        .navigation();

                break;
        }

    }

    @OnClick(R.id.info_add_to_shelf_tv)
    public void info_add_to_shelf_tv_click(View view) {
        inShelf = true;
        ShelfBook shelfBook = new ShelfBook();
        shelfBook.setPath(bookId+"");
        shelfBook.setFrom("network");
        shelfBook.setBookLen(bookSize);
        shelfBook.setCharset(bookCharSet);
        shelfBook.setName(bookName);
        shelfBook.setBegin(0L);//todo
        shelfBook.setPicture(bookPicture);
        RxBus.getDefault().post(new AddToShelfEvent(shelfBook));
        Log.i(TAG, "info_add_to_shelf_tv_click: "+shelfBook);
        info_add_to_shelf_tv.setEnabled(false);
        info_add_to_shelf_tv.setText("已加入书架");
        inShelf = true;
        catalogueAdapter.setInShelf(inShelf);
    }

    @OnClick(R.id.tv_book_info_download)
    public void tv_book_info_download_click(View v) {
        if (mDownloadState == DownloadInfo.FINISH
                || mDownloadState == DownloadInfo.DOWNLOADING
                || mDownloadState == DownloadInfo.WAITING)
            return;
        DownloadInfo book = new DownloadInfo();
        book.setBookId(""+bookId);
        book.setName(bookName);
        book.setAuthor(bookAuthor);
        book.setPicture(bookPicture);
        book.setCategory(categoryName);
        book.setUrl("http://192.168.43.14:8080/file"+bookPath);
        DownloadManager.download(book);
        tv_book_info_download.setText("下载中...");
    }
}
