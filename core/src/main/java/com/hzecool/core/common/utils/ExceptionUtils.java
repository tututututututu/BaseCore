package com.hzecool.core.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tutu on 2017/7/31.
 */

public class ExceptionUtils {
    public static String getExceptionString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String strs = sw.toString();
        return strs;
    }

    public static String getExceptionString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String strs = sw.toString();
        return strs;
    }
}
