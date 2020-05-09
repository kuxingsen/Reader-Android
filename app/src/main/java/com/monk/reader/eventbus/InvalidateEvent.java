package com.monk.reader.eventbus;



public class InvalidateEvent implements RxEvent {
    private long begin;

    public InvalidateEvent(long begin) {
        this.begin = begin;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }
}
