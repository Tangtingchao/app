package com.hudongwx.origin.base;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by hudongwx on 17-1-17.
 */

public abstract class BaseBottomSheetDialogFragment<T extends ViewDataBinding> extends BottomSheetDialogFragment implements LifecycleProvider<FragmentEvent> {

    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    protected T dataBind;
    protected Dialog dialog;
    protected View rootView;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.dataBind = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        initView(savedInstanceState);
        return dataBind.getRoot();
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        //每次打开都调用该方法 类似于onCreateView 用于返回一个Dialog实例
//        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
//        if (rootView == null) {
//            //缓存下来的View 当为空时才需要初始化 并缓存
//            rootView = View.inflate(mContext, getLayoutId(), null);
//            initView(savedInstanceState);
//        }
//        resetView();
//        //设置View重新关联
//        dialog.setContentView(rootView);
//        mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
//        mBehavior.setHideable(true);
//        //圆角边的关键
//        ((View) rootView.getParent()).setBackgroundColor(Color.TRANSPARENT);
//        rootView.post(new Runnable() {
//            @Override
//            public void run() {
//                /**
//                 * PeekHeight默认高度256dp 会在该高度上悬浮
//                 * 设置等于view的高 就不会卡住
//                 */
//                mBehavior.setPeekHeight(rootView.getHeight());
//            }
//        });
//        return dialog;
//    }

    /**
     * 视图的id
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutId();


    /**
     * onCreateView 初始化视图做的事情
     * @param savedInstanceState
     *
     */
    protected abstract void initView(Bundle savedInstanceState);


    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }


}
