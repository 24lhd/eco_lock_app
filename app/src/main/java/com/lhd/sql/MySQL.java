package com.lhd.sql;

import android.content.Context;
import android.database.Cursor;

import com.lhd.module.Config;

import java.util.ArrayList;

/**
 * Created by D on 8/17/2017.
 */

public class MySQL implements MySQLInter {
    DuongSQLite duongSQLite;

    Context context;

    @Override
    public void insertOneItemApp(String itemApp) {
        runQuery(Config.CREATE_TABLE_LIST);
        runQuery("INSERT INTO `app_locked`(`stt`,`package`) VALUES (NULL,'" + itemApp + "');");
    }

    public MySQL(Context context) {
        this.context = context;
        duongSQLite = new DuongSQLite();
        initDatabase(Config.DB_NAME);
    }

    @Override
    public void initDatabase(String name) {
        duongSQLite.createOrOpenDataBases(context, name);
        runQuery(Config.CREATE_TABLE_LIST);
    }

    @Override
    public void runQuery(String query) {
        duongSQLite.getDatabase().execSQL(query);
    }

    @Override
    public void updateItemApp(String itemApp) {

    }

    @Override
    public void removeOneItemApp(String itemApp) {
        duongSQLite.getDatabase().execSQL("DELETE FROM `app_locked` WHERE `package` IN ('" + itemApp + "');");
    }

    @Override
    public void removeAllItemApp() {
        duongSQLite.getDatabase().execSQL("DELETE  FROM `app_locked`");
    }

    @Override
    public ArrayList<String> getAllItemApp() {
        ArrayList<String> strings = new ArrayList<>();
        try {
            Cursor cursor = duongSQLite.getDatabase().query(Config.TABLE_LIST, null, null, null, null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                strings.add(cursor.getString(cursor.getColumnIndex(Config.CL_LIST_LOCKED)));
                cursor.moveToNext();
            }
        } catch (Exception e) {
        }
        return strings;
    }

    @Override
    public boolean isExistsItemApp(String itemApp, ArrayList<String> itemApps) {
        return itemApps.contains(itemApp);
    }

    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;


}
