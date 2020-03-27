package com.monk.reader.eventbus;

import com.monk.reader.dao.bean.ShelfBook;

/**
 * Created by Qiao on 2017/8/11.
 */

public class AddToShelfEvent implements RxEvent {
    private ShelfBook newBook;
    public AddToShelfEvent(ShelfBook shelfBook){
        this.newBook = shelfBook;
    }

    public ShelfBook getNewBook() {
        return newBook;
    }
}
