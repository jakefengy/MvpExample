package pj.mobile.maintain.example.view.presenter;

import android.content.Context;

import pj.mobile.maintain.example.view.contract.IFormContract;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class FormPresenter implements IFormContract.Presenter {

    private Context context;
    private IFormContract.View view;
    private CompositeDisposable disposables = new CompositeDisposable();

    public FormPresenter(Context context, IFormContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (disposable == null) {
            return;
        }

        disposables.add(disposable);
    }

    @Override
    public void release() {
        disposables.clear();
    }

}
