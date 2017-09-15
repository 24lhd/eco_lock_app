package com.lhd.module;

import android.graphics.drawable.Drawable;

/**
 * Created by D on 8/10/2017.
 */

public class ItemApp {
    private Drawable iconApp;
    private String namePackage;
    private String nameApp;
    private boolean isLock;


    public Drawable getIconApp() {
        return iconApp;
    }

    public void setIconApp(Drawable iconApp) {
        this.iconApp = iconApp;
    }

    public String getNamePackage() {
        return namePackage;
    }

    public void setNamePackage(String namePackage) {
        this.namePackage = namePackage;
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    @Override
    public String toString() {
        return "ItemApp{" +
                "iconApp=" + iconApp +
                ", namePackage='" + namePackage + '\'' +
                ", nameApp='" + nameApp + '\'' +
                ", isLock=" + isLock +
                '}';
    }

    public ItemApp(Drawable iconApp, String namePackage, String nameApp, boolean isLock) {
        this.iconApp = iconApp;
        this.namePackage = namePackage;
        this.nameApp = nameApp;
        this.isLock = isLock;
    }
}
