package com.monk.reader.eventbus;

import com.monk.reader.dao.bean.ShelfBook;

public class AddToShelfEvent implements RxEvent {
    private ShelfBook newBook;
    public AddToShelfEvent(ShelfBook shelfBook){
        this.newBook = shelfBook;
    }

    public ShelfBook getNewBook() {
        return newBook;
    }
}
