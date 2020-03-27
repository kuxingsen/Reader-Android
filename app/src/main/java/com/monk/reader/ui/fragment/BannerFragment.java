package com.monk.reader.ui.fragment;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.monk.reader.R;
import com.monk.reader.retrofit2.bean.Banner;
import com.monk.reader.ui.base.BaseFragment;

import butterknife.BindView;

public class BannerFragment extends BaseFragment {
    private static final String TAG = "BannerFragment";
    private Banner banner;

    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.tv_banner)
    TextView tvBanner;

    public BannerFragment(Banner banner) {
        this.banner = banner;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_banner;
    }

    @Override
    protected void initAfter() {
        Glide.with(getContext()).load(banner.getPicture()).into(ivBanner);
        tvBanner.setText(banner.getUrl());
    }
}
