package com.lhd.mvp.listapp;

import android.content.Context;

import com.lhd.module.ItemApp;

import java.util.ArrayList;

/**
 * Created by D on 8/10/2017.
 */

public interface ListAppModel {
    public ArrayList<ItemApp> getListApp();

    public void saveState();

    public ArrayList<ItemApp> getAllListApp(Context mContext);

    public ArrayList<ItemApp> getListAppLocked(Context mContext);

    public ArrayList<ItemApp> getListAppUnLock(Context mContext);

    public void putAllApp(Context mContext);


}
