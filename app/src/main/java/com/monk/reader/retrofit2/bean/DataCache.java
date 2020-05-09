package com.monk.reader.retrofit2.bean;

import java.lang.ref.WeakReference;

public class DataCache {
    private long size;
    private String data;
    private int index;

    @Override
    public String toString() {
        return "DataCache{" +
                "size=" + size +
                ", data='" + data + '\'' +
                ", index=" + index +
                '}';
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
