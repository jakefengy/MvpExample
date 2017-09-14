package com.pujieinfo.mobile.framework.support.log;

import android.util.Log;


/**
 * 2017-05-10.
 */

public class Logger {

    private static boolean debug = true;

    public static void init(boolean debug) {
        Logger.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void e(String tag, String msg) {
        if (isDebug())
            Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isDebug())
            Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug())
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug())
            Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug())
            Log.v(tag, msg);
    }
}
