package com.hzecool.core.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Discribe:集合转换工具类
 * Created by tutu on 2017/3/17.
 */

public class ListConvertUtils {

    /**
     * 将list转Array[]
     * @param arrays
     * @param <T>
     * @return
     */
    public static <T> List<T> list2Array(T[] arrays) {

        List<T> list = new ArrayList<>();
        if (arrays == null) {
            return list;
        }
        for (T array : arrays) {
            list.add(array);
        }

        return list;
    }

}
