package com.hzecool.core.net;


import com.hzecool.core.bean.BaseResponseBean;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * retrofit返回调度中心
 * 线程调度  统一结果code处理
 * Created by tutu on 2017/3/10.
 */

public class RxHelper {

    public static <T> ObservableTransformer<BaseResponseBean<T>, T> handleResult() {
        return upstream -> upstream.flatMap(
                result -> {
                    if ("0".equals(result.getCode())) {
                        return createData(result.getData());
                    } else {
                        return Observable.error(new BussinessException(result.getCode(),result.getMsg()));
                    }
                }

        ).compose(switchSchedulers());
    }

    private static <T> Observable<T> createData(final T t) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(t);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    public static <T> ObservableTransformer<T, T> switchSchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
