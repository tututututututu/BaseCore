package com.hzecool.core.log;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tutu on 2017/1/1.
 * 日志类
 */

public class L {
    public static boolean isDebug = true;

    public static final String DEFAULT_LOG_TAG = "StaffAssistant";

    public static void initLoger(boolean debug) {
        Logger
                .init("logger")                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(1);               // default 0
        //.logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
        isDebug = debug;
    }

    public static void e(String message, Object... args) {
        if (isDebug) {
            Logger.e("response=" + message, args);
        }
        logFile(message);

    }

    public static void i(String message, Object... args) {
        if (isDebug) {
            Logger.i("response=" + message, args);
        }
    }

    public static void w(String message, Object... args) {
        Logger.w("response=" + message, args);
    }

    public static void v(String message, Object... args) {
        Logger.v("response=" + message, args);
    }

    public static void json(String json) {
        if (isDebug) {
            Logger.e("response json=");
            Logger.json(json);
        }
    }

    public static void d(String message, Object... args) {
        if (isDebug) {
            Logger.d("response=" + message, args);
        }
    }

    /**
     * 以下带r的都是请求日志，上面都是返回日志
     *
     * @param message
     * @param args
     */
    public static void er(String message, Object... args) {
        if (isDebug) {
            Logger.e("request=" + message, args);
        }
        logFile(message);
    }

    public static void ir(String message, Object... args) {
        if (isDebug) {
            Logger.i("request=" + message, args);
        }
    }

    public static void wr(String message, Object... args) {
        if (isDebug) {
            Logger.w("request=" + message, args);
        }
    }

    public static void vr(String message, Object... args) {
        if (isDebug) {
            Logger.v("request=" + message, args);
        }
    }

    public static void jsonr(String json) {
        if (isDebug) {
            Logger.e("request json=");
            Logger.json(json);
        }
    }

    public static void dr(String message, Object... args) {
        if (isDebug) {
            Logger.d("request=" + message, args);
        }
    }

    public static void logStackTraceToFile(Throwable e){
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw =  new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        logFile(sw.toString());
    }

    public static void logFile(String message) {
        if (isDebug) {
            Logger.t("logfile").i(message);
        }

        StringBuffer sb = new StringBuffer();

        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {

            if (i == 1 || i == 2) {
                sb.append("文件名:")
                        .append(stackTraceElements[i].getFileName())
                        .append("行号:")
                        .append(stackTraceElements[i].getLineNumber())
                        .append("方法名:")
                        .append(stackTraceElements[i].getMethodName())
                        .append("\n")
                        .append("信息:");
            }
        }

        LocalLogManager.getInstanse().cacheLog(sb.toString() + message);
    }

    public static void iTag(String tag, String message) {
        if (isDebug) {
            Logger.t(tag).i(message);
        }
    }

    public static void eTag(String tag, String message) {
        if (isDebug) {
            Logger.t(tag).e(message);
        }
    }

    public static void wTag(String tag, String message) {
        if (isDebug) {
            Logger.t(tag).w(message);
        }
    }

}
