package com.lhd.mvp.listapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.lhd.applock.R;
import com.lhd.module.Config;
import com.lhd.module.ItemApp;
import com.lhd.mvp.lockapp.LockAppActivity;
import com.lhd.mvp.logapp.MyLog;
import com.lhd.mvp.main.MainActivity;
import com.lhd.sql.MySQL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by D on 8/10/2017.
 * <p>
 * <p>
 * dung appanie biet het
 */

public class ListAppFragment extends Fragment implements ListAppView {
    private ArrayList<ItemApp> itemApps;
    ListAppPresenterImpl listAppPresenter;
    private ArrayList<ItemApp> listAppUnlock;
    private ArrayList<ItemApp> listAppLocked;
    private ListView rcvLocked;
    private ListView rcvUnLock;
    private MainActivity mainActivity;
    private ProgressBar progLoad;
    private ExpandableListView expandableListView;
    private ExpandableListViewAdapter expandableListViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewContent = inflater.inflate(R.layout.list_app_layout, null);
        MyLog.putBooleanValueByName(getActivity(), Config.LOG_APP, Config.IS_FIRST_SET_LOCK, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.showAllPermisstion();
        mainActivity.showBar();
        initDate();
        initView(viewContent);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            mainActivity.showDialogPerAccess();
        }
        return viewContent;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        checkLock();
    }

    private void checkLock() {
        if (MyLog.getStringValueByName(getContext(), Config.LOG_APP, Config.APP_UNLOCK).equals(getContext().getPackageName()) == false) {
            Intent intent = new Intent(getContext(), LockAppActivity.class);
            intent.putExtra(Config.NAME_PACKAGE_ICON, getContext().getPackageName());
            startActivity(intent);
        }
    }

    MySQL mySQL;

    private void initDate() {
        mySQL = new MySQL(getContext());
        mySQL.getAllItemApp();
    }

    private void initView(View viewContent) {
        expandableListView = (ExpandableListView) viewContent.findViewById(R.id.elv_list_app);
//        rcvLocked = (ListView) viewContent.findViewById(R.id.item_list_app_rcv_list_locked_app);
//        rcvUnLock = (ListView) viewContent.findViewById(R.id.item_list_app_rcv_list_unlock_app);
        progLoad = (ProgressBar) viewContent.findViewById(R.id.prog_load);
        progLoad.setVisibility(View.VISIBLE);//day
//        if (mainActivity.isPermisstionUsagerAccess() == true && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            runSetListView();
//        } else {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (mainActivity.isPermisstionUsagerAccess() == true) {
                        runSetListView();
                        break;
                    }
                    if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
                        runSetListView();
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
//        }

