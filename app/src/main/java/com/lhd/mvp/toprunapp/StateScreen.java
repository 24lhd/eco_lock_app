package com.lhd.mvp.toprunapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lhd.mvp.lockapp.LockAppActivity;


/**
 * Created by D on 8/9/2017.
 */

public class StateScreen extends BroadcastReceiver {

    private static final int REQUEST_CODE = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if (!StateDeviceService.isRunning(context)) {
                StateDeviceService.startAlarm(context);
            }
            // Trigger package again
//            mLastPackageName = "";
//            startAlarm(AppLockService.this);
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            LockAppActivity.isUnlock = false;
//            stopAlarm(AppLockService.this);
            Log.e("faker", "ACTION_SCREEN_OFF");
            if (StateDeviceService.isRunning(context)) {
                Log.e("faker", "stopAlarm");
                StateDeviceService.stopAlarm(context);
            }

//            if (mRelockScreenOff) {
//                lockAll();
//            }
        }
    }


}