package com.hzecool.core.FileUpLoadDownLoad;


import android.support.annotation.Nullable;

import com.hzecool.core.R;
import com.hzecool.core.base.TAbsActivity;
import com.hzecool.core.common.utils.ResourceUtils;
import com.hzecool.core.common.utils.ToastUtils;
import com.hzecool.core.log.L;
import com.hzecool.widget.materialdialog.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 文件下载上传管理器
 * Created by tutu on 2017/3/12.
 */

public class FileDownUpLoad {

    private MaterialDialog dialog;

    /**
     * 下载文件
     */
    public void downLoadFile(String tag, String url, String destFileDir, String name,
                             final FileDownCallBackInterface downCallBackInterface) {//
        L.i("url====" + url);
        OkGo.get(url)
                .tag(tag)
                .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)    // 缓存模式，详细请看缓存介绍
                .execute(new FileCallback(destFileDir, name) {  //文件下载时，可以指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        L.i("onSuccess===" + file);
                        // file 即为文件数据，文件保存在指定目录
                        downCallBackInterface.onSuccess(file);
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调下载进度(该回调在主线程,可以直接更新ui)
                        downCallBackInterface.onProress(progress);
                    }

                    @Override
                    public void onCacheSuccess(File file, Call call) {
                        L.i("onCacheSuccess===" + file + " call == " + call);
                        super.onCacheSuccess(file, call);
                        // file 即为文件数据，文件保存在指定目录
                        downCallBackInterface.onSuccess(file);
                        L.i("读取缓存成功" + file);
                    }

                    @Override
                    public void onCacheError(Call call, Exception e) {
                        super.onCacheError(call, e);
                        L.i("读取缓存失败" + e);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        downCallBackInterface.onFail(e);
                        L.i("图片加载失败" + e);
                    }

                    @Override
                    public void onAfter(File file, Exception e) {
                        super.onAfter(file, e);
                        L.i("图片加载完成" + file);
                    }
                });

    }


    /**
     * 上传文件
     *
     * @param tag                     tag用来取消任务
     * @param url                     上传路径url
     * @param key                     文件对应的key
     * @param fileList                文件file
     * @param uploadCallBackInterface 上传回调
     * @param activity                activity用来显示进度框 如果后面showProgress 这个参数为false可以传null
     * @param showProgress            是否显示进度框
     */
    public void upFile(String tag, String url, String key, List<File> fileList, final
    FileUploadCallBackInterface uploadCallBackInterface, @Nullable WeakReference<TAbsActivity> activity, @Nullable boolean showProgress) {

        if (fileList == null || fileList.isEmpty()) {
            L.logFile("上传文件为空,直接return");
            return;
        }
        L.logFile("upFile: url=" + url);

        StringBuffer sb = new StringBuffer();
        sb.append("upFile: key=" + key);
        sb.append("上传文件路径:" + "\n");
        for (File file : fileList) {
            sb.append(" " + file.getAbsolutePath()).append("\n");
        }

        L.logFile(sb.toString());

        if (showProgress && activity != null && activity.get() instanceof TAbsActivity) {
            dialog = new MaterialDialog.Builder(activity.get())
                    .title(ResourceUtils.getString(R.string.core_upLoading))
                    .progress(false, 100)
                    .build();
            dialog.show();

        }

        OkGo.post(url)//
                .tag(tag)//
                .addFileParams(key, fileList)
//                .isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
//                .params("file1", file)   // 可以添加文件上传
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        L.i("respdata===" + s);
                        //上传成功
                        if (uploadCallBackInterface != null) {
                            uploadCallBackInterface.onSuccess(s);
                        }
                        if (dialog != null) {
                            dialog.cancel();
                        }
                    }


                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        if (uploadCallBackInterface != null) {
                            uploadCallBackInterface.onProress(progress);
                        }
                        if (dialog != null) {
                            dialog.setProgress((int) (progress * 100));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (uploadCallBackInterface != null) {
                            uploadCallBackInterface.onFail(e);
                        }
                        ToastUtils.showShortToast(ResourceUtils.getString(R.string.base_netError));
                        if (dialog != null) {
                            dialog.cancel();
                        }
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if (dialog != null) {
                            dialog.cancel();
                        }
                    }
                });


    }

    public void cancelRequest(String tag) {
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(tag);
    }

    public void cancelAllRequest() {
        //根据 Tag 取消请求
        OkGo.getInstance().cancelAll();
    }
}
