package com.lhd.mvp.listapp;


import com.lhd.module.ItemApp;

import java.util.ArrayList;

/**
 * Created by d on 9/11/2017.
 */

public class Group {
    private String name;
    private ArrayList<ItemApp> childArrayList;

    public Group(String name, ArrayList<ItemApp> child) {
        this.name = name;
        this.childArrayList = child;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ItemApp> getChildArrayList() {
        return childArrayList;
    }

    public void setChildArrayList(ArrayList<ItemApp> childArrayList) {
        this.childArrayList = childArrayList;
    }
}
