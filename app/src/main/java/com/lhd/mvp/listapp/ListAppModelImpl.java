package com.lhd.mvp.listapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.lhd.module.ItemApp;
import com.lhd.mvp.logapp.LogStateApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D on 8/10/2017.
 */

public class ListAppModelImpl implements ListAppModel {
    LogStateApp logStateApp;

    public ListAppModelImpl() {
        logStateApp = new LogStateApp();


    }

    @Override
    public ArrayList<ItemApp> getListApp() {
        return null;
    }

    @Override
    public void saveState() {

    }

    @Override
    public ArrayList<ItemApp> getAllListApp(Context mContext) {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager mPm = mContext.getPackageManager();
        ArrayList<ItemApp> itemApps = new ArrayList<>();
        List<ResolveInfo> ris = mPm.queryIntentActivities(i, 0);
//        List<ApplicationInfo> list = mPm.getInstalledApplications(0);
//        for (ApplicationInfo applicationInfo:list) {
//            Log.e("duongapp", applicationInfo.packageName);
//            Log.e("duongapp", applicationInfo.loadLabel(mPm).toString());
//        }
        for (ResolveInfo ri : ris) {
            if (!mContext.getPackageName().equals(ri.activityInfo.packageName)) {
//                final AppListElement ah = new AppListElement(ri.loadLabel(mPm).toString(), ri.activityInfo, AppListElement.PRIORITY_NORMAL_APPS);
//                Log.e("duongapp", ri.loadLabel(mPm).toString());
//                Log.e("duongapp", ri.activityInfo.loadIcon(mPm) + "");
                ItemApp itemApp = new ItemApp(ri.activityInfo.loadIcon(mPm) , ri.activityInfo.packageName, ri.loadLabel(mPm).toString(), false);
                itemApps.add(itemApp);
            }
        }
        return itemApps;
    }

    @Override
    public ArrayList<ItemApp> getListAppLocked(Context mContext) {
        return null;
    }

    @Override
    public ArrayList<ItemApp> getListAppUnLock(Context mContext) {
        return null;
    }

    @Override
    public void putAllApp(Context mContext) {
        logStateApp.putAllApp(mContext, getAllListApp(mContext));
    }

    public ArrayList<ItemApp> getListApp(Context mContext) {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager mPm = mContext.getPackageManager();
        ArrayList<ItemApp> itemApps = new ArrayList<>();
        List<ResolveInfo> ris = mPm.queryIntentActivities(i, 0);
        List<ApplicationInfo> list = mPm.getInstalledApplications(0);
//        for (ApplicationInfo applicationInfo:list) {
//            Log.e("duongapp", applicationInfo.packageName);
//            Log.e("duongapp", applicationInfo.loadLabel(mPm).toString());
//        }
        for (ResolveInfo ri : ris) {
            if (!mContext.getPackageName().equals(ri.activityInfo.packageName)) {
//                final AppListElement ah = new AppListElement(ri.loadLabel(mPm).toString(), ri.activityInfo, AppListElement.PRIORITY_NORMAL_APPS);
//                Log.e("duongapp", ri.loadLabel(mPm).toString());
//                Log.e("duongapp", ri.activityInfo.packageName);
//                Log.e("duongapp", ri.activityInfo.loadIcon(mPm) + "");
                ItemApp itemApp = new ItemApp(ri.activityInfo.loadIcon(mPm) , ri.activityInfo.packageName, ri.loadLabel(mPm).toString(), false);
                itemApps.add(itemApp);
            }
        }
        return itemApps;
    }
}
