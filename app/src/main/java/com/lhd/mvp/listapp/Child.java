package com.lhd.mvp.listapp;

/**
 * Created by d on 9/11/2017.
 */
public class Child {
    private String name;
    private int idImage;

    public Child(String name, int idImage) {
        this.name = name;
        this.idImage = idImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }
}
