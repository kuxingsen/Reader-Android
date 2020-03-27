package com.monk.reader.ui.fragment;

import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.monk.reader.R;
import com.monk.reader.adapter.FileAdapter;
import com.monk.reader.adapter.WrapperAdapter;

import com.monk.reader.dagger.DaggerAppComponent;
import com.monk.reader.ui.base.BaseFragment;

import java.io.File;

import butterknife.BindView;

public class FileFragment extends BaseFragment {
    private static final String TAG = "FileFragment";
    @BindView(R.id.rv_show_file)
    RecyclerView rvShowFile;
    private FileAdapter fileAdapter;


    public FileFragment(FileAdapter fileAdapter) {
        this.fileAdapter = fileAdapter;
    }

    @Override
    protected void initBefore() {
        super.initBefore();

        getmApplication().getAppComponent().inject(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_file;
    }

    @Override
    protected void initAfter() {
        rvShowFile.setLayoutManager(new LinearLayoutManager(mActivity));
        File dir = fileAdapter.getDir();
        if(dir != null){
            WrapperAdapter adapter = new WrapperAdapter(fileAdapter);
            TextView textView = new TextView(mActivity);
            textView.setText("Path: "+dir.getAbsolutePath());
            adapter.addHeaderView(textView);
            rvShowFile.setAdapter(adapter);
        }else {
            rvShowFile.setAdapter(fileAdapter);
        }
    }


    public FileAdapter getFileAdapter() {
        return fileAdapter;
    }
}
