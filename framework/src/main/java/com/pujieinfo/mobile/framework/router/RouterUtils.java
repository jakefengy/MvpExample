package com.pujieinfo.mobile.framework.router;

import android.content.Context;

import com.fengy.framework.router.MaApplication;
import com.fengy.framework.router.action.MaActionResult;
import com.fengy.framework.router.router.LocalRouter;
import com.fengy.framework.router.router.RouterRequest;
import com.fengy.framework.router.router.RouterResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * 2017-05-19.
 */

public class RouterUtils {

    public static Observable<RouterResponse> route(Context context, RouterRequest request) {
        return build(context, request);
    }

    private static Observable<RouterResponse> build(Context context, RouterRequest request) {
        try {
            return Observable
                    .just(LocalRouter.getInstance(MaApplication.getMaApplication()).route(context, request))
                    .compose(check());
        } catch (Exception e) {
            e.printStackTrace();
            return Observable.error(e);
        }
    }

    private static ObservableTransformer<RouterResponse, RouterResponse> check() {

        return upstream -> upstream.flatMap(response -> {

            if (response == null) {
                return Observable.error(new Throwable("未知错误"));
            } else {
                if (response.getCode() == MaActionResult.CODE_SUCCESS) {
                    return Observable.just(response);
                } else {
                    return Observable.error(new Throwable(response.getMessage()));
                }
            }
        });
    }

}
