package com.lhd.sql;

import java.util.ArrayList;

/**
 * Created by D on 8/17/2017.
 */

interface MySQLInter {
    public void insertOneItemApp(String itemApp);

    public void initDatabase(String name);

    public void runQuery(String query);

    public void updateItemApp(String query);

    public void removeOneItemApp(String itemApp);

    public void removeAllItemApp();

    public ArrayList<String> getAllItemApp();

    public boolean isExistsItemApp(String itemApp, ArrayList<String> itemApps);


}
