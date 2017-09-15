package com.lhd.mvp.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.appevents.AppEventsLogger;
import com.lhd.applock.R;
import com.lhd.module.Config;
import com.lhd.mvp.logapp.MyLog;
import com.lhd.mvp.setpin.ChangePinActivity;

/**
 * Created by d on 8/23/2017.
 */

public class Settings extends AppCompatActivity {

    private AppEventsLogger logger;
    private NativeAd nativeAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        logger = AppEventsLogger.newLogger(this);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        TextView tvChangePin = (TextView) findViewById(R.id.settings_change_pin_code);
        tvChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.logEvent("Setting_change_pin_code_clicked");
//                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
//                View viewContent = View.inflate(Settings.this, R.layout.change_pin_layout, null);
//                builder.setView(viewContent);
//                Button btnSubmit = (Button) viewContent.findViewById(R.id.change_pin_code_btn_change);
//                final EditText edtInput1 = (EditText) viewContent.findViewById(R.id.change_pin_code_pin_1);
//                final EditText edtInput2 = (EditText) viewContent.findViewById(R.id.change_pin_code_pin_2);
//                final EditText edtInputCu = (EditText) viewContent.findViewById(R.id.change_pin_code_pin_cu);
//                final AlertDialog alertDialog = builder.create();
//                alertDialog.setView(viewContent);
//                btnSubmit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String input1 = edtInput1.getText().toString();
//                        String input2 = edtInput2.getText().toString();
//                        String inputCu = new String(Base64.encode(edtInputCu.getText().toString().getBytes(), 101)).trim();
//                        String pinCode = MyLog.getStringValueByName(Settings.this, Config.LOG_APP, Config.PIN_CODE).trim();
//                        if (input1.length() < 4)
//                            Toast.makeText(Settings.this, "Bạn hãy nhập đầy đủ 4 kí tự", Toast.LENGTH_SHORT).show();
//                        else if (!input1.equals(input2)) {
//                            Toast.makeText(Settings.this, "Mã pin nhập không giống nhau", Toast.LENGTH_SHORT).show();
//                        } else if (!inputCu.equals(pinCode)) {
//                            Toast.makeText(Settings.this, "Mã pin cũ của bạn không đúng", Toast.LENGTH_SHORT).show();
//                        } else {
//                            byte[] bytesEncoded = Base64.encode(input2.getBytes(), 101);
//                            MyLog.putStringValueByName(Settings.this, Config.LOG_APP, Config.PIN_CODE, new String(bytesEncoded));
//                            Toast.makeText(Settings.this, "Đã thay đổi mã pin dự phòng", Toast.LENGTH_SHORT).show();
//                            alertDialog.dismiss();
//                        }
//                    }
//                });
//                alertDialog.show();
                startActivity(new Intent(Settings.this, ChangePinActivity.class));
            }
        });
        LinearLayout changeMode = (LinearLayout) findViewById(R.id.settings_mode);
        changeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logger.logEvent("Setting_change_mode_clicked");
                Toast.makeText(Settings.this, getResources().getString(R.string.change_mode), Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout onOffFingerprint = (LinearLayout) findViewById(R.id.settings_enable_lock);
        final Switch swLock = (Switch) findViewById(R.id.settings_enable_lock_switch);
        onOffFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = !MyLog.getBooleanValueByName(Settings.this, Config.LOG_APP, Config.IS_FINGERPRINT);
                if (b) logger.logEvent("Setting_unlock_fingerprint_switch_on");
                else logger.logEvent("Setting_unlock_fingerprint_switch_off");
                MyLog.putBooleanValueByName(Settings.this, Config.LOG_APP, Config.IS_FINGERPRINT, !MyLog.getBooleanValueByName(Settings.this, Config.LOG_APP, Config.IS_FINGERPRINT));
                swLock.setChecked(MyLog.getBooleanValueByName(Settings.this, Config.LOG_APP, Config.IS_FINGERPRINT));
            }
        });
        swLock.setChecked(MyLog.getBooleanValueByName(Settings.this, Config.LOG_APP, Config.IS_FINGERPRINT));
        swLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    logger.logEvent("Setting_unlock_fingerprint_switch_on");

                else logger.logEvent("Setting_unlock_fingerprint_switch_off");
                MyLog.putBooleanValueByName(Settings.this, Config.LOG_APP, Config.IS_FINGERPRINT, b);
            }
        });
        nativeAd = new NativeAd(this, getResources().getString(R.string.SettingScreen_Footer_NativeAds_Height300));
        nativeAd.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
                NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                        .setButtonColor(getResources().getColor(R.color.background_ads))
                        .setButtonBorderColor(getResources().getColor(R.color.colorTran))
                        .setButtonTextColor(getResources().getColor(R.color.text_ads));
                View adView = NativeAdView.render(Settings.this, nativeAd, NativeAdView.Type.HEIGHT_300, viewAttributes);
                LinearLayout nativeAdContainer = (LinearLayout) findViewById(R.id.settings_layout_ads);
                nativeAdContainer.addView(adView);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });
        nativeAd.loadAd();

    }
}
