package com.lhd.mvp.logapp;

import android.content.Context;

import com.lhd.module.ItemApp;

import java.util.ArrayList;

/**
 * Created by D on 8/14/2017.
 */

interface LogStartAppInter {
    public ArrayList<ItemApp> getLockedApp(Context context);

    // xóa tất cả dữ liệu
    public void delAll(Context context);

    //xóa 1 item trong list app đã khóa và trả về list mới
    public ArrayList<ItemApp> removeOneLockedApp(Context context, ItemApp itemApp);

    // chèn thêm một app khóa vào list và trả về list mới
    public ArrayList<ItemApp> putOneLockedApp(Context context, ItemApp itemApp);

    public ArrayList<ItemApp> getAllApp(Context context, ItemApp itemApp);

    public void putAllApp(Context context, ArrayList<ItemApp> itemApp);

    public ArrayList<ItemApp> updateLockedApp(Context context, ItemApp itemApp);

    public ArrayList<ItemApp> getAllUnlockApp(Context context, ItemApp itemApp);

    public ArrayList<ItemApp> updateUnlockApp(Context context, ItemApp itemApp);




}
