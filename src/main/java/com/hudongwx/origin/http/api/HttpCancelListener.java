package com.hudongwx.origin.http.api;

/**
 *手动退出监听
 */
public interface HttpCancelListener {
    /**
     * 手动退出,当dialog退出的时候
     */
    void onCancel();
}
