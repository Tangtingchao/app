package com.hudongwx.origin.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 *
 *支持视图显示不同的状态
 *
 */
public abstract class BaseStateView {

    private RelativeLayout.LayoutParams mLayoutParams;
    private RelativeLayout rootView;//控制显示的内容
    protected View mLoadingView;
    protected View mContentView;
    protected View mEmptyView;
    protected View mErrorView;

    protected boolean mLoadingAdded;
    protected boolean mEmptyAdded;
    protected boolean mErrorAdded;
    protected LayoutInflater mInflater;

    public BaseStateView(Activity activity,View content){
        this.mContentView = content;
        initView(activity,content);
    }

    private void initView(Activity activity,View content){
        mInflater = LayoutInflater.from(activity);

        mLoadingView = getLoadingView();
        mEmptyView = getEmptyView();
        mErrorView = getErrorView();

        mLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        //初始化根布局
        rootView = new RelativeLayout(activity);;
        rootView.setLayoutParams(mLayoutParams);
        ViewGroup parent = (ViewGroup) mContentView.getParent();
        parent.addView(rootView);
    }

    public abstract View getEmptyView();

    public abstract View getErrorView();

    public abstract View getLoadingView();

    /**
     * 加载中
     */
    public void onLoading(){
        if(mLoadingView != null){
            if(!mLoadingAdded){
                rootView.addView(mLoadingView, mLayoutParams);
                mLoadingAdded = true;
            }
            showViewWithStatus(LayoutStatus.Empty);
        }
    }

    /**
     * 加载完成
     */
    public void onDone(){
        showViewWithStatus(LayoutStatus.Done);
    }

    /**
     * 加载空
     */
    public void onEmpty(){
        if(mEmptyView != null){
            if(!mEmptyAdded){
                rootView.addView(mEmptyView, mLayoutParams);
                mEmptyAdded = true;
            }
            showViewWithStatus(LayoutStatus.Empty);
        }
    }

    /**
     * 加载错误
     */
    public void onError(){
        if(mErrorView != null){
            if(!mErrorAdded){
                rootView.addView(mErrorView, mLayoutParams);
                mErrorAdded = true;
            }
            showViewWithStatus(LayoutStatus.Error);
        }
    }

    protected void showViewWithStatus(LayoutStatus status) {
        switch (status) {
            case Loading:
                if (mLoadingView == null) {
                    return;
                }
                mLoadingView.setVisibility(View.VISIBLE);
                if (mContentView != null)
                    mContentView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                break;
            case Done:
                if (mContentView == null) {
                    return;
                }
                mContentView.setVisibility(View.VISIBLE);
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                break;
            case Empty:
                if (mEmptyView == null) {
                    return;
                }
                mEmptyView.setVisibility(View.VISIBLE);
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mContentView != null)
                    mContentView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                break;
            case Error:
                if (mErrorView == null) {
                   return;
                }
                mErrorView.setVisibility(View.VISIBLE);
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mContentView != null)
                    mContentView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    protected enum LayoutStatus {
        Loading, Done, Empty, Error
    }

}
