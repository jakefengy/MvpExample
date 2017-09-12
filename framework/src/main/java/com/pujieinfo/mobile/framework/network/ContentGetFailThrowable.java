package com.pujieinfo.mobile.framework.network;

/**
 * 2017-03-27.
 */

public class ContentGetFailThrowable extends Throwable {

    private static final String desc = "获取内容失败";

    public ContentGetFailThrowable() {
        this(desc);
    }

    public ContentGetFailThrowable(String message) {
        super(message);
    }

}
