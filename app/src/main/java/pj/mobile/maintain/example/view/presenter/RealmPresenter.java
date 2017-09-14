package pj.mobile.maintain.example.view.presenter;

import android.content.Context;

import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import pj.mobile.maintain.example.db.entity.Dog;
import pj.mobile.maintain.example.db.entity.Person;
import pj.mobile.maintain.example.db.helper.RealmHelper;
import pj.mobile.maintain.example.view.contract.IRealmContract;

public class RealmPresenter implements IRealmContract.Presenter {

    private Context context;
    private IRealmContract.View view;
    private CompositeDisposable disposables;

    private Realm realm;
    private Random random;

    public RealmPresenter(Context context, IRealmContract.View view) {
        this.context = context;
        this.view = view;
        this.disposables = new CompositeDisposable();
        this.realm = RealmHelper.getRealm();
        this.random = new Random(50);
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
        RealmHelper.closeRealm(realm);
    }

    @Override
    public void add() {

        Person person = new Person();
        String id = String.valueOf(random.nextInt(100));
        person.setId(id);
        person.setName("person-" + id);
        person.setRemark("Remark" + id);
        person.setAge(Integer.valueOf(id));

        Dog dog = new Dog();
        id = String.valueOf(random.nextInt(100));
        dog.setId(id);
        dog.setName("dog-" + id);

        person.getDogs().add(dog);

        RealmHelper.insertOrUpdate(realm, person);

    }

    @Override
    public void update() {
        RealmResults<Person> persons = realm.where(Person.class).findAll();

        if (persons.isEmpty()) {
            return;
        }

        Person last = persons.last();
        last.setName(last.getName() + "-update");

        RealmList<Dog> dogs = last.getDogs();
        if (dogs == null) {
            dogs = new RealmList<>();
        }

        Dog dog = new Dog();
        String id = String.valueOf(random.nextInt(100));
        dog.setId(id);
        dog.setName("dog-" + id + "-update");

        dogs.add(dog);

        last.setDogs(dogs);

        RealmHelper.insertOrUpdate(realm, last);

    }

    @Override
    public void del() {

        RealmResults<Person> persons = realm.where(Person.class).findAll();

        if (persons.isEmpty()) {
            return;
        }

        RealmHelper.delete(persons.first());

    }

    @Override
    public void reset() {
        realm.delete(Person.class);
    }
}
