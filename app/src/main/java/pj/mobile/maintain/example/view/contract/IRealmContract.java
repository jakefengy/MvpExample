package pj.mobile.maintain.example.view.contract;

import com.pujieinfo.mobile.framework.mvp.IBaseContract;

import java.util.List;

import pj.mobile.maintain.example.view.entity.RealmEntity;

public interface IRealmContract {

    interface View {

        void onGetPersons(boolean success, String error, List<RealmEntity> realms);

    }

    interface Presenter extends IBaseContract.Presenter {

        void add();

        void update();

        void del();

        void reset();

        void reload();
    }

}
