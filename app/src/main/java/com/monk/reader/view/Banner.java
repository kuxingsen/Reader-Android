package com.monk.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class Banner extends ViewPager {

    private float downX;
    private float moveX;

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
