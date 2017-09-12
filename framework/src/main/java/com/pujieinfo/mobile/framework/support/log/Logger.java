package com.pujieinfo.mobile.framework.support.log;

import android.util.Log;

import com.pujieinfo.mobile.framework.BuildConfig;


/**
 * 2017-05-10.
 */

public class Logger {

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.v(tag, msg);
    }
}
