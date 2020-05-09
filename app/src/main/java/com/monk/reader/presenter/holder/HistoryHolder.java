package com.monk.reader.presenter.holder;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.monk.reader.BaseApplication;
import com.monk.reader.R;
import com.monk.reader.dao.bean.ReadInfo;
import com.monk.reader.dao.bean.ShelfBook;
import com.monk.reader.presenter.BaseHolder;

import butterknife.BindView;

public class HistoryHolder extends BaseHolder<ReadInfo> {

    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_range)
    TextView tvRange;

    public HistoryHolder(View itemView, BaseApplication application) {
        super(itemView, application);
    }

    @Override
    public void bindData(int position, ReadInfo readInfo) {

        Long bookId = readInfo.getBookId();
        tvName.setText(readInfo.getBookName());
        tvSize.setText(readInfo.getBookSize()+" 字");
        tvRange.setText(toDurationStr(readInfo.getDuration()));
        Glide.with(context).load("http://192.168.43.14:8080"+readInfo.getBookPicture()).into(ivPicture);

        itemView.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putLong("bookId", bookId);
            bundle.putString("from", "network");
            ARouter.getInstance().build("/activity/info").with(bundle).navigation();
        });
    }

    private String toDurationStr(Long duration) {
        long h = duration/60;
        int m = (int) (duration % 60);
        String durationStr = "";
        if(h > 0) durationStr = h+"小时";
        durationStr += m+"分钟";
        return durationStr;
    }
}
