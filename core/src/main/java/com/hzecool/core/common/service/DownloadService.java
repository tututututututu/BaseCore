package com.hzecool.core.common.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;


/**
 * 下载文件Service
 */
public class DownloadService extends Service {
    /**
     * 广播接受者
     */
    private BroadcastReceiver receiver;
    /**
     * 系统下载管理器
     */
    private DownloadManager dm;
    /**
     * 系统下载器分配的唯一下载任务id，可以通过这个id查询或者处理下载任务
     */
    private long enqueue;
    /**
     * 下载地址
     */
    private String downloadUrl = "http://192.168.3.186/StaffAssistant.apk";
    private static String apkname = "StaffAssistant.apk";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        downloadUrl = intent.getStringExtra("downloadUrl");
        if (TextUtils.isEmpty(downloadUrl)) {
            apkname = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1, downloadUrl.length());
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                install(context);
                //销毁当前的Service
//                stopSelf();
            }
        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        //下载需要写SD卡权限, targetSdkVersion>=23 需要动态申请权限
        startDownload(downloadUrl);
        return Service.START_STICKY;
    }

    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    public static void install(Context context) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , apkname);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri =
                    FileProvider.getUriForFile(context, "com.hzecool.slhStaff.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        //服务销毁的时候 反注册广播
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 开始下载
     */
    private void startDownload(String downUrl) {
        //获得系统下载器
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        //设置下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downUrl));
        //设置下载文件的类型
        request.setMimeType("application/vnd.android.package-archive");
        //设置下载存放的文件夹和文件名字
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkname);
        //设置下载时或者下载完成时，通知栏是否显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载新版本");
        //执行下载，并返回任务唯一id
        enqueue = dm.enqueue(request);
    }
}