package com.lhd.mvp.lockapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.lhd.mvp.main.MainActivity;

import java.util.List;

/**
 * Created by d on 8/29/2017.
 */

public class LockAppFragment extends Fragment implements View.OnClickListener {
    private ImageView imFringerprint;
    private ImageView imIconApp;
    private TextView tvTitleApp;
    private TextView tvStatePin;
    private AppEventsLogger logger;
    MyFingerPrint myFingerPrint;
    TextView tvPin;
    ImageView iconApp;
    boolean isEnableLock;
    private View viewContent;

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

    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.window_lock_pin_frangment, null);
        logger = AppEventsLogger.newLogger(getActivity());
        tvPin = (TextView) viewContent.findViewById(R.id.txt_pin_lock);
        mainActivity = (MainActivity) getActivity();
        mainActivity.hideBar();
        mainActivity.hideAllPermisstion();
        tvStatePin = (TextView) viewContent.findViewById(R.id.txt_noti_pin_lock);
        tvTitleApp = (TextView) viewContent.findViewById(R.id.lock_view_tv_title_app);
        imFringerprint = (ImageView) viewContent.findViewById(R.id.lock_view_im_ic_fingersprint);
        imIconApp = (ImageView) viewContent.findViewById(R.id.lock_view_im_ic_app);
        isEnableLock = MyLog.getBooleanValueByName(getActivity(), Config.LOG_APP, Config.IS_FINGERPRINT);
        tvTitleApp.setText(getResources().getString(R.string.app_name));
        Button btn1 = (Button) viewContent.findViewById(R.id.btn_lock_fragment_1);
        Button btn2 = (Button) viewContent.findViewById(R.id.btn_lock_fragment_2);
        Button btn3 = (Button) viewContent.findViewById(R.id.btn_lock_fragment_3);
        Button btn4 = (Button) viewContent.findViewById(R.id.btn_lock_fragment_4);
        Button btn5 = (Button) viewContent.findViewById(R.id.btn_lock_fragment_5);
        Button btn6 = (Button) viewContent.findViewById(R.id.btn_lock_fragment_6);
        Button btn7 = (Button) viewContent.findViewById(R.id.btn_lock_fragment_7);
        Button btn8 = (Button) viewContent.findViewById(R.id.btn_lock_fragment_8);
        Button btn9 = (Button) viewContent.findViewById(R.id.btn_lock_fragment_9);
        Button btn0 = (Button) viewContent.findViewById(R.id.btn_lock_fragment_0);
        ImageView btnBack = (ImageView) viewContent.findViewById(R.id.btn_lock_fragment_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvPin.getText().toString().length() > 0)
                    tvPin.setText(tvPin.getText().toString().substring(0, tvPin.getText().toString().length() - 1));
            }
        });
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        imIconApp.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        return viewContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestFingerprint();
        showHScroll();
    }


    private void requestFingerprint() {
        boolean isUseFinger = true;
        try {
            isUseFinger = MyLog.getBooleanValueByName(mainActivity, Config.LOG_APP, Config.IS_FINGERPRINT);
        } catch (Exception e) {
            isUseFinger = false;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                && isUseFinger) {
            myFingerPrint = new MyFingerPrint(mainActivity, new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    imFringerprint.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.ic_fingerprint_active));
                    logger.logEvent("ScreenLock_unlock_using_fingerprint");
                    unLockApp();
                }

                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onAuthenticationFailed() {
                    try {
                        tvStatePin.setText(getResources().getString(R.string.wrong_finger));
                    } catch (IllegalStateException e) {

                    }
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
            if (myFingerPrint.isFingerprintAuthAvailable()) {
                logger.logEvent("Device_fingerprint_supported");
                myFingerPrint.initKey();
            }
        }

    }

    private NativeAdsManager manager;
    private NativeAd nativeAd;
    private ViewPager vpr;

    public void showHScroll() {
        final int adsSize = 3;
        vpr = (ViewPager) viewContent.findViewById(R.id.view_pager_myapp);
        manager = new NativeAdsManager(mainActivity, getResources().getString(R.string.LockAppsScreen_Header_ListNativeAds_Height300), adsSize);
//        manager = new NativeAdsManager(getContext(), "YOUR_PLACEMENT_ID", adsSize);
        manager.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {
                vpr.setAdapter(new FragmentStatePagerAdapter(mainActivity.getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return FragmentADS.newInstance(manager);
                    }

                    @Override
                    public int getCount() {
                        return adsSize;
                    }
                });
                CircleIndicator indicatorView = (CircleIndicator) viewContent.findViewById(R.id.indicator_myapp);
                indicatorView.setViewPager(vpr);
            }

            @Override
            public void onAdError(AdError adError) {
            }
        });
        manager.loadAds(NativeAd.MediaCacheFlag.ALL);

    }

    private void unLockApp() {
        MyLog.putStringValueByName(mainActivity, Config.LOG_APP, Config.MY_APP_UNLOCK, mainActivity.getPackageName());
        mainActivity.startListAppFragment();
    }


    @Override
    public void onClick(View view) {
        Button btPin = (Button) view;
        tvPin.setText(tvPin.getText().toString() + btPin.getText());
        tvStatePin.setText("");
        if (tvPin.getText().toString().length() == 4) {
            String pinCodeInput = new String(Base64.encode(tvPin.getText().toString().getBytes(), 101)).trim();
            String pinCodeTrue = MyLog.getStringValueByName(mainActivity, Config.LOG_APP, Config.PIN_CODE).trim();
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
}
