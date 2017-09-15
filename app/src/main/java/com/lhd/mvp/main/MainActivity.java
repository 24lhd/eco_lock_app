package com.lhd.mvp.main;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.lhd.applock.R;
import com.lhd.module.Config;
import com.lhd.module.ItemApp;
import com.lhd.mvp.listapp.ListAppFragment;
import com.lhd.mvp.lockapp.LockAppFragment;
import com.lhd.mvp.logapp.MyLog;
import com.lhd.mvp.setpin.SetPinFragment;
import com.lhd.mvp.toprunapp.MyService;
import com.lhd.mvp.toprunapp.StateDeviceService;
import com.lhd.mvp.wellcome.WellcomeFragment;
import com.lhd.sql.MySQL;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

import static com.lhd.sql.MySQL.OVERLAY_PERMISSION_REQ_CODE;

/**
 * Created by D on 8/8/2017.
 */

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = "MainActivity";
    private MainPresenter mainPresenter;
    private ListAppFragment listAppFragment;
    AppEventsLogger logger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.main_layout);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Gửi mã tracking lên FB analytic
        logger = AppEventsLogger.newLogger(this);
//        int i = 1 / 0;

        mainPresenter = new MainPresenterImpl(this);
//        new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    Log.e("duong", "" + LockAppActivity.isRunning);
//                    if (LockAppActivity.isRunning == true) {
//                        finish();
//                        return;
//                    }
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                    }
//                }
//            }
//        }.start();
        initView();
        startLogService();
    }

    private PackageManager mPm;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        savedInstanceState.clear();
        super.onRestoreInstanceState(savedInstanceState);
    }

    public ArrayList<ItemApp> loadAppsIntoList(Context mContext) {
        final Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        mPm = mContext.getPackageManager();
        ArrayList<ItemApp> itemApps = new ArrayList<>();
        final List<ResolveInfo> ris = mPm.queryIntentActivities(i, 0);
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
                ItemApp itemApp = new ItemApp(ri.activityInfo.loadIcon(mPm), ri.activityInfo.packageName, ri.loadLabel(mPm).toString(), false);
                itemApps.add(itemApp);
            }
        }
        return itemApps;
    }

    private LinearLayout itemNotiDrawOverApp;
    private LinearLayout itemNotiUsagerAccess;
    private LinearLayout itemNotiAccessibility;

    public void showDialogPerAccess() {
        if (isPermisstionUsagerAccess() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getResources().getString(R.string.dialog_request_usager_access_title));
            builder.setMessage(getResources().getString(R.string.dialog_request_usager_access_body));
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    requestPermisstionUsagerAccess();
                    if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
                        hideAllPermisstion();
                    }
                }
            });
            builder.setCancelable(false);
            builder.show();
        }

    }

    private void initView() {
        itemNotiDrawOverApp = (LinearLayout) findViewById(R.id.main_id_permisstion_draw_over);
        itemNotiUsagerAccess = (LinearLayout) findViewById(R.id.main_id_permisstion_usager_access);
        itemNotiAccessibility = (LinearLayout) findViewById(R.id.main_id_permisstion_accesssibility);
        itemNotiDrawOverApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.dialog_request_draw_over_app_title));
                builder.setMessage(getResources().getString(R.string.dialog_request_draw_over_app_body));
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        requestPermisstionDrawOverApp();
                    }
                });
                builder.show();
            }
        });
        itemNotiUsagerAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPerAccess();
            }
        });
        itemNotiAccessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.dialog_request_accessibility_title));
                builder.setMessage(getResources().getString(R.string.dialog_request_accessibility_body));
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        requestPermisstionAccesiblity();
                    }
                });
                builder.show();
            }
        });
        mainPresenter.checkStartWellcomeFragment();
    }

    @Override
    public void startWellcomeFragment() {
        MyLog.putBooleanValueByName(this, Config.LOG_APP, Config.IS_FINGERPRINT, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_view, new WellcomeFragment()).commit();
    }

    @Override
    public void startListAppFragment() {
        showBar();
        listAppFragment = new ListAppFragment();
        boolean isFirst = MyLog.getBooleanValueByName(this, Config.LOG_APP, Config.IS_FIRST_SET_LOCK);
        boolean isSetPin = MyLog.getStringValueByName(this, Config.LOG_APP, Config.PIN_CODE).equals("") == false;
        boolean isUnlock = MyLog.getStringValueByName(this, Config.LOG_APP, Config.MY_APP_UNLOCK).equals(getPackageName()) == false;
        if (isSetPin && isUnlock && isFirst == false) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_view, new LockAppFragment()).commit();
        } else
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_view, listAppFragment).commit();
//        Intent intent = new Intent(this, ListAppActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);

    }

    @Override
    public void startLogService() {
        if (!StateDeviceService.isRunning(this)) {
            startService(new Intent(this, StateDeviceService.class));
        }
        if (!MyService.isRunning(this)) {
            startService(new Intent(this, MyService.class));
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void showPermisstionDrawOverApp() {
//        if (!Settings.canDrawOverlays(this)) {
//            itemNotiDrawOverApp.setVisibility(View.VISIBLE);
//        }
//
//    }


//    @Override
//    public void requestPermisstionDrawOverApp() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            yeuCauDrawOver();
//        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
//            if (Settings.canDrawOverlays(this)) {
//                hidePermisstionDrawOverApp();
//            }
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void yeuCauDrawOver() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);

    }

//    @Override
//    public boolean isPermisstionDrawOverApp() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return Settings.canDrawOverlays(this);
//        }
//        return true;
//    }

    public AppEventsLogger getLogger() {
        return logger;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        logger.logEvent("Main_icon_menu_clicked");
        switch (item.getItemId()) {
            case R.id.id_menu_setting:
                Intent intent = new Intent(this, com.lhd.mvp.setting.Settings.class);
                startActivity(intent);
                logger.logEvent("Main_menu_item_setting_clicked");

                return true;
            case R.id.id_menu_unlock_all:
                logger.logEvent("Main_menu_item_unlockall_clicked");

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.dialog_unlock_all_app_title));
                builder.setMessage(getResources().getString(R.string.dialog_unlock_all_app_body));
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MySQL mySQL = new MySQL(MainActivity.this);
                        mySQL.removeAllItemApp();
                        listAppFragment.refeshList();
                        logger.logEvent("Main_dialog_unlockall_button_ok_clicked");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.create().dismiss();
                        logger.logEvent("Main_dialog_unlockall_button_cancel_clicked");
                    }
                });
                builder.show();

                return true;
        }
        return onOptionsItemSelected(item);
    }

    //    @Override
