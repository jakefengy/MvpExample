package com.pujieinfo.mobile.framework.mvp;

import io.reactivex.disposables.Disposable;

/**
 * 2017-04-11.
 */

public interface IBaseContract {

    interface Presenter {

        void addDisposable(Disposable disposable);

        void release();
    }

}
