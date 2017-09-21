package com.hudongwx.origin.http.api;

/**
 * 数据返回接口
 */
public interface IResult<T> {
    /**
     * 消息数据
     * @return
     */
    T getData();

    /**
     * 是否请操作
     * @return
     */
    boolean isOk();

    /**
     * 错误信息
     * @return
     */
    String getErrorMsg();

    /**
     * 状态码
     * @return
     */
    int getCode();
}
