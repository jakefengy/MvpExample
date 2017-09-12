package com.pujieinfo.mobile.framework.widget.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * 设置禁止滑动  用于被Scroller嵌套的RecyclerView
 * 2017-02-28.
 */
public class NoScrollLinearLayoutManager extends LinearLayoutManager {

    public NoScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public NoScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NoScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
