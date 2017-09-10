package com.hzecool.core.common.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

/**
 * 微信分享(非SDK版)工具类
 */
public class ShareUtils {

    public static String tencentPk = "com.tencent.mm";
    public static String tencentAction = "android.intent.action.SEND_MULTIPLE";

    /**
     * 不实用微信的SDK分享图片到好友
     *
     * @param context
     * @param
     */
    public static boolean sharePicToFriendNoSDK(Context context, String Kdescription, List<String> paths) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, Kdescription));//复制到剪切板
        if (!isInstallWeChart(context)) {
            makeText(context, "您没有安装微信", Toast.LENGTH_SHORT).show();
            return false;
        }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName(tencentPk, "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction(tencentAction);
        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_TEXT, Kdescription);
//        intent.putExtra("Kdescription", Kdescription);

        ArrayList<Uri> imageList = new ArrayList<Uri>();
        for (String picPath : paths) {
            File f = new File(picPath);
            if (f.exists()) {

//                url = MediaStore.Images.Media.insertImage(context.getContentResolver(), picPath, "12312", "你对图片的描述");
                Uri imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    imageUri = Uri.parse(insertImageToSystem(context, picPath));
                } else {
                    imageUri = Uri.fromFile(f);
                }
                imageList.add(imageUri);
            }
        }

        if (imageList.size() > 0) {
            intent.putExtra(Intent.EXTRA_STREAM, imageList);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(intent);
        } else {
            ToastUtils.showShortToast("文件不存在");
            return false;
        }

        return true;

    }


    /**
     * 分享9图到朋友圈
     *
     * @param context
     * @param Kdescription 9图上边输入框中的文案
     * @param paths        本地图片的路径
     */
    public static boolean share9PicsToWXCircle(Context context, String Kdescription, List<String> paths) {
        if (!isInstallWeChart(context)) {
            ToastUtils.showShortToast("您没有安装微信");
            return false;
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(tencentPk, "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
        intent.setAction(tencentAction);

        ArrayList<Uri> imageList = new ArrayList<>();
        for (String picPath : paths) {
            File f = new File(picPath);
            if (f.exists()) {

//                url = MediaStore.Images.Media.insertImage(context.getContentResolver(), picPath, "12312", "你对图片的描述");
                Uri imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    imageUri = Uri.parse(insertImageToSystem(context, picPath));
                } else {
                    imageUri = Uri.fromFile(f);
                }
                imageList.add(imageUri);
            }
        }
        if (imageList.size() == 0) {
            ToastUtils.showShortToast("文件不存在");
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageList); //图片数据（支持本地图片的Uri形式）
        intent.putExtra("Kdescription", Kdescription); //微信分享页面，图片上边的描述、
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
        return true;
    }


    /**
     * 将照片显示在图库
     *
     * @param context
     * @param imagePath
     * @return
     */
    private static String insertImageToSystem(Context context, String imagePath) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, "aaa", "你对图片的描述");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 不实用微信sdk检查是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isInstallWeChart(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(tencentPk, 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }


}
