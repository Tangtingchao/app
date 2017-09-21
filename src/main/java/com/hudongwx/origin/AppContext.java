package com.hudongwx.origin;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * APP 上下文对象,必须要在application中做初始化操作
 */
public final class AppContext {

    private static Application instance;
    private static Resources mResources;

    public static Application init(Application application){
        instance = application;
        mResources = instance.getResources();
        return instance;
    }

    public static Context getContext() {
        if(instance == null)
            throw new IllegalStateException("application 类没有初始化调用AppContext.init(...)");
        return instance;
    }

    public static Resources getResource(){
        return mResources;
    }

    public static String string(int id) {
        return instance.getString(id);
    }


}
