package pj.mobile.maintain.example.db.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * 2017-09-14.
 */

public class Dog extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String name;

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
}
