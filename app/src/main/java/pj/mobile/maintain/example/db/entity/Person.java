package pj.mobile.maintain.example.db.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * 2017-09-14.
 */

public class Person extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String name;

    private int age;

    @Ignore
    private String remark;

    private RealmList<Dog> dogs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RealmList<Dog> getDogs() {
        if (dogs == null) {
            dogs = new RealmList<>();
        }
        return dogs;
    }

    public void setDogs(RealmList<Dog> dogs) {
        if (this.dogs == null) {
            this.dogs = new RealmList<>();
        }
        this.dogs.clear();
        this.dogs.addAll(dogs);
    }

    public String getDogsName() {
        RealmList<Dog> ds = getDogs();
        String dn = "";
        for (Dog d : ds) {
            dn += d.getName() + " , ";
        }
        return dn;
    }
}
