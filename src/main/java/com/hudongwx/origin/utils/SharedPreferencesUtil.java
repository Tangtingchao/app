package com.hudongwx.origin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

/**
 * 基本数据类型的存储
 * Created by hudongwx on 16-12-29.
 */
public class SharedPreferencesUtil {
    protected static String FILE_NAME="history";
    protected static String IS_LOADING="loading";//判断是否登陆
    protected static String STATE="state";//状态三方登陆还是本地登陆1 本地 2 三方
    protected static String USER_FILE="user";
    protected static String HEAD_IMG="headImg";
    protected static String USER_NAME="userName";


    //获取SharedPerences对象
    public static SharedPreferences getShared(Context context,String file_name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(file_name, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    //读取历史数据
    public static List<String> getHisData(Context context){
        SharedPreferences shared = getShared(context, FILE_NAME);
        int number = shared.getInt("number", 0);
        List<String> strings=new ArrayList<>();
        if(number!=0){
            for (int i = number-1; i >= 0; i--) {
                String string = shared.getString(FILE_NAME + i, null);
                strings.add(string);
            }
        }
        return strings;
    }
    //删除历史数据
    public static void deleHisData(Context context){
        SharedPreferences shared = getShared(context, FILE_NAME);
        shared.edit().clear().commit();
    }
    //添加历史数据
    public static void addHisData(Context context,String data){
        SharedPreferences shared = getShared(context, FILE_NAME);
        List<String> hisData = getHisData(context);
        boolean isExist=false;
        for (String str : hisData) {
            if(str.equals(data)){
                isExist=true;
            }
        }
        if(!isExist){
            SharedPreferences.Editor edit = shared.edit();
            int number = shared.getInt("number", 0);
            List<String> strings=new ArrayList<>();
            edit.putString(FILE_NAME+number,data);
            edit.putInt("number",number+1);
            edit.commit();
        }
    }
    //用户登录成功
    public static void saveUser(Context context,int state){
        SharedPreferences shared = getShared(context, IS_LOADING);
        SharedPreferences.Editor edit = shared.edit();
        edit.putInt(STATE,state);
        edit.putBoolean(IS_LOADING,true);
        edit.commit();
    }
    //判断用户是否登陆成功
    public  static boolean isLoad(Context context){
        SharedPreferences shared = getShared(context, IS_LOADING);
        boolean aBoolean = shared.getBoolean(IS_LOADING, false);
        return aBoolean;
    }
    //获取登陆方式
    public static int getState(Context context){
        SharedPreferences shared = getShared(context,IS_LOADING);
        int state = shared.getInt(STATE, -1);
        return state;
    }
    //用户退出登录
    public static void exitLoad(Context context){
        SharedPreferences shared = getShared(context, IS_LOADING);
        shared.edit().clear().commit();
    }

    //用户三方登陆成功后存储头像姓名
    public static void saveThreeUser(Context context,String userName,String headImg){
        SharedPreferences shared = getShared(context,USER_FILE);
        SharedPreferences.Editor edit = shared.edit();
        edit.putString(USER_NAME,userName);
        edit.putString(HEAD_IMG,headImg);
        edit.commit();
    }
    //获取用户本地姓名
    public static String getUserName(Context context){
        SharedPreferences shared = getShared(context,USER_FILE);
        return shared.getString(USER_NAME,"");
    }
    //获取用户本地头像
    public static String getUserImg(Context context){
        SharedPreferences shared = getShared(context,USER_FILE);
        return shared.getString(HEAD_IMG,"");
    }
    //用户退出清除头像姓名
    public static void deletUser(Context context){
        SharedPreferences shared = getShared(context,USER_FILE);
        shared.edit().clear().commit();
    }
    //获取当前版本进入app几次
    public static int searchLuncher(Context context){
        SharedPreferences shared = getShared(context,USER_FILE);
        int luncher = shared.getInt("luncher", 0);
        return luncher;
    }
    //保存当前第几次进入app
    public static void saveLuncher(Context context){
        SharedPreferences shared = getShared(context,USER_FILE);
        SharedPreferences.Editor edit = shared.edit();
        edit.putInt("luncher",1);
        edit.commit();
    }
    //版本更新时归零
    public static void deleLuncher(Context context){
        SharedPreferences shared = getShared(context,USER_FILE);
        shared.edit().putInt("luncher",1).commit();
    }
    //存储短信倒计时
    public static void saveMsgTime(Context context,long times){
        SharedPreferences shared = getShared(context,USER_FILE);
        SharedPreferences.Editor edit = shared.edit();
        edit.putLong("time",times);
        edit.commit();
    }
    //查询短信倒计时
    public static long queryMsgTime(Context context){
        SharedPreferences shared = getShared(context,USER_FILE);
        long time = shared.getLong("time", 0l);
        return time;
    }
}
