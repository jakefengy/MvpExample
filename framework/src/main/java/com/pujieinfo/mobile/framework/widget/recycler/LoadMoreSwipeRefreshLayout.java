package com.pujieinfo.mobile.framework.widget.recycler;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.pujieinfo.mobile.framework.R;
import com.pujieinfo.mobile.framework.support.toast.Tip;

import java.util.ArrayList;
import java.util.List;


/**
 * 下啦刷新，加载更多
 */
public class LoadMoreSwipeRefreshLayout extends SwipeRefreshLayout {

    // RecyclerView
    private TouchableRecyclerView recyclerView;
    private Adapter adapter;

    // Empty View
    private RelativeLayout rlEmpty;
    private TextView tvEmpty;
    private ImageView ivEmpty;

    // empty desc
    private String emptyDesc = getResources().getString(R.string.extend_refresh_def_empty);
    private int emptyImage = R.mipmap.no_data_common;

    private OnSwipeListener onSwipeListener;
    private OnLayoutScrollListener onLayoutScrollListener;

    // refresh
    private boolean canRefresh = true;
    private boolean isRefreshing = false;

    // load more
    private final static int PRELOAD_SIZE = 1;
    private boolean canLoadMore = true;
    private boolean isLoading = false;

    public LoadMoreSwipeRefreshLayout(Context context) {
        super(context);
    }

    public LoadMoreSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.extend_refresh_layout, null);
        recyclerView = (TouchableRecyclerView) view.findViewById(R.id.refresh_rv_list);
        rlEmpty = (RelativeLayout) view.findViewById(R.id.refresh_rl_container);
        ivEmpty = (ImageView) view.findViewById(R.id.refresh_iv_empty);
        tvEmpty = (TextView) view.findViewById(R.id.refresh_tv_empty);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setOnRefreshListener(getOnRefreshListener());
        recyclerView.addOnScrollListener(getOnBottomListener(linearLayoutManager));

        addView(view);

    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    private OnRefreshListener getOnRefreshListener() {
        return () -> {
            if (!canRefresh) {
                onRefreshComplete();
                return;
            }

            if (isRefreshing || isLoading) {
                return;
            }

            if (onSwipeListener != null) {
                setRefreshing(true);
                isRefreshing = true;
                onSwipeListener.onRefresh();
            }

        };
    }

    private RecyclerView.OnScrollListener getOnBottomListener(final LinearLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {

                if (!canLoadMore) {
                    onLoadComplete();
                    return;
                }

                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPosition() >= recyclerView.getAdapter().getItemCount() - PRELOAD_SIZE;

                if (!isBottom) {
                    return;
                }

                if (isRefreshing || isLoading) {
                    return;
                }

                if (onSwipeListener != null) {
                    isLoading = true;
                    setRefreshing(true);
                    onSwipeListener.onLoadMore();
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (onLayoutScrollListener != null) {
                    onLayoutScrollListener.onScrollStateChange(recyclerView, newState);
                }
            }
        };
    }

    public void onRefreshComplete() {
        setRefreshing(false);
        isRefreshing = false;
        checkEmpty();
    }

    public void onLoadComplete() {
        setRefreshing(false);
        isLoading = false;
        checkEmpty();
    }

    private void checkEmpty() {

        boolean isEmpty = recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0;

        rlEmpty.setVisibility(isEmpty ? VISIBLE : GONE);
        tvEmpty.setText(emptyDesc);
        ivEmpty.setImageResource(emptyImage);
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public void setOnLayoutScrollListener(OnLayoutScrollListener onLayoutScrollListener) {
        this.onLayoutScrollListener = onLayoutScrollListener;
    }

    public void reload() {
        if (!canRefresh) {
            onRefreshComplete();
            return;
        }

        if (isRefreshing || isLoading) {
            return;
        }

        if (onSwipeListener != null) {
            setRefreshing(true);
            isRefreshing = true;
            onSwipeListener.onRefresh();
        }
    }

    // interface
    public interface OnSwipeListener {
        void onRefresh();

        void onLoadMore();
    }

    public interface OnLayoutScrollListener {
        void onScrollStateChange(RecyclerView recyclerView, int newState);
    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public void setEmptyDesc(String emptyDesc) {
        this.emptyDesc = emptyDesc;
        checkEmpty();
    }

    public void setEmptyImage(int emptyRes) {
        this.emptyImage = emptyRes;
        checkEmpty();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public abstract static class Adapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

        private Context context;
        private LayoutInflater inflater;

        private List<T> dataSource;
        private int index = 1;
        private int size = 10;

        public Adapter(Context context, List<T> dataSource) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.dataSource = new ArrayList<>();
            if (dataSource != null) {
                this.dataSource.addAll(dataSource);
            }
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public int getItemCount() {
            return dataSource.size();
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void updateSource(List<T> data) {
            if (data == null) {
                return;
            }

            dataSource.clear();
            dataSource.addAll(data);
            notifyDataSetChanged();
        }

        public void addData(List<T> data) {
            if (data == null) {
                return;
            }

            dataSource.addAll(data);
            notifyItemRangeInserted(getItemCount() - data.size(), data.size());

        }

        protected T getItem(int position) {
            return dataSource.get(position);
        }

        public LayoutInflater getInflater() {
            return inflater;
        }

        public List<T> getDataSource() {
            return dataSource;
        }

        public void resetIndex() {
            this.index = 1;
        }

        public Context getContext() {
            return context;
        }
    }

    public <T> void onGetDataComplete(boolean success, List<T> items) {
        if (adapter == null) {
            onRefreshComplete();
            onLoadComplete();
            return;
        }

        if (success) {

            if (items.size() < adapter.getSize() && items.size() > 0) {
                if (adapter.getIndex() == 1) {
                    setCanLoadMore(false);
                }
                adapter.setIndex(adapter.getIndex() + 1);
            } else if (items.size() >= adapter.getSize()) {
                adapter.setIndex(adapter.getIndex() + 1);
                setCanLoadMore(true);
            } else {
                setCanLoadMore(false);
                if (adapter.getIndex() > 1) {
                    Tip.toast(getContext(), "没有更多内容");
                }
            }

            if (isRefreshing()) {
                adapter.updateSource(items);
            } else {
                adapter.addData(items);
            }

        }
        onRefreshComplete();
        onLoadComplete();
    }

}
