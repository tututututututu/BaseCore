package com.hzecool.core.FileUpLoadDownLoad;

import java.io.File;

/**
 * 文件下载回调接口
 * Created by tutu on 2017/3/15.
 */

public interface FileDownCallBackInterface {

    /**
     * 当前进度
     */
    void onProress(float progress);

    /**
     * 下载文件成功的回调
     *
     * @param file 文件
     */
    void onSuccess(File file);

    /**
     * 网络请求失败的回调
     *
     * @param e
     */
    void onFail(Exception e);

}
