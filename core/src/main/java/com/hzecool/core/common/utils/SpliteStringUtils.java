package com.hzecool.core.common.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理用分隔符分割 的字符串
 * Created by tutu on 2017/4/21.
 */

public class SpliteStringUtils {

    /**
     * 获取用split 分割的数据  转化为list
     *
     * @param split 分割符
     * @param data  原始String
     * @return 返回的String list 可能为空
     */
    public static List<String> getStringList(String split, String data) {
        List<String> retList = new ArrayList<>();

        if (TextUtils.isEmpty(data)) {
            return retList;
        }

        String splitStr[] = data.split(split);
        if (splitStr.length == 0) {
            return retList;
        }


        for (String s : splitStr) {
            retList.add(s);
        }

        return retList;
    }

    public static String getImgUrl(String str) {
        List<String> retList = SpliteStringUtils.getStringList(",", str);
        return retList.isEmpty() ? "" : retList.get(0);
    }

    /**
     * 根据url的Liist转为带,的fileids
     *
     * @param imgUrls
     * @return
     */
    public static String genFileIds(List<String> imgUrls) {
        StringBuilder sb = new StringBuilder();
        if (imgUrls == null || imgUrls.isEmpty()) {
            return "";
        }

        for (String imgUrl : imgUrls) {
            sb.append(imgUrl)
                    .append(",");
        }

        return sb.substring(0, sb.length() - 1).toString();
    }
}
