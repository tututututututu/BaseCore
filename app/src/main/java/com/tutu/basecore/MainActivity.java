package com.tutu.basecore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hzecool.core.net.RxHelper;
import com.hzecool.core.net.RxObserver;
import com.tutu.basecore.net.NetProvider;
import com.tutu.basecore.net.RouterBean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NetProvider.requestSlhRouter("13018924230")
                .compose(RxHelper.<RouterBean>handleResult())
                .subscribe(new RxObserver<RouterBean>() {
                    @Override
                    protected void onSuccess(RouterBean routerBean) {

                    }

                    @Override
                    protected void onFail(String msg) {

                    }

                    @Override
                    protected void onNetError(String msg) {

                    }
                });
    }
}
