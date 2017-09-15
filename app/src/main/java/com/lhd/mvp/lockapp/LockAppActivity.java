package com.lhd.mvp.lockapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.facebook.appevents.AppEventsLogger;
import com.lhd.MyFingerPrint;
import com.lhd.applock.R;
import com.lhd.module.CircleIndicator;
import com.lhd.module.Config;
import com.lhd.mvp.logapp.MyLog;

import java.util.List;

/**
 * Created by d on 8/22/2017.
 */

//
//Gọi sau setContentView
//        FacebookSdk.sdkInitialize(getApplicationContext());
//Gửi mã tracking lên FB analytic
//        AppEventsLogger logger = AppEventsLogger.newLogger(this);
//
// Add to a button click handler
//        logger.logEvent(""Button_start_click"");
// Đặt vào sự kiện muốn tracking, key này sẽ gửi lên FB analytic"
public class LockAppActivity extends AppCompatActivity {
    public static boolean isRunning = false;
    public static boolean isUnlock = false;
    private ActivityManager mActivityManager;
    private ImageView imFringerprint;
    private ImageView imIconApp;
    private TextView tvTitleApp;
    private String lockPackage;
    private TextView tvStatePin;
    private AppEventsLogger logger;


    private String getAppNameFromPackage(String packageName, Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> pkgAppsList = context.getPackageManager()
                .queryIntentActivities(mainIntent, 0);

        for (ResolveInfo app : pkgAppsList) {
            if (app.activityInfo.packageName.equals(packageName)) {
                return app.activityInfo.loadLabel(context.getPackageManager()).toString();
            }
        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isUnlock = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && myFingerPrint != null) {
            myFingerPrint.stopListening();
        }
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    MyFingerPrint myFingerPrint;
    TextView tvPin;
    ImageView iconApp;
    LockAppActivity lockAppActivity;
    boolean isEnableLock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_lock_pin);
        lockAppActivity = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.background_app));
        }

        logger = AppEventsLogger.newLogger(this);
        lockPackage = getIntent().getStringExtra(Config.NAME_PACKAGE_ICON);
        tvPin = (TextView) findViewById(R.id.txt_pin_lock);
        tvStatePin = (TextView) findViewById(R.id.txt_noti_pin_lock);
        tvTitleApp = (TextView) findViewById(R.id.lock_view_tv_title_app);
        imFringerprint = (ImageView) findViewById(R.id.lock_view_im_ic_fingersprint);
        imIconApp = (ImageView) findViewById(R.id.lock_view_im_ic_app);
        isEnableLock = MyLog.getBooleanValueByName(this, Config.LOG_APP, Config.IS_FINGERPRINT);

        Drawable icon = null;

        tvTitleApp.setText(getAppNameFromPackage(lockPackage, this));
        try {
            icon = getPackageManager().getApplicationIcon(lockPackage);
            imIconApp.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        showHScroll();
        requestFingerprint();
    }

    private NativeAdsManager manager;
    private NativeAd nativeAd;
    private ViewPager vpr;

    public void showHScroll() {
        final int adsSize = 3;
        vpr = (ViewPager) findViewById(R.id.view_pager);
        manager = new NativeAdsManager(this, getResources().getString(R.string.LockAppsScreen_Header_ListNativeAds_Height300), adsSize);
        manager.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {
                try {
                    vpr.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            return FragmentADS.newInstance(manager);
                        }
                        @Override
                        public int getCount() {
                            return adsSize;
                        }
                    });
                    CircleIndicator indicatorView = (CircleIndicator) findViewById(R.id.indicator);
                    indicatorView.setViewPager(vpr);
                } catch (Exception e) {
                    Log.e("leuleu", e.getMessage());
                    Log.e("leuleu", "Exception");
                }
            }

            @Override
            public void onAdError(AdError adError) {
            }
        });
        manager.loadAds(NativeAd.MediaCacheFlag.ALL);

    }

    private void requestFingerprint() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                && MyLog.getBooleanValueByName(this, Config.LOG_APP, Config.IS_FINGERPRINT) == true) {
            myFingerPrint = new MyFingerPrint(LockAppActivity.this, new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    imFringerprint.setImageDrawable(getResources().getDrawable(R.drawable.ic_fingerprint_active));
                    logger.logEvent("ScreenLock_unlock_using_fingerprint");
                    unLockApp();
                }

                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onAuthenticationFailed() {
                    tvStatePin.setText(getResources().getString(R.string.wrong_finger));
                    myFingerPrint.stopListening();
                    requestFingerprint();
                }
            });
            imFringerprint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logger.logEvent("ScreenLocl_icon_fingerprint_clicked");
                }
            });
            if (isEnableLock && myFingerPrint.isFingerprintAuthAvailable()) {
                imFringerprint.setImageDrawable(getResources().getDrawable(R.drawable.ic_fingerprint_no_active));
            }
            if (myFingerPrint.isFingerprintAuthAvailable()) {
                logger.logEvent("Device_fingerprint_supported");
                myFingerPrint.initKey();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        requestFingerprint();
    }

    public void onPinCick(View view) {
        Button btPin = (Button) view;
        tvPin.setText(tvPin.getText().toString() + btPin.getText());
        tvStatePin.setText("");
        if (tvPin.getText().toString().length() == 4) {
            String pinCodeInput = new String(Base64.encode(tvPin.getText().toString().getBytes(), 101)).trim();
            String pinCodeTrue = MyLog.getStringValueByName(this, Config.LOG_APP, Config.PIN_CODE).trim();
            if (pinCodeInput.equals(pinCodeTrue)) {
                logger.logEvent("ScreenLock_unlock_using_pin");
                unLockApp();
            } else {
                tvStatePin.setText(getResources().getString(R.string.wrong_pin));
//                Toast.makeText(this, getResources().getString(R.string.wrong_pin), Toast.LENGTH_SHORT).show();
                tvPin.setText("");
            }
        }
    }

    private void unLockApp() {
        MyLog.putStringValueByName(this, Config.LOG_APP, Config.APP_UNLOCK, lockPackage);
//        Log.e("duong", "" + lockPackage);
//        Log.e("duong", "" + getPackageName());
//        Log.e("duong", "" + lockPackage.equals(getPackageName()));
//        if (lockPackage.equals(getPackageName())) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        } else {
        finish();
//        }
    }

    public void onBack(View view) {
        if (tvPin.getText().toString().length() > 0)
            tvPin.setText(tvPin.getText().toString().substring(0, tvPin.getText().toString().length() - 1));
    }


}
