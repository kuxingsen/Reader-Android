package com.monk.reader.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;


public class CatalogueFragment extends ListFragment {
    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        onItemClickCallBack.onListItemClick(l,v,position,id);
    }

    private OnItemClickCallBack onItemClickCallBack;

    public void setOnItemClickCallBack(OnItemClickCallBack onItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack;
    }

    public interface OnItemClickCallBack{
        void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id);
    }
}
