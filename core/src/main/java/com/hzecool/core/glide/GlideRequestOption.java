package com.hzecool.core.glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by 47066 on 2017/9/14.
 */

public class GlideRequestOption {
    private static RequestOptions requestOptions;

    public static RequestOptions getRequestOptions(){
        if (requestOptions==null){
            requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }

        return requestOptions;
    }
}
