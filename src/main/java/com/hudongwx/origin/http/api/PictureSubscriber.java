package com.hudongwx.origin.http.api;

import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;

import rx.Subscriber;

/**
 * Created by hudongwx on 17-2-16.
 */
public abstract class PictureSubscriber<T> extends Subscriber<T> implements HttpCancelListener {
    @Override
    public void onCancel() {
        //手动退出监听
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
    protected abstract void onOk(T t);

    protected abstract void onError(String msg);

    @Override
    public void onCompleted() {
        //Logger.d("ResultSubscriber 取消订阅");
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if(e instanceof ApiException){ //错误信息
            onError(e.getMessage());
        }else if(e instanceof JsonParseException){//json解析异常
            onError("数据解析异常");
        }else{
            onError("其他异常");
        }
    }
    @Override
    public void onNext(T t) {
        onOk(t);
    }
}
