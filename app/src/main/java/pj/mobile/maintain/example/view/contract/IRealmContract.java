package pj.mobile.maintain.example.view.contract;

import com.pujieinfo.mobile.framework.mvp.IBaseContract;

public interface IRealmContract {

    interface View {

    }

    interface Presenter extends IBaseContract.Presenter {

        void add();

        void update();

        void del();

        void reset();

    }

}
