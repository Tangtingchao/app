package com.hudongwx.origin.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.hudongwx.origin.AppContext;

/**
 * 图片加载器
 */
public final class ImgLoad {

    private Object with;//用于Glide.with()

    public ImgLoad(Fragment with){
        this.with = with;
    }

    public ImgLoad(FragmentActivity with){
        this.with = with;
    }

    public static void load(ImageView view, String url){
        Glide.with(view.getContext()).
                load(url)
                .into(view);
    }
    public static void load(ImageView view, String url,boolean tags){
        Glide.with(view.getContext()).
                load(url)
                .into(view);
    }
    public static void load(ImageView view, String url, Drawable error){
        Glide.with(view.getContext())
                .load(url)
                .error(error)
                .into(view);
    }

    public static void load(ImageView view, String url, int errorId){
        Glide.with(view.getContext()).
                load(url)
                .error(errorId)
                .into(view);
    }

    public static void load(ImageView view,ImgLoad load,String url){
        RequestManager glide = null;
        if(load.with instanceof Fragment){
            glide = Glide.with((Fragment)load.with);
        }else if(load.with instanceof FragmentActivity){
            glide = Glide.with((FragmentActivity)load.with);
        }else{
            glide = Glide.with(view.getContext());
        }
        glide.load(url)
                .into(view);
    }

    /**
     * 自动判断当前加载的with
     * @param view
     * @param load
     * @param url
     * @param error
     */
    public static void load(ImageView view,ImgLoad load,String url,Drawable error){
        RequestManager glide = null;
        if(load.with instanceof Fragment){
            glide = Glide.with((Fragment)load.with);
        }else if(load.with instanceof FragmentActivity){
            glide = Glide.with((FragmentActivity)load.with);
        }else{
            glide = Glide.with(view.getContext());
        }
        glide.load(url)
                .error(error)
                .into(view);
    }

    public static void load(final TextView view,String url,SimpleTarget simpleTarget){
        Glide.with(view.getContext()).load(url).into(simpleTarget);
    }

    public static void load(Context context,String url,ImageView img){
        Glide.with(context)
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(img);
    }

}
