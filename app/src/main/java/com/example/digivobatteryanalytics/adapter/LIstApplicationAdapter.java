package com.example.digivobatteryanalytics.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.digivobatteryanalytics.AppInfoInterface;
import com.example.digivobatteryanalytics.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LIstApplicationAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<AppInfoInterface> itemList = new ArrayList<>();

    public LIstApplicationAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void addItem(AppInfoInterface item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(int position, AppInfoInterface newItem) {
        itemList.set(position, newItem);
        notifyDataSetChanged();
    }

    public int getItemPosition(AppInfoInterface item) {
        return itemList.indexOf(item);
    }

    public boolean isItemExists(AppInfoInterface item) {
        return itemList.contains(item);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_application_inflater, null);
        }

        ImageView iconOfApps = convertView.findViewById(R.id.iconOfApps);
        TextView txtAppName = convertView.findViewById(R.id.txtAppName);
        TextView txtAppConsumtionDetil = convertView.findViewById(R.id.txtAppConsumtionDetil);
        ProgressBar progressBatteryUsage = convertView.findViewById(R.id.progressBatteryUsage);

        AppInfoInterface item = itemList.get(position);

        iconOfApps.setImageDrawable(item.packageIcon);
        txtAppName.setText(item.packageTitle);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setMaximumFractionDigits(2);
        String formattedNumber = decimalFormat.format(item.batteryPercentage);

        txtAppConsumtionDetil.setText(formattedNumber+"%, "+item.totalMah+" mAh");
        progressBatteryUsage.setProgress(Float.valueOf(item.batteryPercentage).intValue());

        return convertView;
    }
}
