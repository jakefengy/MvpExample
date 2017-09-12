package com.pujieinfo.mobile.framework.utils;

import android.content.Context;

import com.pujieinfo.mobile.framework.support.dialog.CodeName;
import com.pujieinfo.mobile.framework.support.dialog.RadioMaterialBuilder;

import java.util.List;

import pj.mobile.ui.materialdialog.MaterialDialog;

/**
 * 2017-05-23.
 */

public class DialogUtils {

    public static MaterialDialog buildDialog(Context context, String msg, String ps, String ns) {
        return buildDialog(context, msg, ps, ns, null, null);
    }

    public static MaterialDialog buildDialog(Context context, String msg, String ps, String ns, MaterialDialog.SingleButtonCallback positive) {
        return buildDialog(context, msg, ps, ns, positive, null);
    }

    public static MaterialDialog buildDialog(Context context, String msg, String ps, String ns,
                                             MaterialDialog.SingleButtonCallback positive, MaterialDialog.SingleButtonCallback negative) {

        return new MaterialDialog.Builder(context)
                .content(msg)
                .positiveText(ps)
                .negativeText(ns)
                .onPositive(positive == null ? (a, b) -> a.dismiss() : positive)
                .onNegative(negative == null ? (a, b) -> a.dismiss() : negative)
                .build();

    }

    public static MaterialDialog buildDialog(Context context, String title, String msg, String ps, String ns,
                                             MaterialDialog.SingleButtonCallback positive, MaterialDialog.SingleButtonCallback negative) {

        return new MaterialDialog.Builder(context)
                .title(title)
                .content(msg)
                .positiveText(ps)
                .negativeText(ns)
                .onPositive(positive == null ? (a, b) -> a.dismiss() : positive)
                .onNegative(negative == null ? (a, b) -> a.dismiss() : negative)
                .build();

    }

    public static MaterialDialog buildSingleDialog(Context context, List<CodeName> items, int selectIndex, RadioMaterialBuilder.SelectAction action) {

        RadioMaterialBuilder build = new RadioMaterialBuilder(context, "", "")
                .setDatas(items)
                .setItemSelectedAction(selectIndex, action);

        MaterialDialog dialog = build.build();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }


}
