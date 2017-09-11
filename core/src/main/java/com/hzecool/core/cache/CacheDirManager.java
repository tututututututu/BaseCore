package com.hzecool.core.cache;

import android.os.Environment;
import android.support.annotation.IntDef;

import com.hzecool.core.common.utils.Utils;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.hzecool.core.common.utils.FileUtils.deleteDir;

/**
 * Discribe:app缓存路径管理类
 * Created by tutu on 2017/3/17.
 */

public class CacheDirManager {
    /**
     * 分享缓存
     */
    public static final int SHARE_CACHE_PATH = 1;

    /**
     * 一般通用缓存
     */
    public static final int COMMON_CACHE_PATH = 2;

    /**
     * 日志缓存
     */
    public static final int LOG_CACHE_PATH = 3;

    /**
     * 数据库缓存
     */
    public static final int DB_CACHE_PATH = 4;

    /**
     * 图片缓存
     */
    public static final int IMG_CACHE_PATH = 5;

    @IntDef({SHARE_CACHE_PATH, COMMON_CACHE_PATH, LOG_CACHE_PATH, DB_CACHE_PATH, IMG_CACHE_PATH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheType {
    }

    /**
     * 获取app缓存目录
     *
     * @return
     */
    public static String getAppCachePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Utils.getContext().getExternalCacheDir().getAbsolutePath();
        } else {
            return Utils.getContext().getCacheDir().getAbsolutePath();
        }
    }


    /**
     * 获取不同类型缓存的目录
     *
     * @param cacheType
     * @return
     */
    public static String getTyleCachePath(@CacheType int cacheType) {
        switch (cacheType) {
            case COMMON_CACHE_PATH:
                return getAppCachePath() + File.separator + "commonCache";

            case SHARE_CACHE_PATH:
                return getAppCachePath() + File.separator + "shareCache";

            case LOG_CACHE_PATH:
                return getAppCachePath() + File.separator + "logCache";

            case DB_CACHE_PATH:
                return getAppCachePath() + File.separator + "dbCache";

            case IMG_CACHE_PATH:
                return getAppCachePath() + File.separator + "imgCache";
        }
        return null;
    }


    /**
     * 清除一般缓存目录
     * 包括通用缓存,分享下载的图片缓存
     */
    public static void clearCommonCache() {
        clearSpecifiedCache(COMMON_CACHE_PATH);
        clearSpecifiedCache(SHARE_CACHE_PATH);
        clearSpecifiedCache(IMG_CACHE_PATH);
    }

    /**
     * 清除日志缓存
     */
    public static void clearLogCache() {
        clearSpecifiedCache(LOG_CACHE_PATH);
    }

    public static void clearDbCacje() {
        clearSpecifiedCache(DB_CACHE_PATH);
    }


    public static void clearSpecifiedCache(@CacheType int cacheType) {
        switch (cacheType) {
            case COMMON_CACHE_PATH:
                deleteDir(getTyleCachePath(COMMON_CACHE_PATH));

            case SHARE_CACHE_PATH:
                deleteDir(getTyleCachePath(SHARE_CACHE_PATH));

            case LOG_CACHE_PATH:
                deleteDir(getTyleCachePath(LOG_CACHE_PATH));

            case DB_CACHE_PATH:
                deleteDir(getTyleCachePath(DB_CACHE_PATH));

            case IMG_CACHE_PATH:
                deleteDir(getTyleCachePath(IMG_CACHE_PATH));
        }
    }
}
