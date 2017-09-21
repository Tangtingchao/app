package com.hudongwx.origin.utils;

import android.os.StrictMode;

import com.orhanobut.logger.Logger;
import com.hudongwx.origin.BuildConfig;

public final class Log {

    public static boolean DEBUG = BuildConfig.DEBUG;
    public static final String TAG = "一元人人购";

    private Log() {
    }

    public static void println(String msg) {
        if (DEBUG && null != msg) {

        }
    }

    public static void print(String msg) {
        if (DEBUG && null != msg) {
        }
    }


    public static void i(String msg) {
        i(TAG, msg);
    }


    public static void i(String tag, String msg) {
        if (DEBUG && null != tag && null != msg) {
            Logger.i(tag, msg);
        }
    }


    public static void e(String msg) {
        e(TAG, msg);
    }


    public static void e(String tag, String msg) {
        if (DEBUG && null != tag && null != msg) {
            Logger.e(tag, msg);
        }
    }


    public static void w(String msg) {
        w(TAG, msg);
    }


    public static void w(String tag, String logInfo) {
        if (DEBUG && null != tag && null != logInfo) {
            Logger.w(tag, logInfo);
        }
    }


    public static void d(String msg) {
        d(TAG, msg);
    }


    public static void d(String tag, String msg) {
        if (DEBUG && null != tag && null != msg) {
            Logger.d(tag, msg);
        }
    }


    public static void v(String msg) {
        v(TAG, msg);
    }


    public static void v(String tag, String msg) {
        if (DEBUG && null != tag || null != msg) {
            Logger.v(tag, msg);
        }
    }


    public static void wtf(String msg) {
        wtf(TAG, msg);
    }


    public static void wtf(String tag, String logInfo) {
        if (DEBUG && null != tag && null != logInfo) {
            Logger.wtf(tag, logInfo);
        }
    }


    public static void printStackInfo() {
        printStackInfo(null);
    }

    public static void printStackInfo(String info) {
        if (DEBUG) {
            StringBuffer strBuffer = new StringBuffer();
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            strBuffer.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\r")
                    .append("class:    ").append(stackTrace[1].getClassName()).append("\n\r")
                    .append("method:   ").append(stackTrace[1].getMethodName()).append("\n\r")
                    .append("number:   ").append(stackTrace[1].getLineNumber()).append("\n\r")
                    .append("fileName: ").append(stackTrace[1].getFileName()).append("\n\r")
                    .append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<").append(info == null ? "" : info);

            println(strBuffer.toString());
        }
    }


    public static void onStrictMode() {
        if (DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                    .penaltyLog().penaltyDeath().build());
        }
    }


}