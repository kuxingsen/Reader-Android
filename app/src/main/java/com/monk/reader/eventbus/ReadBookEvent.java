package com.monk.reader.eventbus;

import com.monk.reader.dao.bean.ReadInfo;
import com.monk.reader.dao.bean.ShelfBook;
import com.monk.reader.retrofit2.bean.User;


public class ReadBookEvent implements RxEvent {
   private ReadInfo readInfo;
    public ReadBookEvent(ReadInfo readInfo){
        this.readInfo = readInfo;
    }

    public ReadInfo getReadInfo() {
        return readInfo;
    }
}
