package com.monk.reader.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.monk.reader.BaseApplication;
import com.monk.reader.R;
import com.monk.reader.constant.Config;
import com.monk.reader.dagger.DaggerAppComponent;
import com.monk.reader.dao.ShelfBookDao;
import com.monk.reader.dao.bean.ReadInfo;
import com.monk.reader.dao.bean.ShelfBook;
import com.monk.reader.dialog.PageModeDialog;
import com.monk.reader.dialog.SettingDialog;
import com.monk.reader.eventbus.ReadBookEvent;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.retrofit2.BookApi;
import com.monk.reader.retrofit2.BookCatalogueApi;
import com.monk.reader.retrofit2.bean.Book;
import com.monk.reader.ui.base.BaseActivity;
import com.monk.reader.utils.BrightnessUtil;
import com.monk.reader.utils.PageFactory;
import com.monk.reader.view.PageWidget;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/activity/reader")
public class ReaderActivity extends BaseActivity {
    private static final String TAG = "ReaderActivity";

    public final static String EXTRA_BOOK_ID = "bookId";
    private final static int MESSAGE_CHANGEPROGRESS = 1;
    public final static int CODE_REQUEST = 0x93;//yuedu

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_directory)
    TextView tvDirectory;
    @BindView(R.id.tv_dayornight)
    TextView tvDayornight;
    @BindView(R.id.tv_pagemode)
    TextView tvPagemode;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.layout_bottom)
    ConstraintLayout layoutBottom;
    @BindView(R.id.book_page)
    PageWidget bookPage;

    @Inject
    public ShelfBookDao shelfBookDao;
    @Inject
    public BookApi bookApi;
    @Inject
    public BookCatalogueApi bookCatalogueApi;


    @Autowired(name = EXTRA_BOOK_ID)
    public long bookId;
    @Autowired(name = "from")
    public String from="local";
    @Autowired(name = "begin")
    public long begin=0L;
    @Autowired(name = "inShelf")
    public boolean inShelf=false;


    private SettingDialog mSettingDialog;
    private PageModeDialog mPageModeDialog;
    private Config config;
    private PageFactory pageFactory;
    // popwindow是否显示
    private Boolean isShow = false;

    private Boolean mDayOrNight;
    private ShelfBook shelfBook;

    private long startTime;

    @Override
    protected void initBefore() {
        super.initBefore();
        getmApplication().getAppComponent().inject(this);
        ARouter.getInstance().inject(this);
        startTime = System.currentTimeMillis();
    }
    @Override
    public int inflateLayout() {
        return R.layout.activity_read;
    }

    @Override
    protected void initAfter() {
        super.initAfter();
        initData();
        initListener();
    }

    @SuppressLint("CheckResult")
    protected void initData() {

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        config = Config.getInstance();
        pageFactory = PageFactory.getInstance();
        pageFactory.hideBottomUIMenu(this);

        mSettingDialog = new SettingDialog(this);
        mPageModeDialog = new PageModeDialog(this);

        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //隐藏
        hideSystemUI();

        Log.i(TAG, "initData: bookId:"+bookId+"   from:"+from);
        bookPage.setPageMode(config.getPageMode());
        pageFactory.setPageWidget(bookPage);

        if ("local".equals(from)) {
            shelfBook = shelfBookDao.load(bookId);

            try {
                pageFactory.openBook(shelfBook);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
            }
        }
        if("network".equals(from)){

            if(inShelf){
                List<ShelfBook> list = shelfBookDao.queryRaw("where path=?",""+bookId);
                if(list != null && list.size()>0){
                    Log.i(TAG, "initData: 在书架上的网络书");
                    shelfBook = list.get(0);
                    if(begin != 0L) shelfBook.setBegin(begin);
                    try {
                        pageFactory.openBook(shelfBook);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                bookApi.getBook(bookId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            Log.i(TAG, "initAfter: " + result);
                            List<Book> bookList = result.getData();
                            if (bookList == null || bookList.size() == 0) return;
                            Book book = bookList.get(0);
                            shelfBook = new ShelfBook();
                            shelfBook.setBegin(begin);
                            shelfBook.setName(book.getName());
                            shelfBook.setFrom("network");
                            shelfBook.setPath(bookId + "");
                            shelfBook.setCharset(book.getCharSet());
                            shelfBook.setBookLen(book.getSize());
                            shelfBook.setPicture(book.getPicture());

                            Log.i(TAG, "initData: " + shelfBook);

                            try {
                                pageFactory.openBook(shelfBook);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
                            }

                        }, Throwable::printStackTrace);
            }
        }
        initDayOrNight();

    }

    protected void initListener() {
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float pro;

            // 触发操作，拖动
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pro = (float) (progress / 10000.0);
                setProgress(pro);
            }

            // 表示进度条刚开始拖动，开始拖动时候触发的操作
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                showProgress();
            }

            // 停止拖动时候
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pageFactory.changeProgress(pro);
            }
        });

        mPageModeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mPageModeDialog.setPageModeListener(new PageModeDialog.PageModeListener() {
            @Override
            public void changePageMode(int pageMode) {
                bookPage.setPageMode(pageMode);
            }
        });

        mSettingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mSettingDialog.setSettingListener(new SettingDialog.SettingListener() {
            @Override
            public void changeSystemBright(Boolean isSystem, float brightness) {
                if (!isSystem) {
                    BrightnessUtil.setBrightness(ReaderActivity.this, brightness);
                } else {
                    int bh = BrightnessUtil.getScreenBrightness(ReaderActivity.this);
                    BrightnessUtil.setBrightness(ReaderActivity.this, bh);
                }
            }

            @Override
            public void changeFontSize(int fontSize) {
                pageFactory.changeFontSize(fontSize);
            }

            @Override
            public void changeBookBg(int type) {
                pageFactory.changeBookBg(type);
            }
        });


        pageFactory.setPageEvent(new PageFactory.PageEvent() {
            @Override
            public void changeProgress(float progress) {
                Message message = new Message();
                message.what = MESSAGE_CHANGEPROGRESS;
                message.obj = progress;
                mHandler.sendMessage(message);
            }
        });

        bookPage.setTouchListener(new PageWidget.TouchListener() {
            @Override
            public void center() {
                if (isShow) {
                    hideReadSetting();
                } else {
                    showReadSetting();
                }
            }

            @Override
            public Boolean prePage() {
                if (isShow) {
                    hideReadSetting();
                    return false;
                }

                pageFactory.prePage();
                if (pageFactory.isfirstPage()) {
                    return false;
                }

                return true;
            }

            @Override
            public Boolean nextPage() {
                if (isShow ) {
                    hideReadSetting();
                    return false;
                }

                pageFactory.nextPage();
                if (pageFactory.islastPage()) {
                    return false;
                }
                return true;
            }

            @Override
            public void cancel() {
                pageFactory.cancelPage();
            }
        });

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_CHANGEPROGRESS:
                    float progress = (float) msg.obj;
                    setSeekBarProgress(progress);
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (!isShow) {
            hideSystemUI();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageFactory.clear();
        bookPage = null;
    }

    @SuppressLint("CheckResult")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: ");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShow) {
                hideReadSetting();
                return true;
            }
            if (mSettingDialog.isShowing()) {
                mSettingDialog.hide();
                return true;
            }
            if (mPageModeDialog.isShowing()) {
                mPageModeDialog.hide();
                return true;
            }
            Log.i(TAG, "onKeyDown: "+shelfBook);
            if("network".equals(from)){
                shelfBook.setBegin(pageFactory.getBegin());
                Log.i(TAG, "onKeyDown: ?");
                //本地历史
                ReadInfo readInfo = new ReadInfo(shelfBook);
                //todo userId
                readInfo.setUserId(5L);
                long endTime = System.currentTimeMillis();
                long duration = millis2Minutes(endTime - startTime);
                readInfo.setDuration(duration);
                readInfo.setUpdateTime(endTime);
                RxBus.getDefault().post(new ReadBookEvent(readInfo));
                //服务器历史 todo
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 隐藏菜单。沉浸式阅读
     */
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    //显示书本进度
    public void showProgress() {
        if (tvProgress.getVisibility() != View.VISIBLE) {
            tvProgress.setVisibility(View.VISIBLE);
        }
    }

    //隐藏书本进度
    public void hideProgress() {
        tvProgress.setVisibility(View.GONE);
    }

    public void initDayOrNight() {
        mDayOrNight = config.getDayOrNight();
        if (mDayOrNight) {
            tvDayornight.setText(getResources().getString(R.string.read_setting_day));
        } else {
            tvDayornight.setText(getResources().getString(R.string.read_setting_night));
        }
    }

    //改变显示模式
    public void changeDayOrNight() {
        if (mDayOrNight) {
            mDayOrNight = false;
            tvDayornight.setText(getResources().getString(R.string.read_setting_night));
        } else {
            mDayOrNight = true;
            tvDayornight.setText(getResources().getString(R.string.read_setting_day));
        }
        config.setDayOrNight(mDayOrNight);
        pageFactory.setDayOrNight(mDayOrNight);
    }

    private void setProgress(float progress) {
        DecimalFormat decimalFormat = new DecimalFormat("00.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(progress * 100.0);//format 返回的是字符串
        tvProgress.setText(p + "%");
    }

    public void setSeekBarProgress(float progress) {
        sbProgress.setProgress((int) (progress * 10000));
    }

    private void showReadSetting() {
        isShow = true;

        showSystemUI();

        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
        layoutBottom.startAnimation(bottomAnim);
        toolbar.startAnimation(topAnim);
//        ll_top.startAnimation(topAnim);
        layoutBottom.setVisibility(View.VISIBLE);
//        ll_top.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
    }

    private void hideReadSetting() {
        hideProgress();
        isShow = false;
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_exit);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_exit);
        if (layoutBottom.getVisibility() == View.VISIBLE) {
            layoutBottom.startAnimation(bottomAnim);
        }
        if (toolbar.getVisibility() == View.VISIBLE) {
            toolbar.startAnimation(topAnim);
        }
        layoutBottom.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        hideSystemUI();
    }

    @OnClick({R.id.tv_pre, R.id.tv_next, R.id.tv_directory, R.id.tv_dayornight, R.id.tv_pagemode, R.id.tv_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pre:
                pageFactory.preChapter();
                break;
            case R.id.tv_next:
                pageFactory.nextChapter();
                break;
            case R.id.tv_directory:
                Log.i(TAG, "onClick: toActivity--Directory");
                Intent intent = new Intent(ReaderActivity.this, DirectoryActivity.class);
                startActivityForResult(intent,CODE_REQUEST);

                break;
            case R.id.tv_dayornight:
                changeDayOrNight();
                break;
            case R.id.tv_pagemode:
                hideReadSetting();
                mPageModeDialog.show();
                break;
            case R.id.tv_setting:
                hideReadSetting();
                mSettingDialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==CODE_REQUEST && resultCode==DirectoryActivity.CODE_RESULT && data!=null){
            long start = data.getLongExtra("start", 0L);
            Log.i(TAG, "onActivityResult: "+start);
            pageFactory.changeChapter(start);
            if (isShow) {
                hideReadSetting();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private long millis2Minutes(long duration) {
        return  duration/ (1000 * 60);
    }
}
