package com.pujieinfo.mobile.framework.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pujieinfo.mobile.framework.R;

/**
 * 2017-05-18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        initDataBinding();
        initView();
        initData();
        initAction();

    }

    protected abstract void initDataBinding();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initAction();
}
