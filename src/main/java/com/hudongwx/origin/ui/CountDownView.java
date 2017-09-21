package com.hudongwx.origin.ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.hudongwx.origin.R;
import com.hudongwx.origin.utils.FixCountDownTimer;

/**
 * 倒计时组件
 */
public class CountDownView extends TextView{

    private  FixCountDownTimer timer;
    private  OnCountDownFinishListener mListener;
    private  String formatStr = "%02d:%02d:%02d";//格式:分:秒:毫秒
    private  int interval = 140; //更新间隔时间
    private  long endTime;
    private  Object data;

    public CountDownView(Context context) {
        super(context);
        initView(context,null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView(context,attrs);
//    }

    private void initView(Context context, AttributeSet attrs) {
        if(attrs != null){
            TypedArray array = context.obtainStyledAttributes(attrs,
                    R.styleable.CountDownView);
            interval = array.getInteger(R.styleable.CountDownView_interval,interval);
            String tmp = array.getString(R.styleable.CountDownView_format);
            if(tmp != null){
                formatStr = tmp;
            }
            array.recycle();
        }
    }

    /**
     * 定时器结束时间
     * @param endTime
     */
    public void start(long endTime,Object data){
        this.data = data;
        stop();
        //计算剩余时间
        long leftTime = endTime - SystemClock.elapsedRealtime();
        //结束时间大于系统时间
        if(endTime<=0){
            onFinishHandler();
        }else{
            this.endTime = endTime;
            timer = new FixCountDownTimer(leftTime,interval) {
                int decade = 60000;//计算单位
                @Override
                public void onTick(long millisUntilFinished) {
                    int mu = (int)(millisUntilFinished/decade);//分钟
                    int sc = (int)((millisUntilFinished%decade)/1000);//秒
                    int ss = (int)(millisUntilFinished%decade%1000/10); //毫秒
                    setText(String.format(formatStr,mu,sc,ss));//格式化处理
                }
                @Override
                public void onFinish() {
                    onFinishHandler();
                }
            };
            timer.start();
        }
    }

    private void onFinishHandler(){
        if(mListener != null)
            mListener.onFinish(this,data);
    }

    /**
     * 重启
     */
    private void restart(){
        start(endTime,data);
    }

    /**
     * 停止
     */
    public void stop(){
        if(timer != null){
            timer.cancel();
        }
    }

    public void setOnFinishListener(OnCountDownFinishListener listener){
        this.mListener = listener;
    }

    public interface OnCountDownFinishListener<T>{
        void onFinish(CountDownView view,T data);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility == View.VISIBLE){
            restart();//重启
        }else{
            stop();
        }
    }

}
