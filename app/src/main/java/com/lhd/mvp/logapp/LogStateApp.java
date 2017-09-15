package com.lhd.mvp.logapp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lhd.module.Config;
import com.lhd.module.ItemApp;

import java.util.ArrayList;

/**
 * Created by D on 8/14/2017.
 */

public class LogStateApp extends MyLog implements LogStartAppInter {

    @Override
    public ArrayList<ItemApp> getLockedApp(Context context) {
        return new Gson().fromJson(getStringValueByName(context, Config.LOG_APP, Config.LOCKED_APP),
                new TypeToken<ArrayList<ItemApp>>() {
                }.getType());
    }

    @Override
    public void delAll(Context context) {
        removeAll();
    }

    @Override
    public ArrayList<ItemApp> removeOneLockedApp(Context context, ItemApp itemApp) {
        ArrayList<ItemApp> itemApps = getLockedApp(context);
        for (int i = itemApps.size(); i >= 0; i++) {
            if (itemApps.get(i).getNamePackage().equals(itemApp.getNamePackage())) {
                itemApps.remove(i);
            }
        }
        putStringValueByName(context, Config.LOG_APP, Config.LOCKED_APP, "");
        putStringValueByName(context, Config.LOG_APP, Config.LOCKED_APP, new Gson().toJson(itemApps,
                new TypeToken<ArrayList<ItemApp>>() {
                }.getType()));
        return itemApps;
    }

    @Override
    public ArrayList<ItemApp> putOneLockedApp(Context context, ItemApp itemApp) {
        ArrayList<ItemApp> itemApps = getLockedApp(context);
        for (int i = itemApps.size(); i >= 0; i++) {
            if (itemApps.get(i).getNamePackage().equals(itemApp.getNamePackage())) {
                itemApps.remove(i);
                itemApps.add(itemApp);
            }
        }
        putStringValueByName(context, Config.LOG_APP, Config.LOCKED_APP, "");
        putStringValueByName(context, Config.LOG_APP, Config.LOCKED_APP, new Gson().toJson(itemApps,
                new TypeToken<ArrayList<ItemApp>>() {
                }.getType()));
        return itemApps;
    }

    @Override
    public ArrayList<ItemApp> getAllApp(Context context, ItemApp itemApp) {
        return new Gson().fromJson(getStringValueByName(context, Config.LOG_APP, Config.ALL_APP),
                new TypeToken<ArrayList<ItemApp>>() {
                }.getType());
    }

    @Override
    public void putAllApp(Context context, ArrayList<ItemApp> itemApp) {
        putStringValueByName(context, Config.LOG_APP, Config.ALL_APP, "");
        putStringValueByName(context, Config.LOG_APP, Config.ALL_APP, new Gson().toJson(itemApp));
//        new Gson().fromJson(getStringValueByName(context, Config.LOG_APP, Config.LOCKED_APP),
//                new TypeToken<ArrayList<ItemApp>>() {
//                }.getType());
    }

    @Override
    public ArrayList<ItemApp> updateLockedApp(Context context, ItemApp itemApp) {
        ArrayList<ItemApp> itemApps = getLockedApp(context);
        for (int i = itemApps.size(); i >= 0; i++) {
            if (itemApps.get(i).getNamePackage().equals(itemApp.getNamePackage())) {
                itemApps.remove(i);
                itemApp.setLock(true);
                itemApps.add(itemApp);
            }
        }
        putStringValueByName(context, Config.LOG_APP, Config.LOCKED_APP, "");
        putStringValueByName(context, Config.LOG_APP, Config.LOCKED_APP, new Gson().toJson(itemApps,
                new TypeToken<ArrayList<ItemApp>>() {
                }.getType()));
        return itemApps;
    }

    @Override
    public ArrayList<ItemApp> getAllUnlockApp(Context context, ItemApp itemApp) {
        return null;
    }

    @Override
    public ArrayList<ItemApp> updateUnlockApp(Context context, ItemApp itemApp) {
        return null;
    }


}
