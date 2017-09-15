package pj.mobile.maintain.example.view.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.content.Intent;
import android.view.View;

import com.pujieinfo.mobile.framework.activity.BaseActivity;
import com.pujieinfo.mobile.framework.widget.recycler.LoadMoreSwipeRefreshLayout;

import pj.mobile.maintain.Activity_RealmBinding;
import pj.mobile.maintain.R;
import pj.mobile.maintain.example.view.contract.IRealmContract;
import pj.mobile.maintain.example.view.presenter.RealmPresenter;
import pj.mobile.maintain.example.view.adapter.RealmAdapter;
import pj.mobile.maintain.example.view.entity.RealmEntity;

import java.util.List;

public class Activity_Realm extends BaseActivity implements IRealmContract.View, RealmAdapter.OnItemClickListener {

    private Activity_RealmBinding binding;

    private IRealmContract.Presenter presenter;

    private RealmAdapter adapter;

    public static Intent getIntent(Context ctx) {
        Intent intent = new Intent(ctx, Activity_Realm.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_realm);
    }

    @Override
    protected void initView() {
        adapter = new RealmAdapter(Activity_Realm.this, null);
        adapter.setOnItemClickListener(this);

        adapter.setSize(0);
        binding.refreshLayout.setAdapter(adapter);
        binding.refreshLayout.setCanLoadMore(false);
        binding.refreshLayout.setOnSwipeListener(new LoadMoreSwipeRefreshLayout.OnSwipeListener() {
            @Override
            public void onRefresh() {
                adapter.resetIndex();
                presenter.reload();
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    @Override
    protected void initData() {
        presenter = new RealmPresenter(this, this);
        binding.refreshLayout.reload();
    }

    @Override
    protected void initAction() {

    }

    public void add(View v) {
        presenter.add();
    }

    public void update(View v) {
        presenter.update();
    }

    public void del(View v) {
        presenter.del();
    }

    public void reset(View v) {
        presenter.reset();
    }

    @Override
    protected void onDestroy() {
        presenter.release();
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, RealmEntity entity) {

    }

    private void onGetDataComplete(boolean success, List<RealmEntity> items) {
        binding.refreshLayout.onGetDataComplete(success, items);
    }

    // callback
    @Override
    public void onGetPersons(boolean success, String error, List<RealmEntity> realms) {
        onGetDataComplete(success, realms);
    }
}
