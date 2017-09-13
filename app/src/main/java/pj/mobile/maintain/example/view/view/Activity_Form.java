package pj.mobile.maintain.example.view.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;

import com.pujieinfo.mobile.framework.activity.BaseActivity;

import pj.mobile.maintain.Activity_FormBinding;
import pj.mobile.maintain.R;
import pj.mobile.maintain.example.view.contract.IFormContract;
import pj.mobile.maintain.example.view.presenter.FormPresenter;
import pj.mobile.maintain.example.view.vm.FormViewModel;

public class Activity_Form extends BaseActivity implements IFormContract.View {

    private Activity_FormBinding binding;

    private IFormContract.Presenter presenter;

    private FormViewModel vm;

    public static Intent getIntent(Context ctx) {
        Intent intent = new Intent(ctx, Activity_Form.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form);
        vm = new FormViewModel();
        binding.setVm(vm);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        presenter = new FormPresenter(this, this);
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void onDestroy() {
        presenter.release();
        super.onDestroy();
    }

}