//        rcvLocked.setAdapter(new AdaptorApp(getContext(),android.R.layout.activity_list_item,getAllListApp(getContext())));
//        new ListAppModelImpl().putAllApp(getContext());
//        rcvLocked.setAdapter(new AdaptorListApp(getContext(), new ListAppModelImpl().getListAppUnLock(getContext())));
    }

    ArrayList<Group> groups;

    private void runSetListView() {
//        expandableListView.
        new Thread() {
            @Override
            public void run() {
                try {
                    updateListItemApp();
                    expandableListViewAdapter = new ExpandableListViewAdapter(mainActivity, groups);
                    handler.sendEmptyMessage(1);
//                    adaptorApp = new AdaptorApp(getActivity(), android.R.layout.activity_list_item, itemApps);
                } catch (NullPointerException e) {

                }

            }
        }.start();
    }

    ArrayList<ItemApp> itemAppLocks;
    ArrayList<ItemApp> itemAppUnLocks;

    private void updateListItemApp() {
        itemApps = getAllListApp(getActivity());
        itemAppLocks = new ArrayList<ItemApp>();
        itemAppUnLocks = new ArrayList<ItemApp>();
        groups = new ArrayList<>();
        sortList();
        for (ItemApp itemApp : itemApps)
            if (itemApp.isLock()) itemAppLocks.add(itemApp);
            else itemAppUnLocks.add(itemApp);
        Group group2 = new Group(getResources().getString(R.string.list_app_txt_title_locked), itemAppLocks);
        Group group3 = new Group(getResources().getString(R.string.list_app_txt_title_unlock), itemAppUnLocks);
        groups.add(group2);
        groups.add(group3);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            expandableListView.setAdapter(expandableListViewAdapter);
            expandableListView.expandGroup(0);
            expandableListView.expandGroup(1);
//            rcvUnLock.setAdapter(adaptorApp);
//            rcvUnLock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    ImageView imStateLockApp = (ImageView) view.findViewById(R.id.item_app_id_im_state_app_lock);
//                    itemApps.get(i).setLock(!itemApps.get(i).isLock());
//                    if (itemApps.get(i).isLock()) {
//                        mySQL.insertOneItemApp(itemApps.get(i).getNamePackage());
//                        imStateLockApp.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_outline_light_green_a400_36dp));
//                        mainActivity.getLogger().logEvent("Main_Item_lockapp_Clicked");
//                    } else {
//                        mySQL.removeOneItemApp(itemApps.get(i).getNamePackage());
//                        imStateLockApp.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_open_white_36dp));
//                        mainActivity.getLogger().logEvent("Main_Item_unlockapp_Clicked");
//                    }
//                }
//            });
            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                    return true;
                }
            });

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                    try {
                        LinearLayout item = (LinearLayout) view.findViewById(R.id.item_app_id_layout_item);
                        ImageView imStateLockApp = (ImageView) view.findViewById(R.id.item_app_id_im_state_app_lock);
                        ItemApp itemAppCick = groups.get(i).getChildArrayList().get(i1);
                        itemAppCick.setLock(!itemAppCick.isLock());
                        if (itemAppCick.isLock()) {
                            mySQL.insertOneItemApp(itemAppCick.getNamePackage());
                            imStateLockApp.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_outline_light_green_a400_36dp));
                            mainActivity.getLogger().logEvent("Main_Item_lockapp_Clicked");
                            groups.get(1).getChildArrayList().remove(i1);
                            groups.get(0).getChildArrayList().add(itemAppCick);
                            Animation animation1 =
                                    AnimationUtils.loadAnimation(getApplicationContext(),
                                            R.anim.anim_unlock);
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    expandableListViewAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            item.startAnimation(animation);
                        } else {

                            mySQL.removeOneItemApp(itemAppCick.getNamePackage());
                            imStateLockApp.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_open_white_36dp));
                            mainActivity.getLogger().logEvent("Main_Item_unlockapp_Clicked");
                            groups.get(0).getChildArrayList().remove(i1);
                            groups.get(1).getChildArrayList().add(itemAppCick);
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_unlock);

                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    expandableListViewAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
                            item.startAnimation(animation);
                        }
                    } catch (IndexOutOfBoundsException e) {

                    }
                    return false;
                }
            });
            mainActivity.showAllPermisstion();
            progLoad.setVisibility(View.GONE);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tap, menu);
        return;
    }

    private void sortList() {
        for (ItemApp itemApp : itemApps) {
            if (mySQL.isExistsItemApp(itemApp.getNamePackage(), mySQL.getAllItemApp()))
                itemApp.setLock(true);
        }
        Collections.sort(itemApps, new Comparator<ItemApp>() {
            @Override
            public int compare(ItemApp itemApp, ItemApp t1) {
                return itemApp.getNameApp().compareTo(t1.getNameApp());
            }
        });
    }

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
                ItemApp itemApp = new ItemApp(ri.activityInfo.loadIcon(mPm), ri.activityInfo.packageName, ri.loadLabel(mPm).toString(), false);
                itemApps.add(itemApp);
            }
        }
        return itemApps;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        listAppPresenter = new ListAppPresenterImpl(this);
    }

    public Drawable ResizeImage(Drawable imageID) {
//Get device dimensions
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        BitmapDrawable bd = (BitmapDrawable) imageID;
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

//        Bitmap bMap = BitmapFactory.decodeResource(getResources(), imageID.geti);
//        Drawable drawable = new BitmapDrawable(this.getResources(), getResizedBitmap(bMap, newImageHeight, (int) deviceWidth));

//        return drawable;
        return null;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

// create a matrix for the manipulation
        Matrix matrix = new Matrix();

// resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

// recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    @Override
    public void loadListAppToList(ArrayList<ItemApp> itemApps) {
        this.itemApps = itemApps;
    }

    @Override
    public void setListUnlock(ArrayList<ItemApp> itemApps) {
        this.listAppUnlock = itemApps;
    }

    @Override
    public void setListLocked(ArrayList<ItemApp> itemApps) {
        this.listAppLocked = itemApps;
    }

    @Override
    public void refeshList() {
        runSetListView();
    }

    @Override
    public void startSetting() {

    }

    @Override
    public void sreachApp() {

    }

    @Override
    public void onLockApp(ItemApp itemApp) {

    }

    @Override
    public void showListLocked() {
//        rcvLocked.setAdapter(new AdaptorListApp(getContext(), listAppLocked));
    }

    @Override
    public void showListUnlock() {
//        rcvUnLock.setAdapter(new AdaptorListApp(getContext(), listAppUnlock));
    }
}
