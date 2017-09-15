package com.lhd.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhd.applock.R;
import com.lhd.module.ItemApp;

import java.util.ArrayList;

/**
 * Created by D on 8/14/2017.
 */

public class AdaptorListApp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    ArrayList<ItemApp> itemApps;

    public AdaptorListApp(Context context, ArrayList<ItemApp> listAppLocked) {
        this.context = context;
        this.itemApps = listAppLocked;
    }

    class ItemAppHolder extends RecyclerView.ViewHolder {
        ImageView imIconApp, imStateLockApp;
        TextView tvTitle;

        public ItemAppHolder(View itemView) {
            super(itemView);
            imIconApp = (ImageView) itemView.findViewById(R.id.item_app_id_im_ic_app);
            imStateLockApp = (ImageView) itemView.findViewById(R.id.item_app_id_im_state_app_lock);
            tvTitle = (TextView) itemView.findViewById(R.id.item_app_id_txv_title_app);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_app, null);
        return new ItemAppHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemAppHolder itemAppHolder = (ItemAppHolder) holder;
        itemAppHolder.imIconApp.setImageDrawable(itemApps.get(position).getIconApp());
        itemAppHolder.imStateLockApp.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_lock_outline_white_48dp));

        itemAppHolder.tvTitle.setText(itemApps.get(position).getNameApp());
    }

    @Override
    public int getItemCount() {
        return itemApps.size();
    }
}
