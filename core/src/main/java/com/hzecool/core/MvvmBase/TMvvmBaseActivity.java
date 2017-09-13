package com.hzecool.core.MvvmBase;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hzecool.core.R;
import com.hzecool.core.base.TAbsActivity;
import com.hzecool.core.log.L;
import com.hzecool.widget.materialdialog.MaterialDialog;

import io.reactivex.internal.disposables.ListCompositeDisposable;

/**
 * Created by 47066 on 2017/9/13.
 */

public abstract class TMvvmBaseActivity<VB extends ViewDataBinding> extends TAbsActivity {
    private VB mBinding;

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

    /**
     * 当前activity对象
     * 提供给fragment使用
     */
    protected Context mBaseActivityContext;

    /**
     * 订阅者容器
     * 所有的activity中的订阅者都应该放在此容器中
     * 已保证不会被泄漏
     */
    protected ListCompositeDisposable mDisposableContainer;

    /**
     * 进度框
     */
    public MaterialDialog mProgressDialog;

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
        mBinding = DataBindingUtil.setContentView(this,getLayoutID());

        mTvTitleName = findViewById(R.id.tv_titel);
        mIvBack = findViewById(R.id.iv_back);
        mTvMenu = findViewById(R.id.tv_menu);
        mViewTitleRoot = findViewById(R.id.title_root);
        mViewLlBack = findViewById(R.id.ll_back);
        mTvBack = findViewById(R.id.tv_back);

        mDisposableContainer = new ListCompositeDisposable();
        mBaseActivityContext = this;

        initView();

        if (mViewLlBack != null) {
            mViewLlBack.setOnClickListener(v -> finish());
        }

        initTitle(mIvBack, mTvBack, mViewLlBack, mTvTitleName, mTvMenu, mViewTitleRoot);
        L.logFile("创建activity=====" + getClass().getSimpleName());
    }


    /**
     * 显示加在框
     *
     * @param progress 是否需要准确的进度 true 需要     false不需要
     * @param msg      内容字符串
     */
    public void showLoadingDialog(boolean progress, boolean cancelable, String msg) {
        if (progress && mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(this).progress(false, 100).cancelable(cancelable).build();
        } else if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(this).progress(true, 0).cancelable(cancelable).build();
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog.setContent(msg);
        mProgressDialog.show();
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
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        hideKeyBoard();

        //解除观察者的订阅关系
        if (mDisposableContainer != null) {
            mDisposableContainer.dispose();
        }

        //存在进度框并且在显示状态 关闭它
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        L.logFile("退出activity=====" + getClass().getSimpleName());
        super.onDestroy();
    }
}
