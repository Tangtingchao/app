package com.hudongwx.origin.dbinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView绑定
 */
public class RecyclerViewBindingAdapter {

    /**
     * 快速绑定adapter
     * @param recyclerView
     * @param adapter
     */
    @BindingAdapter(value = {"binding:adapter"})
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置布局
     * @param recyclerView
     * @param manager\7
     */
    @BindingAdapter(value = {"binding:layoutManager"})
    public static void setLayoutManager(RecyclerView recyclerView, RecyclerView.LayoutManager manager){
        recyclerView.setLayoutManager(manager);
    }

}
