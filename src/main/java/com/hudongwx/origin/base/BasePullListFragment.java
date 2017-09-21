package com.hudongwx.origin.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.List;

/**
 *
 * 支持列表上拉和下拉刷新
 *
 */
public abstract class BasePullListFragment<T extends ViewDataBinding,E extends BaseQuickAdapter,D> extends BaseListFragment<T,E,D>{

    protected SwipeRefreshLayout mRefreshLayout;
    protected ProgressDialog dialog = null;
    protected View mEmptyView;//空视图
    protected View mErrorView; //错误显示视图
    protected View mLoadingView;//加载中显示视图

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.dataBind = DataBindingUtil.inflate(inflater, getLayoutId(), null, false);
        View view = dataBind.getRoot();
        //设置刷新
        mRecyclerView = (RecyclerView)view.findViewById(android.R.id.list);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setProgressViewOffset(true,35,70);

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
                            BasePullListFragment.this.onLoadMoreRequested();
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

    public E getAdapter(){
        return this.mAdapter;
    }

    protected RecyclerView.LayoutManager initLayoutManager(){
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pullrefresh_list_layout;
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
        return inflateView(R.layout.empty_view_1);
    }
    public View getLoadingView(){
        return null;
    }


    /**
     * 加载完成之后处理
     */
    protected void onLoadComplete(List data){
        this.mAdapter.addData(data);
        onLoadMoreComplete();
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
     * 设置正在刷新
     */
    public void onRefreshingState(){
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        },100);
    }


    /**
     * 加载更多状态完成了
     */
    public void onLoadMoreComplete(){
        this.mAdapter.loadMoreComplete();//加载完成
    }

    /**
     * 没有更多数据
     */
    public void loadMoreEnd(){
        //this.mAdapter.loadMoreEnd();//没有更多数据
        setEnableLoadMore(false);
    }
    /**
     * 加载更多数据失败
     */
    public void loadMoreFail(){
        this.mAdapter.loadMoreFail();//加载数据失败
    }

    /**
     * 打开或关闭加载
     * @param b
     */
    protected void setEnableLoadMore(boolean b){
        this.mAdapter.setEnableLoadMore(b);
    }

    /**
     * 创建适配器
     * @return
     */
    public abstract E initAdapter();

    /**
     * 关闭刷新
     */
    public void  onRefreshingComplete(){
        mRefreshLayout.setRefreshing(false);
    }

    /**
     * 打开刷新
     */
    public void  onRefreshingOpen(){
        mRefreshLayout.setRefreshing(true);
    }

    //加载更多时候触发
    @Override
    public void onLoadMoreRequested() {

    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {

    }

}
