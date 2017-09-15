package com.lhd.mvp.setpin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lhd.applock.R;
import com.lhd.module.Config;

/**
 * Created by d on 8/28/2017.
 */

public class ChangePinActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pin_activity);
        SetPinFragment setPinFragment = new SetPinFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Config.IS_CHANGE_PIN, true);
        setPinFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_change_pin, setPinFragment).commit();
    }
}
