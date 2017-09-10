package com.hzecool.core.net;


import com.hzecool.core.R;
import com.hzecool.core.common.utils.NetworkUtils;
import com.hzecool.core.common.utils.ResourceUtils;
import com.hzecool.core.common.utils.ToastUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


/**
 * Rx自定义订阅者
 * Created by tutu on 2017/3/10.
 */

public abstract class RxObserver<T> implements Observer<T> {
    protected abstract void onSuccess(T t);

    protected abstract void onFail(String msg);

    protected abstract void onNetError(String msg);


    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShortToast(ResourceUtils.getString(R.string.base_netError));
            onNetError(ResourceUtils.getString(R.string.base_netError));
        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showShortToast("连接超时");
            onFail(ResourceUtils.getString(R.string.connect_time_out));
        } else if (e instanceof BussinessException) {
            BussinessException exception = (BussinessException) e;
            ToastUtils.showShortToast(exception.getMessage());
            onFail(exception.getMessage());
        } else if (e instanceof HttpException) {
            ToastUtils.showShortToast(ResourceUtils.getString(R.string.base_request_faile));
            onFail(ResourceUtils.getString(R.string.base_request_faile));
        } else {
            ToastUtils.showShortToast(e.getMessage());
            onFail(e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }
}
