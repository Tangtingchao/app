package com.hudongwx.origin.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.hudongwx.origin.R;

/**
 * 支持列表显示的ListFragment
 */
public abstract class BaseListFragment<T extends ViewDataBinding,E extends BaseQuickAdapter,D> extends BaseFragment<T> implements BaseQuickAdapter.RequestLoadMoreListener {

    protected RecyclerView mRecyclerView;
    protected E mAdapter;

    protected View mEmptyView;//空视图
    protected View mErrorView; //错误显示视图
    protected View mLoadingView;//加载中显示视图

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.dataBind = DataBindingUtil.inflate(inflater, getLayoutId(), null, false);
        View view = dataBind.getRoot();
        mRecyclerView = (RecyclerView)view.findViewById(android.R.id.list);

        //设置list
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(initLayoutManager());
        mAdapter = initAdapter();
        //设置动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        //是否支持加载更多
        LoadMoreView loadMoreView = getLoadMoreView();
        if(loadMoreView != null){
            mAdapter.setLoadMoreView(loadMoreView);
            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    //避免出现Cannot call this method while RecyclerView is computing a layout or scrolling 异常
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BaseListFragment.this.onLoadMoreRequested();
                        }
                    }, 2);
                }
            });
            mAdapter.setEnableLoadMore(true);
        }

        //设置空视图
        mEmptyView = getEmptyView();
        if(mEmptyView != null){
            mEmptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefresh();
                }
            });
        }

        //设置加载中视图
        mLoadingView = getLoadingView();
        //加载错误
        mErrorView   = getErrorView();
        if(mErrorView != null){
            mErrorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefresh();
                }
            });
        }


        // 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
        //mQuickAdapter.setAutoLoadMoreSize(int);
        mRecyclerView.setAdapter(mAdapter);
        initView(savedInstanceState);

        return view;
    }

    protected RecyclerView.LayoutManager initLayoutManager(){
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.refresh_list;
    }

    /**
     * 创建view
     * @param resid
     * @return
     */
    protected View inflateView(int resid){
        return getLayoutInflater(null).inflate(resid, (ViewGroup) mRecyclerView.getParent(), false);
    }

    /**
     * 加载更多视图
     */
    public LoadMoreView getLoadMoreView(){
        return new SimpleLoadMoreView();
    }

    /**
     * 设置错误的时候的显示视图
     * @return
     */
    public View getErrorView(){
        return null;
    }
    public View getEmptyView(){
        return null;
    }
    public View getLoadingView(){
        return null;
    }

    /**
     * 正在加载的状态
     */
    public void onLoadingState(){
        if(mLoadingView == null)return;
        mAdapter.setEmptyView(mLoadingView);
    }

    /**
     * 没有数据,加载错误的时候的状态
     */
    public void onErrorState(){
        if(mErrorView == null)return;
        mAdapter.setEmptyView(mErrorView);
    }

    /**
     *设置列表空时候的显示状态
     */
    public void onEmptyState(){
        if(mEmptyView == null)return;
        mAdapter.setEmptyView(mEmptyView);
    }

    /**
     * 加载完成之后处理
     */
    protected void onLoadComplete(D d){
        this.mAdapter.addData(d);
        this.mAdapter.loadMoreComplete();//加载完成
    }

    /**
     * 没有更多数据
     */
    protected void loadMoreEnd(){
        this.mAdapter.loadMoreEnd();//没有更多数据
    }

    /**
     * 打开或关闭加载
     * @param b
     */
    protected void setEnableLoadMore(boolean b){
        this.mAdapter.setEnableLoadMore(b);
    }

    /**
     * 加载数据失败
     */
    protected void loadMoreFail(){
        this.mAdapter.loadMoreFail();//加载数据失败
    }
    /**
     * 创建适配器
     * @return
     */
    public abstract E initAdapter();

    //加载更多时候触发
    @Override
    public void onLoadMoreRequested() {

    }

}
