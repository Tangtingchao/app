package com.hudongwx.origin.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hudongwx.origin.R;

import java.util.List;

/**
 * 支持数据绑定的QuickAdapter
 */
public abstract class BindingQuickAdapter<T,K extends BindingViewHolder> extends BaseQuickAdapter<T,K> {

    public BindingQuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected K createBaseViewHolder(View view) {
        return (K) new BindingViewHolder(view);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_viewholder_support, binding); //为view设置绑定的tag
        return view;
    }

}