//    public void hidePermisstionDrawOverApp() {
//        itemNotiDrawOverApp.setVisibility(View.GONE);
//    }
// if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
    @Override
    public void showPermisstionUsagerAccess() {
        if (isGragUsageAccess() == false) {
            itemNotiUsagerAccess.setVisibility(View.VISIBLE);
        } else itemNotiUsagerAccess.setVisibility(View.GONE);
    }

    @Override
    public void requestPermisstionUsagerAccess() {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public boolean isPermisstionUsagerAccess() {
        return isGragUsageAccess();
    }

    @Override
    public void hidePermisstionUsagerAccess() {
        itemNotiUsagerAccess.setVisibility(View.GONE);
    }

//    @Override
//    public void showPermisstionAccesiblity() {
//        itemNotiAccessibility.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void requestPermisstionAccesiblity() {
//
//    }
//
//    @Override
//    public boolean isPermisstionAccesiblity() {
//        return false;
//    }
//
//    @Override
//    public void hidePermisstionAccesiblity() {
//        itemNotiAccessibility.setVisibility(View.GONE);
//    }

    @Override
    public void showAllPermisstion() {
//        showPermisstionAccesiblity();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            showPermisstionDrawOverApp();
        showPermisstionUsagerAccess();
        itemNotiAccessibility.setVisibility(View.GONE);
        itemNotiDrawOverApp.setVisibility(View.GONE);
//        itemNotiAccessibility.setVisibility(View.VISIBLE);
//        itemNotiUsagerAccess.setVisibility(View.VISIBLE);
//        itemNotiDrawOverApp.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAllPermisstion() {
        itemNotiAccessibility.setVisibility(View.GONE);
        itemNotiDrawOverApp.setVisibility(View.GONE);
        itemNotiUsagerAccess.setVisibility(View.GONE);
    }

    @Override
    public void startSetPinFragment() {
        if (MyLog.getStringValueByName(this, Config.LOG_APP, Config.PIN_CODE).equals("") == true) {
            hideAllPermisstion();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_view, new SetPinFragment()).commit();
        } else {
//            boolean isFirst = MyLog.getBooleanValueByName(this, Config.LOG_APP, Config.IS_FIRST_SET_LOCK);
//            boolean isUnlock = MyLog.getStringValueByName(this, Config.LOG_APP, Config.MY_APP_UNLOCK).equals(getPackageName());
//            listAppFragment = new ListAppFragment();
//            if (isUnlock == false && isFirst == false) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_view, new LockAppFragment()).commit();
//            } else
//                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_view, listAppFragment).commit();
            startListAppFragment();
        }
    }

    @Override
    public void startSettingFragment() {
        Toast.makeText(this, "startSettingFragment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideBar() {
        getSupportActionBar().hide();
    }

    @Override
    public void showBar() {
        getSupportActionBar().show();
    }

    public boolean isGragUsageAccess() {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            try {
//                PackageManager packageManager = this.getPackageManager();
//                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 0);
//                AppOpsManager appOpsManager = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
//                int mode = 0;
//                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
//                return (mode == AppOpsManager.MODE_ALLOWED);
//            } catch (PackageManager.NameNotFoundException e) {
//                return false;
//            }
//        } else return true;
//        // logger.logEvent("Button_start_click");
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            boolean granted = false;
            AppOpsManager appOps = (AppOpsManager) this
                    .getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), this.getPackageName());
            if (mode == AppOpsManager.MODE_DEFAULT)
                granted = (this.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
            else granted = (mode == AppOpsManager.MODE_ALLOWED);

            return granted;
        } else return true;
        // logger.logEvent("Button_start_click");
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    // for Android, you should also log app deactivation
    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }
//Gọi sau setContentView

// Add to a button click handler
// // Đặt vào sự kiện muốn tracking, key này sẽ gửi lên FB analytic"
}
