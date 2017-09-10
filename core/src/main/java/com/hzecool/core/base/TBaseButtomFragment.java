package com.hzecool.core.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzecool.core.R;
import com.hzecool.core.common.utils.KeyboardUtils;
import com.hzecool.core.log.L;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.internal.disposables.ListCompositeDisposable;


/**
 * fragmenet底部弹出基类
 * view绑定 title初始化
 * presenter绑定
 */
public abstract class TBaseButtomFragment<V extends TIBaseView, T extends TBasePresenter<V>> extends DialogFragment implements DialogInterface.OnKeyListener{
    private static final String TAG = "TBaseButtomFragment";

    private static final float DEFAULT_DIM = 0.2f;

    /**
     * Butterknife
     */
    private Unbinder bind;
    private View rootView;

    /**
     * 获取布局文件ID
     *
     * @return
     */
    public abstract int getLayoutID();

    //view初始化完成  可以在这里操作view
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
    public abstract void initTitle(ImageView ivBack, TextView tvBack,View llBack, TextView titleName, TextView tvMenu, View titleRoot);

    /**
     * 创建Presenter 每个fragment 都必须实现此方法并返回正确的presenter
     *
     * @return
     */
    protected abstract T createPresenter();

    /**
     * 键盘管理
     */
    private InputMethodManager imm;

    /**
     * 当前fragment的activity
     */
    protected Context mActivityContext;

    /**
     * RxBus事件订阅者容器
     * 所有的事件订阅者都应该放在这个容器当中
     * 已保证不会发生泄漏
     */

    protected ListCompositeDisposable mDisposableContainer;

    TextView titleName;
    ImageView ivBack;
    TextView tvMenu;
    View titleRoot;
    TextView tvBack;
    View llBack;


    protected T mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);
        //创建Presenter
        mPresenter = createPresenter();
        //内存泄漏
        //关联View
        mPresenter.attachView((V) this);
        mDisposableContainer = new ListCompositeDisposable();
        mActivityContext = getContext();
        L.logFile("创建fragment:" + getClass().getSimpleName());

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        rootView = inflater.inflate(getLayoutID(), null);
        titleName = (TextView) rootView.findViewById(R.id.tv_titel);
        ivBack = (ImageView) rootView.findViewById(R.id.iv_back);
        tvMenu = (TextView) rootView.findViewById(R.id.tv_menu);
        titleRoot = rootView.findViewById(R.id.title_root);
        llBack = rootView.findViewById(R.id.ll_back);
        tvBack = (TextView) rootView.findViewById(R.id.tv_back);

        bind = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initTitle(ivBack,tvBack,llBack,titleName,tvMenu,titleRoot);
        mPresenter.start();
    }


    @Override
    public void onStart() {
        super.onStart();

        if (getDialog()==null){
            this.dismiss();
            return;
        }

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.dimAmount = getDimAmount();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (getHeight() > 0) {
            params.height = getHeight();
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        params.gravity = Gravity.BOTTOM;

        window.setAttributes(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
        //解绑view
        mPresenter.detachView();
        //接触订阅关系
        mDisposableContainer.dispose();
        L.logFile("销毁fragment:" + getClass().getSimpleName());
    }


    /**
     * 隐蔽软键盘
     *
     * @param viewList viewList 中需要放的是当前界面所有触发软键盘弹出的控件,比如两个EditText
     */
    protected final void hideKeyBoard(List<View> viewList) {
        KeyboardUtils.hideSoftKeyboard(mActivityContext, viewList);
    }

    /**
     * 显示软键盘
     */
    protected final void showKeyBoard(View focus) {
        if (mActivityContext != null && (mActivityContext instanceof Activity)) {
            if (null == imm) {
                imm = (InputMethodManager) mActivityContext
                        .getApplicationContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
            }
            if (null == focus) {
                focus = ((Activity) mActivityContext).getCurrentFocus();
            }
            if (null != focus) {
                imm.showSoftInput(focus, InputMethodManager.SHOW_FORCED);
            }
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            L.logFile("隐藏fragment:" + getClass().getSimpleName());
        } else {
            L.logFile("显示fragment:" + getClass().getSimpleName());
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            dismiss();
            return true;
        }else {
            return false;
        }
    }

    public View getRootView(){
        return rootView;
    }

    public int getHeight() {
        return -1;
    }

    public float getDimAmount() {
        return DEFAULT_DIM;
    }

    public boolean getCancelOutside() {
        return true;
    }

    public String getFragmentTag() {
        return TAG;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, getFragmentTag());
    }
}
