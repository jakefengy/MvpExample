package com.pujieinfo.mobile.framework.network;

/**
 * 2017-05-22.
 */

public class BaseResult {

    private final static String KEY_SUCCESS = "10000";

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return code.equals(KEY_SUCCESS);
    }

}
