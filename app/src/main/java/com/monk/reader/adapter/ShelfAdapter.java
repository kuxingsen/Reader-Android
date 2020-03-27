package com.monk.reader.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.monk.reader.R;
import com.monk.reader.dao.ShelfBookDao;
import com.monk.reader.dao.bean.ShelfBook;
import com.monk.reader.ui.base.BaseActivity;
import com.monk.reader.utils.SharedPreferencesUtils;
import com.monk.reader.view.DragGridListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShelfAdapter extends BaseAdapter implements DragGridListener {
    private List<ShelfBook> shelfBookList;
    private LayoutInflater inflater;
    private int mHidePosition = -1;
    private int shelfCount;
    @Inject
    ShelfBookDao shelfBookDao;
    @Inject
    SharedPreferencesUtils sharedPreferencesUtils;

    public ShelfAdapter(Context context, List<ShelfBook> shelfBookList) {
        this.shelfBookList = shelfBookList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ((BaseActivity)context).getmApplication().getAppComponent().inject(this);

        shelfCount = sharedPreferencesUtils.getInt("shelf_count",0);
    }
    @Override
    public int getCount() {
        return shelfCount;
    }

    @Override
    public Object getItem(int position) {
        return shelfBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.shelfitem, null);
            viewHolder = new ViewHolder(contentView);
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        if (shelfBookList.size() > position) {
            //DragGridView  解决复用问题
            if (position == mHidePosition) {
                contentView.setVisibility(View.INVISIBLE);
            } else {
                contentView.setVisibility(View.VISIBLE);
            }

            String fileName = shelfBookList.get(position).getName();
            viewHolder.name.setText(fileName);
        }

        return contentView;
    }


    class ViewHolder {
        @BindView(R.id.ib_close)
        ImageButton deleteItem_IB;
        @BindView(R.id.tv_name)
        TextView name;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void addNewBook(ShelfBook newBook) {
        newBook.setPosition(shelfCount);
        shelfBookDao.insert(newBook);
        shelfBookList.add(newBook);
        shelfCount++;
        sharedPreferencesUtils.putInt("shelf_count",shelfCount);

        setItemToFirst(newBook.getPosition());
    }
    /**
     * Drag移动时item交换数据,并在数据库中更新交换后的位置数据
     *
     * @param oldPosition
     * @param newPosition
     */
    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        changePosition(oldPosition, newPosition);
        ShelfBook remove = shelfBookList.remove(oldPosition);
        shelfBookList.add(newPosition,remove);

        updateShelfBookList2SQLite();
    }


    /**
     * 隐藏item
     *
     * @param hidePosition
     */
    @Override
    public void setHideItem(int hidePosition) {
        this.mHidePosition = hidePosition;
        notifyDataSetChanged();
    }

    /**
     * 删除书本
     *
     * @param deletePosition
     */
    @Override
    public void removeItem(int deletePosition) {
        changePosition(deletePosition,Integer.MAX_VALUE);
        ShelfBook remove = shelfBookList.remove(deletePosition);
        deleteShelfBookListInSQLite(remove);
        shelfCount--;
        sharedPreferencesUtils.putInt("shelf_count",shelfCount);
        notifyDataSetChanged();
    }


    /**
     * Book打开后位置移动到第一位
     *
     * @param openPosition
     */
    @Override
    public void setItemToFirst(int openPosition) {
        reorderItems(openPosition,0);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataRefresh() {
        notifyDataSetChanged();
    }

    private void putAsyncTask(AsyncTask<Void, Void, Void> asyncTask) {
        asyncTask.execute();
    }

    /**
     * 将删除数据库的操作异步执行
     */
    private void deleteShelfBookListInSQLite(ShelfBook remove) {
        putAsyncTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                shelfBookDao.delete(remove);
                return null;
            }
        });
    }
    /**
     * 将更新数据库的操作异步执行
     */
    private void updateShelfBookList2SQLite() {
        putAsyncTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                shelfBookDao.updateInTx(shelfBookList);
                return null;
            }
        });
    }
    /**
     * 将oldPosition的书挪到newPosition,只改变书对象的position属性，没有调整List的位置
     * if(oldPosition > newPosition) 即后面挪到前面时，处于(new,old]的书位置+1，old书的位置变为new
     * else 即前面挪到后面时，处于(old,new]的书位置-1，old书的位置变为new
     * @param oldPosition
     * @param newPosition
     */
    private void changePosition(int oldPosition, int newPosition) {
        for (ShelfBook book :
                shelfBookList) {
            int position = book.getPosition();
            if(oldPosition > newPosition){
                if(position>=newPosition &&position<oldPosition){
                    position++;
                }else if(position == oldPosition){
                    position = newPosition;
                }else {
                    continue;
                }
            }else {
                if(position>oldPosition &&position<=newPosition){
                    position--;
                }else if(position == oldPosition){
                    position = newPosition;
                }else{
                    continue;
                }
            }
            book.setPosition(position);
        }
    }

}
