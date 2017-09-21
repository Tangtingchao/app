package com.hudongwx.origin.base;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import com.hudongwx.origin.http.api.PictureSubscriber;
import com.hudongwx.origin.http.api.ResultCacheSubscriber;
import com.hudongwx.origin.http.api.ResultSubscriber;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.List;

import kale.dbinding.BaseViewModel;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @param <T>  ViewModel对象
 * @param <V>  View对象,可能是Activity或是Fragment
 *
 */
public abstract class BasePresenter<T extends BaseViewModel,V extends LifecycleProvider> {

    protected T viewModel;//视图对象
    private V view; //view对象

    public BasePresenter(V v, T model){
        this.viewModel = model;
        this.view = v;
    }

    /**
     * 执行任务
     * @param observable 要执行的任务
     * @param event 取消任务的时候
     * @return
     */
    private void execute(Observable observable, final Subscriber subscriber, Enum event){
        observable.subscribeOn(Schedulers.io())
                .compose(view.bindUntilEvent(event))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     *
     * 执行任务不需要返回结果的监听
     * @param observable
     * @param
     *
     */
    public void execute(Observable observable){
        observable.subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /**
     * 执行任务
     * @param observable 要执行的任务
     * @return
     */
    protected void execute(Observable observable, final Subscriber subscriber){
        if(view instanceof AppCompatActivity){
            execute(observable,subscriber,ActivityEvent.DESTROY);
        }else if(view instanceof Fragment){
            execute(observable,subscriber, FragmentEvent.DESTROY);
        }
    }

    /**
     * 图片
     * @param observable
     * @param pictureSubscriber
     */
    protected  void execute(Observable observable, final PictureSubscriber pictureSubscriber){
        if(view instanceof AppCompatActivity){
            execute(observable,pictureSubscriber,ActivityEvent.DESTROY);
        }else if(view instanceof Fragment){
            execute(observable,pictureSubscriber, FragmentEvent.DESTROY);
        }
    }
    /**
     * 返回视图
     * @return
     */
    public V getView(){
        return view;
    }

    /**
     * 返回视图&模型
     * @return
     */
    public T getViewModel(){
        return viewModel;
    }

    /**
     * 初始化数据操作
     */
    public abstract void initData();
    /**
     * 点击事件处理
     * @param view
     */
    public void onClick(View view){}
    /**
     * 内容改变事件
     */
    public void onTextChanged(){}
    /**
     * checkbox改变事件
     * @param view
     * @param b
     */
    public void onCheckedChanged(CompoundButton view, boolean b){}

}
