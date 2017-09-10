package com.hzecool.core.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tutu on 2017/4/14.
 */

public class ImageUrlUtils {

    /**
     * 获取图片地址
     *
     * @param url    地址url
     * @param id     当 orign 为true的时候取的是大图传sessionId   false的时候是缩略图传epid
     * @param fileId 文件id
     * @param orign  是否是原图
     * @return 图片地址
     */
    public static String getImageUrl(String url, String id, String fileId, boolean orign) {

        /**
         * 保证图片清晰度全部使用原图  glide自己裁剪
         */
        orign = true;

        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (orign) {
            sb.append("readFile.do?&sessionId=");
        } else {
            sb.append("readFile.do?thumbflag=1&sessionId=");
        }

        sb.append(id).append("&fileid=").append(fileId);

        return sb.toString();
    }

    /**
     * 根据string 转 一个完整的list
     *
     * @param url
     * @param
     * @return
     */
    public static ArrayList<String> getImageUrlList(String url, String fileId,String id,boolean orign) {

        ArrayList<String> arrayList = new ArrayList<>();
        for (String s : SpliteStringUtils.getStringList(",", fileId)) {
            arrayList.add(ImageUrlUtils.getImageUrl(url,id,s,orign));
        }

        return arrayList;
    }


    /**
     * 根据fileId的一个list 转 一个完整的Url list
     *
     * @param url
     * @param
     * @return
     */
    public static ArrayList<String> getImageUrlList(String url, List<String> fileId, String id, boolean orign) {

        ArrayList<String> arrayList = new ArrayList<>();
        for (String s : fileId) {
            arrayList.add(ImageUrlUtils.getImageUrl(url,id,s,orign));
        }
        return arrayList;
    }
}
