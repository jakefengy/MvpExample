package com.pujieinfo.mobile.framework.support.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.List;

import pj.mobile.ui.materialdialog.MaterialDialog;

/**
 * 2016-12-15.
 */
public class RadioMaterialBuilder extends MaterialDialog.Builder {

    private String title = "请选择：";
    private String positiveText = "确定";
    private List<CodeName> datas;


    public RadioMaterialBuilder(@NonNull Context context, String title, String positiveText) {
        super(context);
        this.datas = new ArrayList<>();
        this.title = TextUtils.isEmpty(title) ? this.title : title;
        this.positiveText = TextUtils.isEmpty(positiveText) ? this.positiveText : positiveText;

        setTitle(this.title);
        positiveText(this.positiveText);
    }

    public RadioMaterialBuilder setTitle(String title) {
        this.title = title;
        title(title);
        return this;
    }

    public RadioMaterialBuilder setDatas(List<CodeName> datas) {
        this.datas = datas;

        List<String> items = new ArrayList<>();
        for (CodeName n : datas) {
            items.add(n.getName());
        }

        items(items);

        return this;

    }

    public RadioMaterialBuilder setItemSelectedAction(int selectedIndex, SelectAction action) {
        itemsCallbackSingleChoice(selectedIndex, (dialog, itemView, which, text) -> {
            if (which >= 0 && datas != null && !datas.isEmpty())
                action.onSelected(datas.get(which));
            return true;
        });

        return this;

    }

    public interface SelectAction {
        void onSelected(CodeName item);
    }

}
