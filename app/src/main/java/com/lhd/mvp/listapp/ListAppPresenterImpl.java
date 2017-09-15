package com.lhd.mvp.listapp;

/**
 * Created by D on 8/10/2017.
 */

public class ListAppPresenterImpl implements ListAppPresenter {

    private ListAppModelImpl listAppModel;

    public ListAppPresenterImpl(ListAppFragment listAppFragment) {
        listAppModel = new ListAppModelImpl();
        listAppModel.putAllApp(listAppFragment.getContext());
        listAppFragment.setListUnlock(listAppModel.getListAppUnLock(listAppFragment.getContext()));
        listAppFragment.setListLocked(listAppModel.getListAppLocked(listAppFragment.getContext()));
        listAppFragment.showListUnlock();
        listAppFragment.showListLocked();
//        listAppFragment.loadListAppToList(listAppModel.getListApp());
//        listAppModel.getAllListApp(listAppFragment.getContext());

    }
}
