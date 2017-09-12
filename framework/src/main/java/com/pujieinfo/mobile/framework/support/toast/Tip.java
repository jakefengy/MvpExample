package com.pujieinfo.mobile.framework.support.toast;

import android.content.Context;
import android.widget.Toast;

/**
 * 2017-05-23.
 */

public class Tip {

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
