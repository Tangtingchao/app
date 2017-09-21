package com.hudongwx.origin.http.client;


import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.hudongwx.origin.AppConstant;
import com.hudongwx.origin.AppContext;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * api请求工厂创建器
 */
public enum ClientFactory {

    INSTANCE;

    private volatile OkHttpClient okHttpClient;

    private static final int TIMEOUT_READ = 15;
    private static final int TIMEOUT_CONNECTION = 15;

    private final OkHttpClient.Builder mBuilder;

    ClientFactory() {
        mBuilder = new OkHttpClient.Builder();
        //调试的时候添加日志拦截器
        if (true) {
            mBuilder.addInterceptor(ClientHelper.getHttpLoggingInterceptor());
        }
        //设置缓存
        Cache cache = new Cache(new File(AppConstant.NET_DATA_PATH), 10 * 1024 * 1024);
        mBuilder.cache(cache);

        //添加拦截处理
        mBuilder.addNetworkInterceptor(ClientHelper.getAutoCacheInterceptor());
        mBuilder.addInterceptor(ClientHelper.getAutoCacheInterceptor());

        //设置cookie缓存
        ClearableCookieJar cookieJar = new PersistentCookieJar(
                new SetCookieCache(),
                new SharedPrefsCookiePersistor(AppContext.getContext()));
        mBuilder.cookieJar(cookieJar);

        mBuilder.retryOnConnectionFailure(true) //错误的时候,重试请求
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .build();
    }

    private void onHttpsNoCertficates() {
        try {
            mBuilder.sslSocketFactory(ClientHelper.getSSLSocketFactory()).hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onHttpCertficates(int[] certficates, String[] hosts) {
        mBuilder.socketFactory(ClientHelper.getSSLSocketFactory(AppContext.getContext(), certficates));
        mBuilder.hostnameVerifier(ClientHelper.getHostnameVerifier(hosts));
    }

    public OkHttpClient getHttpClient() {
        okHttpClient = mBuilder.build();
        return okHttpClient;
    }

}