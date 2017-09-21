package com.hudongwx.origin.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.hudongwx.origin.AppContext;

/**
 * ui工具类
 */
public final class UIUtil {

    private static float displayDensity = 0.0F;

    //是否是大屏幕
    private static Boolean sHasBigScreen = null;

    private UIUtil(){
    }

    public static float dpToPixel(float dp) {
        return dp * (getDisplayMetrics().densityDpi / 160F);
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) AppContext.getContext().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }

    public static float getDensity() {
        if (displayDensity == 0.0)
            displayDensity = getDisplayMetrics().density;
        return displayDensity;
    }

    public static float getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static float getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static float pxToDp(float f) {
        return f / (getDisplayMetrics().densityDpi / 160F);
    }

    public static boolean hasBigScreen() {
        boolean flag = true;
        if (sHasBigScreen == null) {
            boolean flag1;
            if ((0xf & AppContext.getContext().getResources()
                    .getConfiguration().screenLayout) >= 3)
                flag1 = flag;
            else
                flag1 = false;
            Boolean boolean1 = Boolean.valueOf(flag1);
            sHasBigScreen = boolean1;
            if (!boolean1.booleanValue()) {
                if (getDensity() <= 1.5F)
                    flag = false;
                sHasBigScreen = Boolean.valueOf(flag);
            }
        }
        return sHasBigScreen.booleanValue();
    }


}
