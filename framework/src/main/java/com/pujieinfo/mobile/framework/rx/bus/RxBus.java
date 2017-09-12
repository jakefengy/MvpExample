package com.pujieinfo.mobile.framework.rx.bus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * RxBus
 */
public class RxBus {

    private static class SingletonHolder {
        private static final RxBus INSTANCE = new RxBus();
    }

    //获取单例
    public static RxBus getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final Subject bus;

    private RxBus() {
        bus = PublishSubject.create();
    }

    // 发射一个事件
    public void post(Object o) {
        bus.onNext(o);
    }

    public <T> Observable<T> toObservable(final Class<T> eventType) {
        return bus.ofType(eventType);
    }

}
