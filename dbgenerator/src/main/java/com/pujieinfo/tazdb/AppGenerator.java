package com.pujieinfo.tazdb;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Generator GreenDao
 *
 * @author fengy
 */
public class AppGenerator {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "pj.mobile.maintain.example.db.entity");

        addUser(schema);

        schema.setDefaultJavaPackageDao("pj.mobile.maintain.example.db.dao");
        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();
        new DaoGenerator().generateAll(schema, "./dbgenerator/src/main/java");
    }

    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("User");

        user.setTableName("User");
        user.addStringProperty("Id");
        user.addStringProperty("UserName").primaryKey();
        user.addStringProperty("OrgName");
        user.addStringProperty("Name");
        user.addStringProperty("HeadId");
        user.addStringProperty("Mobile");
        user.addBooleanProperty("IsLogin");
        user.addLongProperty("LoginDate");
        user.addBooleanProperty("IsLeader");
    }

}