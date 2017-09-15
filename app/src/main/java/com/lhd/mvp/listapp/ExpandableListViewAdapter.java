package com.lhd.mvp.listapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;
import com.lhd.applock.R;

import java.util.ArrayList;

/**
 * CodePro - ThangLQ
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<Group> mData;
    private LayoutInflater mLayoutInflater;
    private TextView tvTitle;
    private ImageView imIconApp;
    private ImageView imStateLockApp;
    private NativeAd nativeAd;
    private NativeAd nativeAdFooter;

    // Hàm khởi tạo
    public ExpandableListViewAdapter(Context mContext, ArrayList<Group> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);


    }

    // Số phần tử của group item
    @Override
    public int getGroupCount() {
        return mData.size();
    }

    // Số phần tử child item trong 1 group
    @Override
    public int getChildrenCount(int i) {
        return mData.get(i).getChildArrayList().size();
    }

    // Trả về đối tượng của 1 group
    @Override
    public Object getGroup(int i) {
        return mData.get(i);
    }

    // Trả về đối tượng của 1 child
    @Override
    public Object getChild(int i, int i1) {
        return mData.get(i).getChildArrayList().get(i1);
    }

    // Trả về group id
    @Override
    public long getGroupId(int i) {
        return i;
    }

    // Trả về child id
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    // Trả về groupview
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
//        ViewHolderGroup viewHolderGroup;
//        if (view == null) {
//            viewHolderGroup = new ViewHolderGroup();
        view = mLayoutInflater.inflate(R.layout.item_group, viewGroup, false);
        tvTitle = (TextView) view.findViewById(R.id.item_grpoup_txt_title);
        if (i == 0) {
            nativeAd = new NativeAd(mContext, mContext.getResources().getString(R.string.SettingScreen_Footer_NativeAds_Height300));
            final View finalView = view;
            nativeAd.setAdListener(new AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                            .setButtonColor(mContext.getResources().getColor(R.color.background_ads))
                            .setButtonBorderColor(mContext.getResources().getColor(R.color.colorTran))
                            .setButtonTextColor(mContext.getResources().getColor(R.color.text_ads));
                    View adView = NativeAdView.render(mContext, nativeAd, NativeAdView.Type.HEIGHT_120, viewAttributes);
                    LinearLayout nativeAdContainer = (LinearLayout) finalView.findViewById(R.id.layout_ads_in_list_header);
                    nativeAdContainer.setVisibility(View.VISIBLE);
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
//            viewHolderGroup.ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
//            view.setTag(viewHolderGroup);
//        } else {
//            viewHolderGroup = (ViewHolderGroup) view.getTag();
//        }
//        if (b) {
//            viewHolderGroup.ivArrow.setImageResource(R.drawable.ic_arrow_up);
//        } else {
//            viewHolderGroup.ivArrow.setImageResource(R.drawable.ic_arrow_down);
//        }
//        Group group = mData.get(i);
        tvTitle.setText(mData.get(i).getName());
//        viewHolderGroup.tvGroup.setText(group.getName());
        return view;
    }

    // Trả về childview
    @Override
    public View getChildView(int i, int i1, boolean b, final View view, ViewGroup viewGroup) {
        View viewItemApp = View.inflate(mContext, R.layout.item_app, null);
        imIconApp = (ImageView) viewItemApp.findViewById(R.id.item_app_id_im_ic_app);
        final LinearLayout linearLayout = (LinearLayout) viewItemApp.findViewById(R.id.item_app_id_layout_item);
        imStateLockApp = (ImageView) viewItemApp.findViewById(R.id.item_app_id_im_state_app_lock);
        try {
            if (mData.get(i).getChildArrayList().get(i1).isLock()) {
                imStateLockApp.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_lock_outline_light_green_a400_36dp));
            } else {
                imStateLockApp.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_lock_open_white_36dp));
            }
        } catch (Exception e) {
//            imStateLockApp.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_lock_open_white_36dp));
        }
        TextView tvTitle = (TextView) viewItemApp.findViewById(R.id.item_app_id_txv_title_app);
        imIconApp.setImageDrawable(mData.get(i).getChildArrayList().get(i1).getIconApp());
        tvTitle.setText(mData.get(i).getChildArrayList().get(i1).getNameApp());
        if (i == 1 && i1 == mData.get(1).getChildArrayList().size() - 1) {
            Log.e("leuleu", "load roi");
            nativeAdFooter = new NativeAd(mContext, mContext.getResources().getString(R.string.SettingScreen_Footer_NativeAds_Height300));
            nativeAdFooter.setAdListener(new AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                            .setButtonColor(mContext.getResources().getColor(R.color.background_ads))
                            .setButtonBorderColor(mContext.getResources().getColor(R.color.colorTran))
                            .setButtonTextColor(mContext.getResources().getColor(R.color.text_ads));
                    View adView = NativeAdView.render(mContext, nativeAdFooter, NativeAdView.Type.HEIGHT_300, viewAttributes);
                    LinearLayout nativeAdContainer = (LinearLayout) linearLayout.findViewById(R.id.list_app_ads_footer);
                    nativeAdContainer.setVisibility(View.VISIBLE);
                    nativeAdContainer.addView(adView);
                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            });
            nativeAdFooter.loadAd();
        }
        return viewItemApp;
    }

    // Cho phép click vào child item hay không
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}
