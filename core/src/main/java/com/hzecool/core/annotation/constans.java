package com.hzecool.core.annotation;

/**
 * Discribe: 数据加载先后顺序
 * Created by tutu on 2017/3/22.
 */

public class constans {
    /**
     * 网络->缓存->DB
     */
    public static final int NET_CACHE_DB = 1;

    /**
     * 网络->DB->缓存
     */
    public static final int NET_DB_CACHE = 2;

    /**
     * 缓存->网络->DB
     */
    public static final int CACHE_NET_DB = 3;

    /**
     * 缓存->DB->网络
     */
    public static final int CACHE_DB_NET = 4;

    /**
     * DB->网络->缓存
     */
    public static final int DB_NET_CACHE = 5;

    /**
     * DB->缓存->网络
     */
    public static final int DB_CACHE_NET = 6;
}
