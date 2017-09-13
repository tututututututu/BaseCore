package com.hzecool.core.activity.imagepager;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.hzecool.core.R;
import com.hzecool.core.MvpBase.TBaseActivity;
import com.hzecool.core.common.utils.NetworkUtils;
import com.hzecool.core.common.utils.ResourceUtils;
import com.hzecool.core.common.utils.ScreenUtils;
import com.hzecool.core.common.utils.ToastUtils;
import com.hzecool.core.data.AppArouterConstants;
import com.hzecool.widget.utils.GlideSetting;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 图片查看activity
 * 包括dot
 */

@Route(path = AppArouterConstants.AR_URL_VIEW_IMG)
public class ImagePagerActivity extends TBaseActivity<IImagePagerView, ImagePagerPresenter> implements IImagePagerView, View.OnClickListener {
    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_IMGURLS_ARRAY = "imgurls_array";
    public static final String INTENT_POSITION = "position";
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;
    private List<View> guideViewList = new ArrayList<View>();
    private LinearLayout guideGroup;
    public static ImageSize imageSize;
    private View base;

    private OnDownClickListener onDownClickListener;
    private TextView mTvShare;
    private int photoIndex;
    private List<String> mImgUrls;


    public void setOnDownClickListener(OnDownClickListener onDownClickListener) {
        this.onDownClickListener = onDownClickListener;
    }


    public void finishAc() {
        finish();
    }


    @Override
    public int getLayoutID() {
        return R.layout.activity_imagepager;
    }

    @Override
    public void initView() {
        PhotoViewPager viewPager = (PhotoViewPager) findViewById(R.id.pager);
        base = findViewById(R.id.rl_base);
        guideGroup = (LinearLayout) findViewById(R.id.guideGroup);
        mTvShare = (TextView) findViewById(R.id.tv_share);
        mTvShare.setOnClickListener(this);
        int startPos = getIntent().getIntExtra(INTENT_POSITION, 0);


        mImgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);


        ImageAdapter mAdapter = new ImageAdapter(this);
        mAdapter.setDatas(mImgUrls);
        viewPager.setAdapter(mAdapter);
        base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                photoIndex = position;
                for (int i = 0; i < guideViewList.size(); i++) {
                    guideViewList.get(i).setSelected(i == position ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(startPos);

        addGuideView(guideGroup, startPos, mImgUrls);
    }

    @Override
    public void initTitle(ImageView ivBack, TextView tvBack, View llBack, TextView titleName, TextView tvMenu, View titleRoot) {

    }

    @Override
    protected ImagePagerPresenter createPresenter() {
        return new ImagePagerPresenter();
    }

    private void addGuideView(LinearLayout guideGroup, int startPos, List<String> imgUrls) {
        if (imgUrls != null && imgUrls.size() > 0) {
            guideViewList.clear();
            for (int i = 0; i < imgUrls.size(); i++) {
                View view = new View(this);
                view.setBackgroundResource(R.drawable.core_selector_guide_bg);
                view.setSelected(i == startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources()
                        .getDimensionPixelSize(R.dimen.gudieview_width),
                        getResources().getDimensionPixelSize(R.dimen.gudieview_heigh));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_share) {
            if (!NetworkUtils.isConnected()) {
                ToastUtils.showShortToast(R.string.base_netError);
                return;
            }
            if (mImgUrls == null || mImgUrls.size() == 0 || mImgUrls.isEmpty()) {
                ToastUtils.showShortToast(ResourceUtils.getString(R.string.no_picture));
                return;
            }
        }
    }

    /**
     * 按钮在规定的时间内只能点击一次
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    @Override
    public void onLoadData(Object netQRbean) {
    }

    @Override
    public void onEmptyData() {

    }

    @Override
    public void onLoadError(String msg) {
    }

    @Override
    public void onNetError(String msg) {
        ToastUtils.showShortToast(R.string.base_netError);
    }

    private class ImageAdapter extends PagerAdapter {

        private List<String> datas = new ArrayList<String>();
        private LayoutInflater inflater;
        private Context context;

        public void setDatas(List<String> datas) {
            if (datas != null)
                this.datas = datas;
        }

        public ImageAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (datas == null)
                return 0;
            return datas.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container, false);

            if (view != null) {
                final PhotoView imageView = (PhotoView) view.findViewById(R.id.image);

                imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        finishAc();
                    }
                });


                final ProgressBar pro = (ProgressBar) view.findViewById(R.id.pro);

                //预览imageView
                final ImageView smallImageView = new ImageView(context);
                if (imageSize == null) {
                    int screenWidth = ScreenUtils.getScreenWidth();
                    imageSize = new ImageSize(screenWidth, screenWidth);
                }
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize
                        .getHeight());
                layoutParams.gravity = Gravity.CENTER;
                smallImageView.setLayoutParams(layoutParams);
                smallImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ((FrameLayout) view).addView(smallImageView);

                final String imgurl = datas.get(position);


                if (imgurl.startsWith("/")) {
                    Glide.with(context).load(imgurl).apply(GlideSetting.getGlideSetting()).into(imageView);
                } else {
                    pro.setVisibility(View.VISIBLE);
                    pro.setProgress(1);

                    if (!NetworkUtils.isConnected()) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.showShortToast(getString(R.string.base_netError));

                    }
                    Glide.with(context).load(imgurl).apply(GlideSetting.getGlideSetting()).into(imageView);
                }
                imageView.setOnLongClickListener(v -> {
                    if (onDownClickListener != null) {
                        onDownClickListener.onDownClick(imgurl);
                    }
                    return false;
                });
                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

    }


    public interface OnDownClickListener {
        void onDownClick(String path);
    }
}
