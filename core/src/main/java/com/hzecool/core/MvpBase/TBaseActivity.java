package com.hzecool.core.MvpBase;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hzecool.core.R;
import com.hzecool.core.base.TAbsActivity;
import com.hzecool.core.log.L;
import com.hzecool.widget.materialdialog.MaterialDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.internal.disposables.ListCompositeDisposable;


/**
 * Activity基类  所有的应用中的activity都应该继承该类
 * 该类实现了一些常用方法
 * dialog
 * rxBus事件容器
 * view和presneter的绑定 解绑
 * Created by tutu on 17/9/13.
 */
public abstract class TBaseActivity<V extends TIBaseView, T extends TBasePresenter<V>> extends TAbsActivity {

    /**
     * butterknife绑定
     */
    private Unbinder unbinder;

    /**
     * 返回布局文件id
     *
     * @return
     */
    public abstract int getLayoutID();

    /**
     * 控件初始化完成 在这个方法中可以使用控件了
     */
    public abstract void initView();

    /**
     * 初始化标题栏
     *
     * @param ivBack    返回图标ImageView
     * @param tvBack    返回文字TextView
     * @param titleName 标题栏TextView
     * @param tvMenu    菜单TextView
     * @param titleRoot 整个布局View
     */
    public abstract void initTitle(ImageView ivBack, TextView tvBack, View llBack, TextView titleName, TextView tvMenu, View titleRoot);

    protected abstract T createPresenter();

    /**
     * 当前activity对象
     * 提供给fragment使用
     */
    protected Context mBaseActivityContext;

    /**
     * app级别的Context
     */
    protected Context mApplicationContext;

    /**
     * 订阅者容器
     * 所有的activity中的订阅者都应该放在此容器中
     * 已保证不会被泄漏
     */
    protected ListCompositeDisposable mDisposableContainer;

    /**
     * 进度框
     */
    public MaterialDialog progressDialog;

    /**
     * presenter 不能为空
     */
    protected T mPresenter;

    private TextView mTvTitleName;
    private ImageView mIvBack;
    private TextView mTvMenu;
    private View mViewTitleRoot;
    private TextView mTvBack;
    private View mViewLlBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);

        setContentView(getLayoutID());
        unbinder = ButterKnife.bind(this);

        //创建Presenter
        mPresenter = createPresenter();
        //内存泄漏
        //关联View
        mPresenter.attachView((V) this);

        mTvTitleName = findViewById(R.id.tv_titel);
        mIvBack = findViewById(R.id.iv_back);
        mTvMenu = findViewById(R.id.tv_menu);
        mViewTitleRoot = findViewById(R.id.title_root);
        mViewLlBack = findViewById(R.id.ll_back);
        mTvBack = findViewById(R.id.tv_back);

        mDisposableContainer = new ListCompositeDisposable();

        mBaseActivityContext = this;
        mApplicationContext = getApplicationContext();
        initView();

        if (mViewLlBack != null) {
            mViewLlBack.setOnClickListener(v -> finish());
        }

        initTitle(mIvBack, mTvBack, mViewLlBack, mTvTitleName, mTvMenu, mViewTitleRoot);

        mPresenter.start();
        L.logFile("创建activity=====" + getClass().getSimpleName());
    }

    protected void updateTitleName(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTvTitleName.setText(title);
        }
    }

    /**
     * 显示加在框
     *
     * @param progress 是否需要准确的进度 true 需要     false不需要
     * @param msg      内容字符串
     */
    public void showLoadingDialog(boolean progress, boolean cancelable, String msg) {
        if (progress && progressDialog == null) {
            progressDialog = new MaterialDialog.Builder(this).progress(false, 100).cancelable(cancelable).build();
        } else if (progressDialog == null) {
            progressDialog = new MaterialDialog.Builder(this).progress(true, 0).cancelable(cancelable).build();
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.setContent(msg);
        progressDialog.show();
    }

    /**
     * 显示加在框
     *
     * @param progress 是否需要准确的进度 true 需要     false不需要
     * @param ids      内容资源文件
     */
    public void showLoadingDialog(boolean progress, boolean cancelable, int ids) {
        showLoadingDialog(progress, cancelable, getString(ids));
    }

    public void cancelLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        hideKeyBoard();
        //解除butterknife绑定
        if (unbinder != null) {
            unbinder.unbind();
        }
        //解除presenter和view的绑定
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        //解除观察者的订阅关系
        if (mDisposableContainer != null) {
            mDisposableContainer.dispose();
        }

        //存在进度框并且在显示状态 关闭它
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        L.logFile("退出activity=====" + getClass().getSimpleName());
        super.onDestroy();
    }
}
