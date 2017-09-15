package pj.mobile.maintain.example.db.helper;

import android.app.Application;
import android.content.Context;
import android.content.PeriodicSync;

import com.pujieinfo.mobile.framework.support.log.Logger;

import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;
import pj.mobile.maintain.example.db.entity.Person;

/**
 * 2017-09-14.
 */

public class RealmHelper {

    private final static String TAG = RealmHelper.class.getName();

    private final static String DB_NAME = "Maintain";
    private final static long DB_VERSION = 0;

    public static void init(Context context) {

        if (!(context instanceof Application)) {
            throw new RuntimeException("Please init realm with Application Context ! ");
        }

        Realm.init(context);

        RealmMigration migration = RealmHelper::dealMigration;

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(DB_VERSION)
                .name(DB_NAME)
                .migration(migration)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

    }

    // db update
    private static void dealMigration(DynamicRealm realm, long oldVersion, long newVersion) {
        Logger.i(TAG, "RealmMigration : " + oldVersion + " -> " + newVersion);
    }

    // CURD

    public static Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    public static void closeRealm(Realm realm) {
        if (realm == null || realm.isClosed()) {
            return;
        }

        realm.close();
    }

    public static <T extends RealmModel> void insertOrUpdate(Realm realm, T t) {

        if (realm == null || t == null) {
            return;
        }

        realm.copyToRealmOrUpdate(t);

    }

    public static <T extends RealmModel> void insertOrUpdate(Realm realm, List<T> ts) {

        if (realm == null || ts == null || ts.isEmpty()) {
            return;
        }

        realm.copyToRealmOrUpdate(ts);
    }

    public static <T extends RealmObject> void delete(Realm realm, Class<? extends T> t, String id) {

        RealmObject result = realm.where(t).equalTo("", id).findFirst();

        if (result == null) {
            return;
        }

        result.deleteFromRealm();

    }

    public static <T extends RealmObject> void delete(T t) {
        if (t == null) {
            return;
        }
        t.deleteFromRealm();
    }

    public static <T extends RealmObject> void delete(List<T> ts) {
        if (ts == null || ts.isEmpty()) {
            return;
        }

        ts.forEach(RealmHelper::delete);
    }

}
