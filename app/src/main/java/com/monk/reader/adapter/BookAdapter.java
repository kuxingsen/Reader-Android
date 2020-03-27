package com.monk.reader.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.monk.reader.R;
import com.monk.reader.retrofit2.bean.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> bookList;
    private ShowRangeContentListener showRangeContentListener;


    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvAuthor.setText(book.getAuthor()+" 著");
        holder.tvName.setText(book.getName());
        holder.tvIntro.setText(book.getIntroduction());
        holder.tvRange.setText(book.getRangeValue());
//        if (showRangeContentListener != null)
//            showRangeContentListener.showRange(holder.tvRange, book);
//        else holder.tvRange.setText("");
        Glide.with(holder.itemView.getContext()).load(book.getPicture()).into(holder.ivPicture);
        holder.itemView.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putLong("bookId", book.getId());
            bundle.putString("from", "network");
            ARouter.getInstance().build("/activity/info").with(bundle).navigation();
        });
    }

    @Override
    public int getItemCount() {
        return bookList == null?0:bookList.size();
    }

    public void setShowRangeContentListener(ShowRangeContentListener showRangeContentListener) {
        this.showRangeContentListener = showRangeContentListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_intro)
        TextView tvIntro;
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.tv_range)
        TextView tvRange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ShowRangeContentListener {
        void showRange(TextView tvRange, Book book);
    }
}
