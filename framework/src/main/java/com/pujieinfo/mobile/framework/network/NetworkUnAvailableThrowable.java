package com.pujieinfo.mobile.framework.network;

/**
 * 2017-03-27.
 */

public class NetworkUnAvailableThrowable extends Throwable {

    private static final String desc = "网络不可用";

    public NetworkUnAvailableThrowable() {
        this(desc);
    }

    public NetworkUnAvailableThrowable(String message) {
        super(message);
    }

}
