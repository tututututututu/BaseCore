package com.hzecool.core.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统自带分享的工具类
 * Created by song on 2017/8/1.
 */

public class LocalShareUtils {

    /**
     * 常用应用的包名
     * qq
     */
    private static String QQ = "tencent.mobileqq";
    /**
     * 微信
     */
    private static String WEICHAT = "com.tencent.mm";
    /**
     * 新浪微博
     */
    private static String SINA = "com.sina.weibo";

    /**
     * 分享文字至所有第三方软件
     *
     * @param context
     * @param content
     */
    public static void ShareText(Context context, String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(intent, "分享到"));

    }

    /**
     * 分享单张图片至所有第三方软件
     */
    public static void ShareSingleImage(Context context, File file) {
        Uri imageUri;

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(context,"com.hzecool.common.fileProvider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }


        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));

    }

    /**
     * 分享多张图片至所有第三方软件
     */
    public static void ShareMultipleImage(Context context, List<File> fileList) {
        ArrayList<Uri> uriList = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory() + File.separator;
        for (int i = 0; i < fileList.size(); i++) {
            uriList.add(Uri.fromFile(fileList.get(i)));
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
      /*  CacheDirManager.INSTANCE.clear();
        deleteDir(getTyleCachePath(SHARE_CACHE_PATH));*/
    }

    /**
     * 分享多图到指定软件,QQ,微信,新浪微博
     */
    public static void ShareImagesToAssignAPP(Context context, List<File> fileList,String type) {
        boolean found = false;
        ArrayList<Uri> uriList = new ArrayList<>();
        Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
        share.setType("image/*");
        for (int i = 0; i < fileList.size(); i++) {
            uriList.add(Uri.fromFile(fileList.get(i)));
        }
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type) ) {
                    share.setPackage(info.activityInfo.packageName);
                    share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
                    found = true;
                    break;
                }
            }
            if (!found)
                return;
            context.startActivity(Intent.createChooser(share, "分享到"));
        }
    }
}
