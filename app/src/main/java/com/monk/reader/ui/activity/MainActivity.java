package com.monk.reader.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.monk.reader.R;
import com.monk.reader.eventbus.RxBus;
import com.monk.reader.eventbus.RxEvent;
import com.monk.reader.eventbus.ShowActionBarCancel;
import com.monk.reader.eventbus.HideShelfDeleteButton;
import com.monk.reader.ui.base.BaseActivity;
import com.monk.reader.ui.fragment.FragmentController;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

@Route(path = "/activity/main")
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";


    private FragmentController mController;

    @Override
    protected int inflateLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initBefore() {
        super.initBefore();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

    }

    @Override
    protected void initAfter() {
        Log.i(TAG, "initAfter: ");
        mController = FragmentController.instance(this,R.id.frame_home_tab);
        mController.showFragment(0);
    }

    @OnCheckedChanged({R.id.rb_recommend,R.id.rb_stack,R.id.rb_download})
    public void OnCheckedChanged(CompoundButton buttonView, boolean isChecked){
        switch (buttonView.getId()){
            case R.id.rb_recommend:
                if(isChecked)
                    mController.showFragment(0);
                break;
            case R.id.rb_stack:
                if(isChecked)
                    mController.showFragment(1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentController.onDestroy();
    }


    @Override
    protected void requestPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Toast.makeText(this, "您已经申请了外存写入权限!", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        } else {
            Toast.makeText(this, "您已经申请了外存访问权限!", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 3);
        } else {
            Toast.makeText(this, "您已经申请了网络访问权限!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

//todo
        if (requestCode == 1 ) {
            Toast.makeText(this, "申请外存写入权限成功!", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 2) {
            Toast.makeText(this, "申请外存访问权限成功!", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 3) {
            Toast.makeText(this, "申请网络访问权限成功!", Toast.LENGTH_SHORT).show();
        }
    }
}
