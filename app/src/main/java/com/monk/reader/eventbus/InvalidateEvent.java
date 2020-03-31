package com.monk.reader.eventbus;


/**
 * Created by Qiao on 2017/8/11.
 */

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
