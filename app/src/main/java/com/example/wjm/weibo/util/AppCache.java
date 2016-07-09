package com.example.wjm.weibo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by Wjm on 2016/6/30.
 */
public class AppCache {
    private static String TAG=AppCache.class.getSimpleName();

    public static Bitmap getCachedImage(Context context,String url){
        String cacheKey=AppUtil.md5(url);
        Bitmap cachedImage=SDUtil.getImage(cacheKey);
        if(cachedImage!=null){
            Log.w(TAG,"get cached image");
            return cachedImage;
        }else {
            Bitmap newImage=IOUtil.getBitmapRemote(context,url);
            SDUtil.saveImage(newImage,cacheKey);
            return newImage;
        }
    }

    public static Bitmap getImage(String url){
        String cachedKey=AppUtil.md5(url);
        return SDUtil.getImage(cachedKey);
    }
}
