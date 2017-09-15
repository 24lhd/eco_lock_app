package com.lhd.module;

import java.util.ArrayList;

/**
 * Created by D on 8/14/2017.
 */

public class ItemListApps {
    private String title;
    private ArrayList<ItemApp> itemApps;

    @Override
    public String toString() {
        return "ItemListApps{" +
                "title='" + title + '\'' +
                ", itemApps=" + itemApps +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ItemApp> getItemApps() {
        return itemApps;
    }

    public void setItemApps(ArrayList<ItemApp> itemApps) {
        this.itemApps = itemApps;
    }

    public ItemListApps(String title, ArrayList<ItemApp> itemApps) {

        this.title = title;
        this.itemApps = itemApps;
    }
}
