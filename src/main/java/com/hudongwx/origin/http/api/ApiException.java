package com.hudongwx.origin.http.api;

/**
 * API 异常信息封装
 */
public class ApiException extends RuntimeException{

    private int errorCode;//错误码
    private String errorMsg;//错误信息

    public ApiException(int code,String errorMsg) {
        super(errorMsg);
        this.errorCode = code;
        this.errorMsg = errorMsg;
    }

    public ApiException(int code,String detailMessage,String errorMsg) {
        super(detailMessage);
        this.errorCode = code;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
