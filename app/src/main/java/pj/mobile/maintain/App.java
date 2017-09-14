package pj.mobile.maintain;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.pujieinfo.mobile.framework.support.log.Logger;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import pj.mobile.maintain.example.db.helper.RealmHelper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initModule();
    }

    private void initModule() {
        Logger.init(BuildConfig.DEBUG);
        RealmHelper.init(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }

}
