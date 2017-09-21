package com.hudongwx.origin.http.api;

import android.widget.Toast;

import com.cpoopc.retrofitrxcache.RxCacheHttpException;
import com.cpoopc.retrofitrxcache.RxCacheResult;
import com.google.gson.JsonParseException;
import com.hudongwx.origin.AppContext;
import com.hudongwx.origin.ui.SimpleLoadDialog;
import com.hudongwx.origin.utils.SharedPreferencesUtil;
import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 *
 * 支持缓存的的Subscriber
 *
 */
public abstract class ResultCacheSubscriber<D> extends Subscriber<RxCacheResult<IResult<D>>> implements HttpCancelListener{
    protected SimpleLoadDialog dialogHandler;

    public ResultCacheSubscriber(){}

    public ResultCacheSubscriber(SimpleLoadDialog dialog){
        this.dialogHandler = dialog;
    }
    /**
     * 错误处理
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        //Logger.e(e.getMessage());
        try{
            if(e instanceof ApiException){ //错误信息
                onError(e.getMessage(),-1002);
            }else if(e instanceof JsonParseException){//json解析异常
                onError("数据解析异常",-1001);
            }else if(e instanceof SocketTimeoutException){
                onError("网络异常",-1003);
            }else if(e instanceof ConnectException){
                onError("网络请求失败",-1004);
            }else if(e instanceof RxCacheHttpException){
                onError("网络异常",-1005);
            }else{

            }
        }catch (Exception e1){
            //Logger.e(e1);
        }finally {
            //完成
            onCompleted();
        }
    }

    @Override
    public void onCompleted() {
        //Logger.d("ResultSubscriber 取消订阅");
        dismissDialog();
        onFinish();
    }

    @Override
    public void onNext(RxCacheResult<IResult<D>> ret) {
        IResult<D> result = ret.getResultModel();
        if(result.isOk()){ //请求数据返回成功
            onOk(ret.isCache(),result.getData());
        }else{
            onError(result.getErrorMsg(),result.getCode());//返回错误信息
        }
    }

    @Override
    public void onStart() {
        showDialog();
    }

    /**
     * 显示Dialog
     */
    public void showDialog(){
        if (dialogHandler != null) {
            dialogHandler.obtainMessage(SimpleLoadDialog.SHOW_PROGRESS_DIALOG).sendToTarget();
            dialogHandler.show();
        }
    }

    /**
     * 隐藏Dialog
     */
    private void dismissDialog(){
        if (dialogHandler != null) {
            dialogHandler.obtainMessage(SimpleLoadDialog.DISMISS_PROGRESS_DIALOG).sendToTarget();
            dialogHandler.dismiss();
            dialogHandler=null;
        }
    }

    @Override
    public void onCancel() {
        //手动退出监听
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
    /**
     * 请求成功
     * @param t 返回数据
     */
    protected abstract void onOk(boolean isCache,D t);
    /**
     * 请求错误
     * @param msg 提示错误信息
     */
    protected  void onError(String msg,int code){
        Toast.makeText(AppContext.getContext(), msg, Toast.LENGTH_SHORT).show();
        if(code==403){
            SharedPreferencesUtil.exitLoad(AppContext.getContext());
            SharedPreferencesUtil.deletUser(AppContext.getContext());
        }
    }
    /**
     * 完成之后触发
     */
    protected void onFinish(){
    }
}
