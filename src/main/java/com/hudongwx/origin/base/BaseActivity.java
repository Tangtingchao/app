package com.hudongwx.origin.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.hudongwx.origin.R;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.analytics.MobclickAgent;

import kale.dbinding.DBinding;
import me.yokeyword.fragmentation.SupportActivity;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 *支持ViewDataBinding的Activity
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends SupportActivity implements LifecycleProvider<ActivityEvent> {

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    protected Toolbar mToolbar;
    protected TextView mTtitle;
    protected T dataBind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        dataBind = DBinding.bind(this, getLayoutId());
        init(savedInstanceState);
    }
//    public void initBar() {
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //状态栏
////            int identifier = context.getResources()
////                    .getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
////            ViewGroup.LayoutParams params = view.getLayoutParams();
////            params.height=identifier;
////            view.setLayoutParams(params);
////            view.setBackgroundColor(getResources().getColor(R.color.color_bar_background));
//        }
//    }
    protected void initToolBar(){
        mToolbar = (Toolbar) dataBind.getRoot().findViewById(R.id.title_bar);
        mTtitle = (TextView) this.mToolbar.findViewById(R.id.title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    /**
     * 设置返回图标
     * @param resid
     */
    protected void setToolBarLeft(int resid){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(resid);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    pop();
                } else {
                    onBackClick(v);
                }
            }
        });
    }

    protected void onBackClick(View v){
        finish();
    }

    protected void setTitle(String title){
        mTtitle.setText(title);
    }

    /**
     * 初始化视图设置,设置Presenter和ViewModel对象
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 视图的id
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutId();

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }
    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        //友盟统计
        MobclickAgent.onPageStart(getClass().getSimpleName()); //统计页面(参数为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        /**
         * 友盟统计策略就是在Activity或者Fragment中统计一次即可
         */
        MobclickAgent.onPageEnd(getClass().getSimpleName()); //保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }


    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }


}
