package com.hzecool.core.MvpBase;

/**
 * 所有View层的基类
 * Created by tutu on 2017/2/25.
 */

public interface TIBaseView<B> {

    /**
     * 加载数据成功
     *
     * @param b 自定义数据类型
     */
    void onLoadData(B b);

    /**
     * 加载数据为空
     */
    void onEmptyData();

    /**
     * 加载数据失败
     *
     * @param msg
     */
    void onLoadError(String msg);

    /**
     * 网络错误
     *
     * @param msg
     */
    void onNetError(String msg);
}
