package com.hzecool.core.net;

/**
 * 文件上传回调接口
 * Created by tutu on 2017/3/15.
 */

public interface FileUploadCallBackInterface {

    /**
     * 网络请求失败的回调
     *
     * @param e
     */
    void onFail(Exception e);

    /**
     * 网络请求成功的回调
     */
    void onSuccess(String s);

    /**
     * 当前进度
     */
    void onProress(float progress);

}
