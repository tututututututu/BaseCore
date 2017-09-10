package com.hzecool.core.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;

/**
 * Created by tutu on 2016/12/31.
 */

public class ResourceUtils {
    public static String getString(int resID) {
        return Utils.getContext().getResources().getString(resID);
    }

    public static Resources getResources(){
        return Utils.getContext().getResources();
    }

    public static int getDimen(int resID) {
        return (int) Utils.getContext().getResources().getDimension(resID);
    }

    public static int getColor(int resID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Utils.getContext().getResources().getColor(resID, Utils.getContext().getTheme());
        } else {
            return Utils.getContext().getResources().getColor(resID);
        }
    }

    /**
     * 获取drawable
     * @param drawableResId
     * @return
     */
    public static Drawable getDrawable(@DrawableRes int drawableResId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Utils.getContext().getResources().getDrawable(drawableResId, Utils.getContext().getTheme());
        } else {
            return Utils.getContext().getResources().getDrawable(drawableResId);
        }
    }

    /**
     * 根据color资源获取color id
     *
     * @param context
     * @param resID
     * @return
     */
    public static int getColor(Context context, int resID) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(resID, context.getTheme());
        } else {
            return context.getResources().getColor(resID);
        }
    }
}

