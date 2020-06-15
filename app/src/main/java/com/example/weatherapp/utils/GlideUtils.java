package com.example.weatherapp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideUtils {
    private static final String TAG = GlideUtils.class.getSimpleName();
    public  static  void uploadIcon(Context context, ImageView imageView, String icoName){
        String url = String.format("https://openweathermap.org/img/wn/%s@2x.png", icoName);
        Log.d(TAG, "got url");

        Glide.with(context).load(url).centerCrop().into(imageView);
    }
}
