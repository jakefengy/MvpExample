package com.pujieinfo.mobile.framework.rx.bus;

/**
 * 2017-05-22.
 */

public class RxBusAction {

    public static final int CODE_LOGIN_SUCCESS = 0;
    public static final int CODE_LOGIN_OUT = 1;

    private int code;
    private String msg;
    private Object object;

    public RxBusAction setCode(int code) {
        this.code = code;
        return this;
    }

    public RxBusAction setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public RxBusAction setObject(Object object) {
        this.object = object;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getObject() {
        return object;
    }
}
