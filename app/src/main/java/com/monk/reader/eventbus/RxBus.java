package com.monk.reader.eventbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Qiao on 2017/2/3.
 */

public class RxBus {
    private static volatile RxBus defaultInstance;

    private final PublishSubject<RxEvent> bus;

    private RxBus() {
        bus = PublishSubject.create();
    }

    public static RxBus getDefault() {
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new RxBus();
                }
            }
        }
        return defaultInstance;
    }

    public void post(RxEvent event) {
        bus.onNext(event);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}