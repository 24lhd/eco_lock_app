package com.lhd.mvp.toprunapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by d on 8/28/2017.
 */

public class MyService extends Service {
    private StateScreen stateScreen;

    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (MyService.class.getName().equals(
                    service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    private void unListenOnOffSceen() {
        if (stateScreen != null)
            unregisterReceiver(stateScreen);
    }

    private void listenOnOffSceen() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        stateScreen = new StateScreen();
        registerReceiver(stateScreen, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        listenOnOffSceen();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unListenOnOffSceen();
        super.onDestroy();
    }
}
