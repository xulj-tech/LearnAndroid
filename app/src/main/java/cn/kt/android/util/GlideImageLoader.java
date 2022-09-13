package cn.kt.android.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object o, ImageView imageView) {
        Glide.with(context).load(o).into(imageView);
    }
}