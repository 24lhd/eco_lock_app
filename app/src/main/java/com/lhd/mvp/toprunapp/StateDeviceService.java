package com.lhd.mvp.toprunapp;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.lhd.module.Config;
import com.lhd.mvp.lockapp.LockAppActivity;
import com.lhd.mvp.logapp.MyLog;
import com.lhd.sql.MySQL;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by D on 8/9/2017.
 */

public class StateDeviceService extends Service {
    private static final String TAG = "StateDeviceService";
    private ActivityManager mActivityManager;
    private WindowManager windowManager;
    private View viewLock;
    private boolean isShowLock;
    private ArrayList<String> listApp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (StateDeviceService.class.getName().equals(
                    service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private static PendingIntent running_intent;

    public static PendingIntent getRunIntent(Context context) {
        if (running_intent == null) {
            Intent intent = new Intent(context, StateDeviceService.class);
            running_intent = PendingIntent.getService(context, 2017, intent, 0);
        }
        return running_intent;
    }

    static AlarmManager alarmManager;

    public static void startAlarm(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = getRunIntent(context);
//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, startTime, 250, pendingIntent);
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, startTime, 1000, pendingIntent);
//        alarmManager.set(AlarmManager.RTC_WAKEUP,startTime,pendingIntent);
        if (Build.VERSION.SDK_INT < 23) {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), pendingIntent);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), pendingIntent);
        }
    }

    public static void stopAlarm(Context c) {
        AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);
        am.cancel(getRunIntent(c));
//        c.stopService(new Intent(c, StateDeviceService.class));
    }

    static MySQL mySQL;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mySQL == null) mySQL = new MySQL(StateDeviceService.this);
        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        listApp = mySQL.getAllItemApp();
        String topTask = getTopTask();
        String appUnLock = MyLog.getStringValueByName(StateDeviceService.this, Config.LOG_APP, Config.APP_UNLOCK);
        if (!topTask.equals(getPackageName()) && !topTask.equals(appUnLock)) {
            MyLog.putStringValueByName(StateDeviceService.this, Config.LOG_APP, Config.APP_UNLOCK, "");
            MyLog.putStringValueByName(StateDeviceService.this, Config.LOG_APP, Config.MY_APP_UNLOCK, "");
        }
        if (mySQL.isExistsItemApp(topTask, listApp) && LockAppActivity.isRunning == false
                && !appUnLock.equals(topTask)) {
            showWindowLog(getTopTask());
        }
//        if (topTask.equals(getPackageName()) && LockAppActivity.isRunning == false
//                && !MyLog.getStringValueByName(StateDeviceService.this, Config.LOG_APP, Config.APP_UNLOCK).equals(topTask)) {
//            Intent intentLock = new Intent(this, LockAppActivity.class);
//            intentLock.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intentLock);
//            isShowLock = true;
//        }
        if (!mySQL.isExistsItemApp(topTask, listApp) && !topTask.isEmpty() && LockAppActivity.isRunning == true) {
            hideWindowLog();
        }
        startAlarm(StateDeviceService.this);

        return START_STICKY;
    }

    private void hideWindowLog() {
        isShowLock = false;
//        if (viewLock != null) windowManager.removeViewImmediate(viewLock);
    }

    private void showWindowLog(String namePackage) {
        Intent intent = new Intent(this, LockAppActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Config.NAME_PACKAGE_ICON, namePackage);
        startActivity(intent);
        isShowLock = true;
    }

    private String getTopTask() {
        String topPackageName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
            // Sort the stats by the last time used
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
//                    Log.e("topPackageName", topPackageName);
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            topPackageName = mActivityManager.getRunningAppProcesses().get(0).processName;
//            Log.e("topPackageName", topPackageName);
        } else {
            topPackageName = (mActivityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
//            Log.e("topPackageName", topPackageName);
        }
        return topPackageName;

    }
}
