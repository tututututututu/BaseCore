package com.tutu.basecore;

import com.hzecool.core.base.BaseApp;
import com.hzecool.core.init.CoreInit;

/**
 * Created by tutu on 2017/9/10.
 */

public class App extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();

        CoreInit.getInstance().setBaseUrl("http://assist.hzdlsoft.com:7086/assist/");
    }
}
