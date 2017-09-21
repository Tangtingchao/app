package com.hudongwx.origin.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hudongwx.origin.R;
import com.hudongwx.origin.ui.BaseStateView;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import me.yokeyword.fragmentation.SupportFragment;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 *
 * 支持多视图状态显示
 *
 */
public abstract class BaseStateFragment<T extends ViewDataBinding> extends SupportFragment implements LifecycleProvider<FragmentEvent>, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    protected T dataBind;
    protected Toolbar mToolbar;
    protected TextView mTtitle;
    protected BaseStateView mStateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBind = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mStateView = initStateView(dataBind.getRoot());
        initView(dataBind,savedInstanceState);
        return dataBind.getRoot();
    }

    protected void initToolBar(String title){
        mToolbar = (Toolbar) dataBind.getRoot().findViewById(R.id.title_bar);
        mTtitle = (TextView) this.mToolbar.findViewById(R.id.title);
        mTtitle.setText(title);
        AppCompatActivity activity = getAppCompatActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected void inflateMenu(int resId){
        mToolbar.inflateMenu(resId);
        mToolbar.setOnMenuItemClickListener(this);
    }

    private AppCompatActivity getAppCompatActivity(){
        return (AppCompatActivity) getActivity();
    }

    /**
     * 设置返回图标
     * @param resid
     */
    protected void setToolBarLeft(int resid){
        getAppCompatActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(resid);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick(v);
            }
        });
    }

    /**
     * 设置空的时候显示的视图
     * @return
     */
    protected abstract BaseStateView initStateView(View view);

    protected void setTitle(String title){
        this.mTtitle.setText(title);
    }

    /**
     * 左边导航栏点击了
     * @param v
     */
    protected void onBackClick(View v){

    }

    /**
     * onCreateView 初始化视图做的事情
     * @param dataBind
     * @param savedInstanceState
     */
    protected abstract void initView(T dataBind, Bundle savedInstanceState);
    /**
     * 视图的id
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 首次加载数据的时候执行,只执行一次
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        initData(savedInstanceState);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }

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

    @Override
    public void onClick(View v) {

    }
}
