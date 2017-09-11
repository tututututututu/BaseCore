package com.hzecool.core.crash;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;

import com.hzecool.core.cache.CacheDirManager;
import com.hzecool.core.common.utils.AppUtils;
import com.hzecool.core.common.utils.CloseUtils;
import com.hzecool.core.common.utils.FileUtils;
import com.hzecool.core.common.utils.ResourceUtils;
import com.hzecool.core.common.utils.ToastUtils;
import com.hzecool.core.common.utils.Utils;
import com.hzecool.core.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 崩溃相关工具类
 */
public class CrashHandler
        implements UncaughtExceptionHandler {

    private volatile static CrashHandler mInstance;

    private UncaughtExceptionHandler mHandler;
    private boolean mInitialized;
    private String crashDir;
    private String versionName;
    private int versionCode;

    private CrashHandler() {
    }

    /**
     * 获取单例
     * <p>在Application中初始化{@code CrashHandler.getInstance().init(this);}</p>
     *
     * @return 单例
     */
    public static CrashHandler getInstance() {
        if (mInstance == null) {
            synchronized (CrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new CrashHandler();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean init() {
        if (mInitialized) return true;

        crashDir = CacheDirManager.getTyleCachePath(CacheDirManager.LOG_CACHE_PATH) + File.separator + "crash" + File.separator;

        try {
            PackageInfo pi = Utils.getContext().getPackageManager().getPackageInfo(Utils.getContext().getPackageName
                    (), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        String now = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        final String fullPath = crashDir + "crash" + ".log";
        if (!FileUtils.createOrExistsFile(fullPath)) return;
        new Thread(() -> {
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileWriter(fullPath, true));
                pw.write(now + AppUtils.getPhoneSysInfo());
                throwable.printStackTrace(pw);
                Throwable cause = throwable.getCause();
                while (cause != null) {
                    cause.printStackTrace(pw);
                    cause = cause.getCause();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                CloseUtils.closeIO(pw);
            }
        }).start();

        Looper.getMainLooper();
        ToastUtils.showShortToast(ResourceUtils.getString(R.string.core_app_crash));
        Looper.loop();

        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }

        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
    }


}
