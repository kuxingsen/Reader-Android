package com.monk.reader.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.monk.reader.R;
import com.monk.reader.ui.activity.DownloadActivity;
import com.monk.reader.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MyselfFragment extends BaseFragment {
    @BindView(R.id.myself_avatar_iv)
    ImageView myself_avatar_iv;
    @BindView(R.id.myself_login_tv)
    TextView myself_login_tv;
    @BindView(R.id.myself_history_ll)
    LinearLayout myself_history_ll;
    @BindView(R.id.myself_download_ll)
    LinearLayout myself_download_ll;

    @Override
    protected int layoutId() {
        return R.layout.fragment_myself;
    }

    @Override
    protected void initAfter() {
//        Glide.with(mActivity)
//                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587814757216&di=485fc131b52ac095f87f62f3bcce74e6&imgtype=0&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D643274985%2C2283651253%26fm%3D214%26gp%3D0.jpg")
//                .into(myself_avatar_iv);
        Glide.with(mActivity)
                .load("https://b-ssl.duitang.com/uploads/item/201808/06/20180806101031_hZyjM.thumb.700_0.jpeg")
                .into(myself_avatar_iv);
        myself_login_tv.setText("枫枫枫");
    }

    @OnClick(R.id.myself_login_tv)
    public void loginClick(View v) {

    }

    @OnClick(R.id.myself_history_ll)
    public void historyClick(View v) {
        toActivity("/activity/history", null);
    }

    @OnClick(R.id.myself_download_ll)
    public void downloadClick(View v) {
        toActivity("/activity/download", null);
    }
}
