package com.hudongwx.origin.dbinding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hudongwx.origin.utils.ImgLoad;

/**
 * 自定义图片绑定
 */
public class ImageBindingAdapter {
    /**
     * @param view
     * @param url 绑定地址
     * @param error 下载失败图片
     */
    @BindingAdapter(value = {"binding:url","binding:error"})
    public static void bindingImg(ImageView view, String url, Drawable error){
        ImgLoad.load(view,url,error);
    }

    /**
     *
     * @param view
     * @param url 下载地址
     * @param place 默认显示的图片
     * @param error 下载错误图片
     */
    @BindingAdapter(value = {"binding:url","binding:place","binding:error"})
    public static void bindingImg(ImageView view,String url,Drawable place,Drawable error){
        ImgLoad.load(view,url,error);
    }

    @BindingAdapter(value = {"binding:url"},requireAll = true)
    public static void bindingImg(ImageView view, String url){
        ImgLoad.load(view,url);
    }

    @BindingAdapter(value = {"binding:drawableTop"},requireAll = true)
    public static void bindingImg(final TextView view, String url){
        ImgLoad.load(view, url, new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, GlideAnimation<? super Drawable> glideAnimation) {
                view.setCompoundDrawables(null,null,null,resource);
            }
        });
    }

}
