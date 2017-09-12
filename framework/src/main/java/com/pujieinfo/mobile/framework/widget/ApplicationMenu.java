package com.pujieinfo.mobile.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.pujieinfo.mobile.framework.ItemMenuBinding;
import com.pujieinfo.mobile.framework.R;


/**
 * 2016-11-28.
 */
public class ApplicationMenu extends LinearLayout {

    private Context context;

    private ItemMenuBinding binding;

    private String name = "";
    private String unread = "";
    private boolean isUnReadShow = false;
    private int resIcon = R.mipmap.icon;


    public ApplicationMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApplicationMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        if (isInEditMode()) {
            return;
        }
        initValues(attrs);
        initMenu();
    }

    private void initValues(AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ApplicationMenu);

        resIcon = a.getResourceId(R.styleable.ApplicationMenu_bg, R.mipmap.icon);
        name = a.getString(R.styleable.ApplicationMenu_name);
        unread = a.getString(R.styleable.ApplicationMenu_unread);

        a.recycle();

    }


    private void initMenu() {

        binding = DataBindingUtil.bind(LayoutInflater.from(context).inflate(R.layout.item_menu, null));
        binding.unreadCount.setVisibility(INVISIBLE);
        binding.setName(name);
        binding.setUnread(unread);
        binding.setIsUnReadShow(false);
        binding.icon.setImageResource(resIcon);

        addView(binding.getRoot());

    }

    public void setName(String name) {
        this.name = name;
        binding.setName(name);
    }

    public void setUnread(int unread) {
        if (unread > 99) {
            this.unread = "99+";
        } else {
            this.unread = unread + "";
        }
        setUnReadShow(unread != 0);
        binding.setUnread(this.unread);
    }

    public void setResIcon(@DrawableRes int resIcon) {
        this.resIcon = resIcon;
        binding.icon.setImageResource(resIcon);
    }

    public void setUnReadShow(boolean unReadShow) {
        isUnReadShow = unReadShow;
        binding.setIsUnReadShow(unReadShow);
    }
}
