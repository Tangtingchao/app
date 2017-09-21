package com.hudongwx.origin.adapter;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.hudongwx.origin.R;

/**
 * 支持数据绑定的viewholder
 */
public class BindingViewHolder<T extends ViewDataBinding> extends BaseViewHolder {

    public BindingViewHolder(View view) {
        super(view);
    }

    public T getBinding() {
        //在BindingQuickAdapter里面的getItemView,设置的tag
        return (T) getConvertView().getTag(R.id.BaseQuickAdapter_viewholder_support);
    }

    /**
     * 绑定之后里面刷新数据
     * @param variableId
     * @param value
     */
    public void bind(int variableId, Object value){
        getBinding().setVariable(variableId,value);
        getBinding().executePendingBindings();
    }
}
