package com.monk.reader.ui.activity;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.monk.reader.BaseApplication;
import com.monk.reader.R;
import com.monk.reader.adapter.FileAdapter;
import com.monk.reader.adapter.WrapperAdapter;
import com.monk.reader.dagger.DaggerAppComponent;
import com.monk.reader.dao.DaoSession;
import com.monk.reader.dao.ShelfBookDao;
import com.monk.reader.dao.bean.ShelfBook;
import com.monk.reader.eventbus.AddToShelfEvent;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.ui.base.BaseActivity;
import com.monk.reader.ui.fragment.FileFragment;
import com.monk.reader.ui.fragment.WaitingFragment;
import com.monk.reader.utils.FileUtils;
import com.monk.reader.utils.SharedPreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

@Route(path = "/activity/file/chooser")
public class FileChooserActivity extends BaseActivity {
    private static final String TAG = "FileChooserActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Autowired(name = "filenameFilter")
    public String filenameFilter;

    @Inject
    DaoSession daoSession;
    @Inject
    SharedPreferencesUtils sharedPreferencesUtils;
    private Set<String> addedSet;

    @Override
    protected void initBefore() {
        super.initBefore();


        getmApplication().getAppComponent().inject(this);
    }

    @Override
    protected int inflateLayout() {
        return R.layout.activity_file_chooser;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initAfter() {
        super.initAfter();
        if(filenameFilter == null){
            Log.i(TAG, "initAfter: filenameFilter==null???");
            filenameFilter = "txt";
        }
        addedSet = getAddedSet();
        File file = Environment.getExternalStorageDirectory();

        FileAdapter fileAdapter = new FileAdapter(file,filenameFilter, addedSet);
        fileAdapter.setClickFilterFileCallBack((v, p, f) -> {
            Log.i(TAG, "ClickFilterFile: "+p+" "+f.getName()+" "+f.getAbsolutePath());
            ShelfBook newBook = new ShelfBook();
            newBook.setName(f.getName());
            newBook.setBegin(0L);
            newBook.setCharset(null);
            newBook.setPath(f.getAbsolutePath());
            Log.i(TAG, "initAfter: newBook"+newBook);
            RxBus.getDefault().post(new AddToShelfEvent(newBook));
        });
        fileAdapter.setReplaceFileFragmentCallBack((fragment) -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_file_show,fragment)
                    .addToBackStack(null)
                    .commit();
        });
      /*  WrapperAdapter adapter = new WrapperAdapter(fileAdapter);
        TextView textView = new TextView(this);
        textView.setText("i am header");
        adapter.addHeaderView(textView);
        adapter.addFooterView(textView);*/

      getSupportFragmentManager().beginTransaction()
              .replace(R.id.frame_file_show,new FileFragment(fileAdapter))
              .addToBackStack(null)
              .commit();
    }

    private Set<String> getAddedSet() {
        Set<String> addedSet = new HashSet<>();
        List<ShelfBook> shelfBookList = daoSession.loadAll(ShelfBook.class);
        for (ShelfBook shelfBook :
                shelfBookList) {
            addedSet.add(shelfBook.getPath());
        }
        return addedSet;
    }

    @Override
    protected void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        } else {
            Toast.makeText(this, "您已经申请了外存访问权限!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 2) {
            Toast.makeText(this, "申请外存访问权限成功!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_search,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(FileChooserActivity.this, ""+query, Toast.LENGTH_SHORT).show();

                List<File> foundFiles = new ArrayList<>();
                FileFragment fileFragment = (FileFragment)getSupportFragmentManager().findFragmentById(R.id.frame_file_show);
                if(fileFragment != null){
                    FileAdapter fileAdapter = fileFragment.getFileAdapter();
                    File dir = fileAdapter.getDir();
                    FileUtils.searchFiles(dir,query,filenameFilter, foundFiles);
                    File[] files = new File[foundFiles.size()];
                    foundFiles.toArray(files);
                    Log.i(TAG, "onQueryTextSubmit: "+foundFiles);
                    Log.i(TAG, "onQueryTextSubmit: "+files.length);

                    FileAdapter fileAdapter2 = new FileAdapter(files,filenameFilter,addedSet);
                    fileAdapter2.setClickFilterFileCallBack(fileAdapter.getClickFilterFileCallBack());
                    fileAdapter2.setReplaceFileFragmentCallBack(fileAdapter.getReplaceFileFragmentCallBack());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_file_show,new FileFragment(fileAdapter2))
                            .addToBackStack(null)
                            .commit();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: "+newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
