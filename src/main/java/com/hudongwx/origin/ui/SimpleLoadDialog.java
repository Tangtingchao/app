package com.hudongwx.origin.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.hudongwx.origin.AppContext;

import java.lang.ref.WeakReference;

/**
 *
 */
public class SimpleLoadDialog extends Handler{
    public static String LOADING="正在加载中...";
    public static String LOADING_LOGIN="登录中...";
    public static String LOADING_WAITING="请稍后...";
    private Dialog load = null;
    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;
    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;
    private final WeakReference<Context> reference;
    private boolean isRefresh;
    private String msg;
    public SimpleLoadDialog(Context context, ProgressCancelListener mProgressCancelListener,
                            boolean cancelable) {
        super();
        this.reference = new WeakReference<Context>(context);
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    public SimpleLoadDialog(Context context,boolean isRefresh) {
        super();
        this.isRefresh = isRefresh;
        this.reference = new WeakReference<Context>(context);
        this.cancelable = false;
    }
    public SimpleLoadDialog(Context context,boolean isRefresh,String msg) {
        super();
        if(msg==null){
            this.msg = LOADING;
        }
        this.msg=msg;
        this.isRefresh = isRefresh;
        this.reference = new WeakReference<Context>(context);
        this.cancelable = false;
    }

    private void create(){
        if (load == null) {
            context  = reference.get();
            load = new ProgressDialog(context);
            load.setCancelable(cancelable);
            ((ProgressDialog) load).setMessage(msg);
            load.setCanceledOnTouchOutside(true);
            load.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if(mProgressCancelListener!=null)
                        mProgressCancelListener.onCancelProgress();
                }
            });

            /*
            load = new Dialog(context);
            View dialogView = LayoutInflater.from(context).inflate(
                    R.layout.custom_sload_layout, null);
            load.setCanceledOnTouchOutside(false);
            load.setCancelable(cancelable);
            load.setContentView(dialogView);
            load.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if(mProgressCancelListener!=null)
                        mProgressCancelListener.onCancelProgress();
                }
            });
            Window dialogWindow = load.getWindow();
            dialogWindow.setGravity(Gravity.CENTER_VERTICAL
                    | Gravity.CENTER_HORIZONTAL);*/
        }
        if(!isRefresh){
            if (!load.isShowing() && context!=null) {
                if(context instanceof Activity && !((Activity) context).isDestroyed()){
                    load.show();
                }
            }
        }
    }

    public void show(){
        create();
    }

    public  void dismiss() {
        context  = reference.get();
        if (load != null&&load.isShowing()) {
            load.dismiss();
            load = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                create();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismiss();
                break;
        }
    }


    public static interface ProgressCancelListener{
        void onCancelProgress();
    }

}
