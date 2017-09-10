package com.hzecool.core.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.hzecool.core.annotation.constans.CACHE_DB_NET;
import static com.hzecool.core.annotation.constans.CACHE_NET_DB;
import static com.hzecool.core.annotation.constans.DB_CACHE_NET;
import static com.hzecool.core.annotation.constans.DB_NET_CACHE;
import static com.hzecool.core.annotation.constans.NET_CACHE_DB;
import static com.hzecool.core.annotation.constans.NET_DB_CACHE;

/**
 * Discribe:用来注解数据获取顺序
 * 比如用了数据库 缓存  那么可能有一下六种组合可能
 * 先加载缓存->数据库->网络 那么就应该使用 CACHE_DB_NET 这种模式
 * Created by tutu on 2017/3/22.
 */


@IntDef({NET_CACHE_DB, NET_DB_CACHE, CACHE_DB_NET, CACHE_NET_DB, DB_CACHE_NET, DB_NET_CACHE})
@Retention(RetentionPolicy.SOURCE)
public @interface DataPriorityInterface {

}
