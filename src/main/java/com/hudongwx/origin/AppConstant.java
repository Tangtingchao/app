package com.hudongwx.origin;

import android.os.Environment;

import java.io.File;

/**
 * 全局静态常量
 */
public class AppConstant {

    //数据缓存目录地址
    public static final String DATA_PATH = AppContext.getContext().getCacheDir().getAbsolutePath() + File.separator + "data";
    //sd卡目录
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "app_cache";
    //日志目录
    public static final String LOG_PATH = SDCARD_PATH + File.separator + "log" + File.separator;
    //网络缓存数据目录
    public static final String NET_DATA_PATH = DATA_PATH + File.separator + "net_cache";
    //app更新下载地址
    public static final String UPDATE_FILE_PATH = SDCARD_PATH + File.separator + "update";


   /* //SP CONFIG
    public static final String KEY_MODE_NO_IMAGE = "no image";
    public static final String KEY_MODE_AUTO_CACHE = "auto cache";

    //DB NAME
    public static final String DB_NAME = "fastapp.realm";

    //HOST URL
    public static final String API_WX_URL = "http://api.tianapi.com";
    public static final String KEY_WX = "71ad915116b186bfa0373f7d880be1c5";

    public static final String API_GAN_URL = "http://gank.io/api/";

    public static final String API_FIR_URL = "http://api.fir.im";
    public static final String TOKEN_FIR = "9e935900ce4683c056f4c59a67f92e7a";
    public static final String KEY_APP_ID = "58023a58959d690fff000aee";*/

}
