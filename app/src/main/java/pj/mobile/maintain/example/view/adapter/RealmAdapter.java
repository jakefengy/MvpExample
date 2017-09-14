package pj.mobile.maintain.example.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.pujieinfo.mobile.framework.widget.recycler.LoadMoreSwipeRefreshLayout;

import java.util.List;

import pj.mobile.maintain.Activity_RealmAdapterBinding;
import pj.mobile.maintain.R;
import pj.mobile.maintain.example.view.entity.RealmEntity;

/**
 *
 */
public class RealmAdapter extends LoadMoreSwipeRefreshLayout.Adapter<RealmEntity, RealmAdapter.ViewHolder> {

    private OnItemClickListener mItemClickListener;

    public RealmAdapter(Context ctx, List<RealmEntity> datas) {
        super(ctx, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.listitem_realm, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RealmEntity entity);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Activity_RealmAdapterBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            binding.setClick(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getItem(getLayoutPosition()));
            }
        }

        public void bind(RealmEntity entity) {
            binding.setEntity(entity);
        }

    }

}


