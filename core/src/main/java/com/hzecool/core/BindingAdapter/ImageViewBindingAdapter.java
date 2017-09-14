package com.hzecool.core.BindingAdapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hzecool.core.glide.GlideRequestOption;

/**
 * Created by 47066 on 2017/9/14.
 */

public class ImageViewBindingAdapter {
    @BindingAdapter(value = {"imageUrl", "placeHolder", "error"}, requireAll = false)
    /**
     * 不支持mipmap下的图片
     */
    public static void loadImage(ImageView imageView, String url, Drawable holderDrawable, Drawable errorDrawable) {

        Glide.with(imageView.getContext())
                .load(url)
                .apply(GlideRequestOption.getRequestOptions().placeholder(holderDrawable)
                        .error(errorDrawable))
                .into(imageView);
    }
}
