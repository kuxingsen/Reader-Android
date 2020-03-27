package com.monk.reader.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.monk.reader.R;
import com.monk.reader.dao.bean.BookCatalogue;
import com.monk.reader.retrofit2.bean.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.ViewHolder> {
    private List<BookCatalogue> bookCatalogueList;

    public CatalogueAdapter(List<BookCatalogue> bookCatalogueList) {
        this.bookCatalogueList = bookCatalogueList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookCatalogue catalogue = bookCatalogueList.get(position);
        holder.tvItem.setText(catalogue.getBookCatalogue());

        holder.itemView.setOnClickListener(v -> {
            //todo 完善
            Bundle bundle = new Bundle();
            bundle.putLong("bookId", catalogue.getBookId());
            bundle.putLong("start", catalogue.getBookCatalogueStartPos());
            ARouter.getInstance().build("/activity/reader").with(bundle).navigation();
        });
    }

    @Override
    public int getItemCount() {
        return bookCatalogueList == null ? 0 : bookCatalogueList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item)
        TextView tvItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
