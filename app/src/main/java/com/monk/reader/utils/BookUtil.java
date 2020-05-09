package com.monk.reader.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import com.monk.reader.BaseApplication;
import com.monk.reader.dao.bean.BookCatalogue;
import com.monk.reader.dao.bean.Cache;
import com.monk.reader.dao.bean.ShelfBook;
import com.monk.reader.eventbus.InitPageEvent;
import com.monk.reader.eventbus.InvalidateEvent;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.retrofit2.BookApi;
import com.monk.reader.retrofit2.BookCacheApi;
import com.monk.reader.retrofit2.BookCatalogueApi;
import com.monk.reader.retrofit2.bean.DataCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BookUtil {
    private static final String TAG = "BookUtil";
    private static final String cachedPath = Environment.getExternalStorageDirectory() + "/kuexun/reader/cache/";
    //存储的字符数
    public static final int cachedSize = 30000;
//    protected final ArrayList<WeakReference<char[]>> myArray = new ArrayList<>();

    protected List<Cache> myArray = new ArrayList<>();
    //目录
    private List<BookCatalogue> directoryList = new ArrayList<>();

    private String from;
    private String bookName;
    private String bookPath;
    private long bookLen;
    //第二页在全书中的位置?
    private long position;
    private ShelfBook shelfBook;

    @Inject
    BookApi bookApi;
    @Inject
    BookCatalogueApi bookCatalogueApi;
    @Inject
    BookCacheApi bookCacheApi;

    public BookUtil(BaseApplication baseApplication) {
        baseApplication.getAppComponent().inject(this);
        File file = new File(cachedPath);
        if (!file.exists()) {
            FileUtils.createDir(cachedPath);
        }
    }

    public synchronized void openBook(ShelfBook shelfBook) throws IOException {
        this.shelfBook = shelfBook;
        //如果当前缓存不是要打开的书本就缓存书本同时删除缓存

        if (bookPath == null || !bookPath.equals(shelfBook.getPath())) {
            cleanCacheFile();

            this.bookPath = shelfBook.getPath();
            bookName = shelfBook.getName();
            from = shelfBook.getFrom();

            Log.i(TAG, "openBook: ???????????"+bookPath+bookName+from);

            cacheBook();

        } else if ("network".equals(from)) {
            RxBus.getDefault().post(new InitPageEvent());
        }
    }

    private void  cleanCacheFile() {
        File file = new File(cachedPath);
        if (!file.exists()) {
            Log.i(TAG, "cleanCacheFile: create cachedPath");
            FileUtils.createDir(cachedPath);
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    public int next(boolean back) {
        position += 1;
        if (position > bookLen) {
            Log.i(TAG, "next: "+position+"   "+bookLen);
            position = bookLen;
            return -1;
        }
        char result = current();
        Log.i(TAG, "next: "+result);
        if (back) {
            position -= 1;
        }
        return result;
    }

    public char[] preLine() {
        if (position <= 0) {
            return null;
        }
        String line = "";
        while (position >= 0) {
            int word = pre(false);
            if (word == -1) {
                break;
            }
            char wordChar = (char) word;
            if ((wordChar + "").equals("\n") && (((char) pre(true)) + "").equals("\r")) {
                pre(false);
//                line = "\r\n" + line;
                break;
            }
            line = wordChar + line;
        }
        return line.toCharArray();
    }

    public char current() {
//        int pos = (int) (position % cachedSize);
//        int cachePos = (int) (position / cachedSize);
        //第几个缓存文件?
        int cachePos = 0;
        //在缓存文件中的位置?
        int pos = 0;
        int len = 0;
        for (int i = 0; i < myArray.size(); i++) {
            long size = myArray.get(i).getSize();
            if (size + len - 1 >= position) {
                cachePos = i;
                pos = (int) (position - len);
                Log.i(TAG, "current: "+pos+" "+size+" "+position);
                break;
            }
            len += size;
        }

        char[] charArray = block(cachePos);
        if(charArray.length == 0){
            Log.i(TAG, "current: ????????");
            return (char)-1;
        }//2777 2778 480405
        Log.i(TAG, "current: "+pos+"  "+charArray.length+"  ");
        return charArray[pos];
    }

    public int pre(boolean back) {
        position -= 1;
        if (position < 0) {
            position = 0;
            return -1;
        }
        char result = current();
        if (back) {
            position += 1;
        }
        return result;
    }

    public long getPosition() {
        return position;
    }

    public void setPostition(long position) {
        this.position = position;
        // 更新book_begin为currentPage.getBegin
    }

    //缓存书本
    @SuppressLint("CheckResult")
    private void cacheBook() throws IOException {
        bookLen = 0;
        directoryList.clear();
        myArray.clear();

        if("network".equals(from)){
            bookLen = shelfBook.getBookLen();
            bookCacheApi.getCacheByBookId(Long.parseLong(bookPath))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        Log.i(TAG, "cacheBook:????????????????????????????????????????? "+result.getCode());
                        List<DataCache> data = result.getData();
                        if(data==null || data.size() == 0) return;
                        for (DataCache dc:data) {
                            Cache cache = new Cache();
                            char[] buf = dc.getData().toCharArray();
                            cache.setSize(buf.length);
                            cache.setData(new WeakReference<>(buf));
//                            Log.i(TAG, "cacheBook: "+dc.getData());
                            cacheAsFile(dc.getIndex(),buf);
                            myArray.add(cache);
                        }
                        bookCatalogueApi.getCatalogueByBookId(Long.parseLong(bookPath))
                                .subscribeOn(Schedulers.io())
                                .subscribe(r -> {
                                    Log.i(TAG, "cacheBook: getCatalogueByBookId"+r);
                                    List<BookCatalogue> catalogueList = r.getData();
                                    directoryList = catalogueList;
                                },Throwable::printStackTrace);

                        RxBus.getDefault().post(new InitPageEvent());
                    },Throwable::printStackTrace);

        }else {
            Log.i(TAG, "cacheBook: local");
            if (TextUtils.isEmpty(shelfBook.getCharset())) {
                String m_strCharsetName = FileUtils.getCharset(bookPath);
                if (m_strCharsetName == null) {
                    m_strCharsetName = "utf-8";
                }
                // 更新charset
                Log.i(TAG, "setCharset: "+m_strCharsetName);
                shelfBook.setCharset(m_strCharsetName);
                updateShelfBookInfoCallBack.updateShelfBook(shelfBook);

            }

            File file = new File(bookPath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), shelfBook.getCharset());
            int index = 0;
            while (true) {
                char[] buf = new char[cachedSize];
                int result = reader.read(buf);
                if (result == -1) {
                    reader.close();
                    break;
                }

                String bufStr = new String(buf);
                bufStr = bufStr.replaceAll("\r\n+\\s*", "\r\n\u3000\u3000");
                bufStr = bufStr.replaceAll("\u0000", "");
                buf = bufStr.toCharArray();
                bookLen += buf.length;

                Cache cache = new Cache();
                Log.i(TAG, "cacheBook: "+index+":::::" + buf.length);
                cache.setSize(buf.length);
                cache.setData(new WeakReference<char[]>(buf));

                myArray.add(cache);

                cacheAsFile(index, buf);
                index++;
            }
            new Thread() {
                @Override
                public void run() {
                    getChapter();
                }
            }.start();
        }
    }

    private void cacheAsFile(int index, char[] buf) {
        try {
            //todo 编码
            File cacheBook = new File(fileName(index));
            if (!cacheBook.exists()) {
                Log.i(TAG, "cacheBook: create");
                cacheBook.createNewFile();
            }
            Log.i(TAG, "cacheBook: before write ");
            final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName(index)), shelfBook.getCharset());
            writer.write(buf);
            writer.close();
            Log.i(TAG, "cacheAsFile: "+buf.length+" "+cacheBook.length());

        } catch (IOException e) {
            throw new RuntimeException("Error during writing " + fileName(index));
        }
    }

    //获取章节
    public synchronized void getChapter() {
        try {
            long size = 0;
            long tmp = 0;
            for (int i = 0; i < myArray.size(); i++) {
                char[] buf = block(i);
                String bufStr = new String(buf);
                String[] paragraphs = bufStr.split("\r\n");
                for (String str : paragraphs) {
                    if (str.length() <= 30 && (str.matches("[\\s\\p{Z}]*第.{1,8}章.*") || str.matches("[\\s\\p{Z}]*第.{1,8}节.*"))) {
                        BookCatalogue bookCatalogue = new BookCatalogue();
                        bookCatalogue.setBookCatalogueStartPos(size);
                        bookCatalogue.setBookCatalogue(str);
                        bookCatalogue.setBookId(shelfBook.getId());
                        Log.i(TAG, "getChapter: ???"+bookCatalogue.getBookCatalogue()+bookCatalogue.getBookCatalogueStartPos()+"   :: "+tmp+"  "+bookLen);
                        directoryList.add(bookCatalogue);
                    }
                    if (str.contains("\u3000\u3000")) {
                        size += str.length() + 2;
                    } else if (str.contains("\u3000")) {
                        size += str.length() + 1;
                    } else {
                        size += str.length();
                    }
                    tmp += str.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BookCatalogue> getDirectoryList() {
        return directoryList;
    }

    public long getBookLen() {
        return bookLen;
    }

    protected String fileName(int index) {
        return cachedPath + bookName + index;
    }

    //获取书本缓存
    @SuppressLint("CheckResult")
    public char[] block(int index) {
        if (myArray.size() == 0) {

            return new char[0];
        }
        WeakReference<char[]> weakReference = myArray.get(index).getData();
        char[] block = weakReference.get();
        if (block == null) {
//            if("local".equals(from)) {
            Log.i(TAG, "block: !!!!!!!!!!!!!!!!!!!!11"+shelfBook.getCharset());
                try {
                    File file = new File(fileName(index));
                    int size = (int) file.length();
                    Log.i(TAG, "block: "+size);
                    if (size < 0) {
                        throw new RuntimeException("Error during reading " + fileName(index));
                    }
                    block = new char[size];
                    InputStreamReader reader =
                            new InputStreamReader(
                                    new FileInputStream(file),
                                    shelfBook.getCharset()
                            );
//                    if (reader.read(block) != block.length) {
//                        throw new RuntimeException("Error during reading " + fileName(index));
//                    }

                    int n = reader.read(block);
                    block = Arrays.copyOf(block,n);
                    reader.close();
                    Log.i(TAG, "block: "+index+":::::"+new String(block).length()+"???"+n);
                } catch (IOException e) {
                    throw new RuntimeException("Error during reading " + fileName(index));
                }
                Cache cache = myArray.get(index);
                cache.setData(new WeakReference<char[]>(block));
            /*}else {
                Log.i(TAG, "block: network");
                bookCacheApi.getBookCache(shelfBook.getId(),index)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            List<DataCache> data = result.getData();
                            if(data==null || data.size() == 0) return;
                            DataCache cache = data.get(0);
                            myArray.get(index).setData(new WeakReference<>(cache.getData().toCharArray()));
                            Log.i(TAG, "block: "+cache);
                            RxBus.getDefault().post(new InvalidateEvent(position));

                        },Throwable::printStackTrace);
                block = new char[0];
            }*/
        }
        return block;
    }
    private UpdateShelfBookInfoCallBack updateShelfBookInfoCallBack;

    public void setUpdateShelfBookInfoCallBack(UpdateShelfBookInfoCallBack updateShelfBookInfoCallBack) {
        this.updateShelfBookInfoCallBack = updateShelfBookInfoCallBack;
    }

    public interface UpdateShelfBookInfoCallBack{
        void updateShelfBook(ShelfBook shelfBook);
    }
}
