package com.hzecool.core.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hzecool.core.R;
import com.hzecool.core.common.utils.ConvertUtils;
import com.hzecool.core.common.utils.ResourceUtils;
import com.hzecool.core.common.utils.SPUtils;
import com.hzecool.core.common.utils.Utils;
import com.hzecool.core.log.L;
import com.hzecool.core.log.LocalLogManager;
import com.hzecool.core.ActivityManager.ActivityStack;
import com.hzecool.core.sp.FinalSPOperation;
import com.hzecool.widget.loadingLayout.LoadingLayout;
import com.lzy.okgo.OkGo;
import com.squareup.leakcanary.LeakCanary;

import static com.hzecool.core.data.AppData.LOG_DEBUG;

/**
 * BaseApp core层Application
 * Created by tutu on 2017/3/3.
 */

public class BaseApp extends Application {
    private static BaseApp myApplication = null;

    public static BaseApp getApplication() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        //初始化utils中的Context
        Utils.init(this);
        //初始化崩溃收集
//        CrashHander.install(this);

        //内存泄漏检测
        if (LOG_DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            }
            LeakCanary.install(this);
        }

        //日志开关
        L.initLoger(LOG_DEBUG);
        //初始化日志管理
        initLocalLog();
        //初始化数据库
        initDB();
        //初始化SP  清理缓存会删除掉的sp
        SPUtils.initSP("appData");
        //清理缓存不会删除的sp
        FinalSPOperation.initSP("finalData");
        //初始化OKgo
        OkGo.init(this);

        //初始化路由
        initArouter();
        //初始化 空布局
        initLoadingLayout();
    }

    private void initDB() {
        //DaoManager.getInstance().setupDatabase(this, LOG_DEBUG);
    }


    private void initLocalLog() {
        LocalLogManager.initLog(this);
    }


    private void initArouter() {
        if (LOG_DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }


    public static void exit() {
        ActivityStack.finishAll();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }


    public static void initLoadingLayout() {
        LoadingLayout.getConfig()
                .setErrorText(ResourceUtils.getString(R.string.base_load_error))
                .setEmptyText(ResourceUtils.getString(R.string.base_no_data))
                .setNoNetworkText(ResourceUtils.getString(R.string.base_netError))
                .setErrorImage(R.mipmap.search_no_result)
                .setEmptyImage(R.mipmap.search_no_result)
                .setNoNetworkImage(R.mipmap.search_no_result)
                .setAllTipTextColor(R.color.base_slh_text_gray)
//                .setAllTipTextSize(ConvertUtils.px2sp(14))
                .setReloadButtonText(ResourceUtils.getString(R.string.click_retry))
//                .setReloadButtonTextSize(ConvertUtils.px2sp(14))
                .setReloadButtonTextColor(R.color.base_slh_text_normal)
                .setReloadButtonWidthAndHeight(ConvertUtils.px2dp(150), ConvertUtils.px2dp(40));
    }

}
