package com.monk.reader.ui.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.monk.reader.ui.base.BaseActivity;
import com.monk.reader.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class FragmentController {
    private static final String TAG = "FragmentController";
    private static FragmentController mController;
    private FragmentManager mManager;
    private int containerId;
    private List<BaseFragment> lists;
    private int mPosition;

    public static FragmentController instance(BaseActivity activity, int containerId) {
        if (mController == null) {
            mController = new FragmentController(activity, containerId);
        }
        return mController;
    }

    private FragmentController(BaseActivity activity, int containerId) {
        mManager = activity.getSupportFragmentManager();
        this.containerId = containerId;
        initFragment();
    }

    private void initFragment() {

        lists = new ArrayList<>();
        lists.add(new HomeFragment());
        BookcaseFragment bookcaseFragment = new BookcaseFragment();
        Bundle arguments = new Bundle();
        arguments.putLong("selectId",0);
        bookcaseFragment.setArguments(arguments);
        lists.add(bookcaseFragment);
//        lists.add(new DemoFragment());
        lists.add(new MyselfFragment());

    }

    public void showFragment(int position) {
        Log.i(TAG, "showFragment: "+position);
        mPosition = position;
        BaseFragment baseFragment = lists.get(position);
        mManager.beginTransaction()
                .replace(containerId,baseFragment)
                .commit();
    }


    public static void onDestroy() {
        mController = null;
    }

}
