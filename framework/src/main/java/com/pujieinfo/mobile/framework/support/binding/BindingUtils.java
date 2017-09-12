package com.pujieinfo.mobile.framework.support.binding;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.EditText;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pujieinfo.mobile.framework.image.ImageUtils;

/**
 */
public class BindingUtils {

    // 加载网络图片
    @BindingAdapter({"http_image"})
    public static void setHttpImage(SimpleDraweeView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ImageUtils.cacheImage(imageView, url);
    }

    // 加载本地图片
    @BindingAdapter({"file_image"})
    public static void setFileImage(SimpleDraweeView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ImageUtils.cacheImage(imageView, url);
    }

    // 设置光标在末尾
    @BindingAdapter({"cursorEnd"})
    public static void setCursorEnd(EditText view, String toEnd) {
        if (toEnd != null && toEnd.length() > 0)
            view.setSelection(view.getText().length());
    }

}
