package com.lhd.mvp.lockapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeAdsManager;
import com.lhd.applock.R;

/**
 * Created by d on 9/14/2017.
 */

public class FragmentADS extends Fragment {

    private NativeAdsManager nativeAdsManager;
    private NativeAd nativeAdFm;


    public static FragmentADS newInstance(NativeAdsManager manager) {
        FragmentADS fragment = new FragmentADS();
        fragment.setADS(manager);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_native_ad_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showNativeAd();
    }

    private void showNativeAd() {
        nativeAdFm = nativeAdsManager.nextNativeAd();
        NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                .setButtonColor(getResources().getColor(R.color.background_ads))
                .setButtonBorderColor(getResources().getColor(R.color.colorTran))
                .setButtonTextColor(getResources().getColor(R.color.text_ads));
        View adView = NativeAdView.render(getContext(), nativeAdFm,
                NativeAdView.Type.HEIGHT_300, viewAttributes);
        LinearLayout nativeAdContainer =
                (LinearLayout) getView().findViewById(R.id.native_ad_containerlist);
        nativeAdContainer.addView(adView);
    }

    public void setADS(NativeAdsManager ADS) {
        this.nativeAdsManager = ADS;
    }
}