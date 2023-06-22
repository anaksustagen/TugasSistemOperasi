package com.example.digivobatteryanalytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryStatusReceiver extends BroadcastReceiver {

    private BatteryStatusListener listener;
    private float initialBatteryLevel;
    private long initialTimestamp;
    private boolean isCharging;


    private float cumulativeChargemAh;
    private float cumulativeDischargemAh;
    private long chargingStartTime;
    private long dischargingStartTime;

    private float lastBatteryPercentage;
    private long lastTime;
    private float batteryCapacity;

    public BatteryStatusReceiver(BatteryStatusListener listener, float batteryCapacity) {
        this.listener = listener;
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            long timestamp = System.currentTimeMillis();
            float batteryPercentage = (level / (float) scale) * 100.0f;

            if (initialBatteryLevel == 0) {
                initialBatteryLevel = (level / (float) scale) * 100;
                initialTimestamp = timestamp;
                isCharging = plugged > 0;
                return;
            }

            float currentBatteryLevel = (level / (float) scale) * 100;
            float mAhValue = ((initialBatteryLevel - currentBatteryLevel) / 100) * voltage;
            long timeElapsed = timestamp - initialTimestamp;

            if (isCharging && plugged == 0) {
                // Discharging
                mAhValue *= -1; // Convert to negative value to indicate discharge
            }

            long currentTime = System.currentTimeMillis();
            int chargingStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            if (lastBatteryPercentage > batteryPercentage) {
                cumulativeDischargemAh += calculatemAhValue(lastBatteryPercentage, batteryPercentage, timeElapsed);
            } else if (lastBatteryPercentage < batteryPercentage) {
                cumulativeChargemAh += calculatemAhValue(lastBatteryPercentage, batteryPercentage, timeElapsed);
            }

            if (chargingStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
                cumulativeChargemAh = calculatemAhValue(lastBatteryPercentage, batteryPercentage, timeElapsed);
                cumulativeDischargemAh = 0;
            }else if (chargingStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                cumulativeDischargemAh = calculatemAhValue(lastBatteryPercentage, batteryPercentage, timeElapsed);
                cumulativeChargemAh = 0;
            }

            lastBatteryPercentage = batteryPercentage;
            lastTime = currentTime;

//            listener.onBatteryStatusChanged(currentBatteryLevel, mAhValue, timeElapsed, chargingStatus);
            if (listener != null) {
                listener.onBatteryStatusChanged(batteryPercentage, cumulativeChargemAh,
                        cumulativeDischargemAh, chargingStatus, timeElapsed);
            }

            if (chargingStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
                handleChargingEvent(intent);
            } else if (chargingStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                handleDischargingEvent(intent);
            }
        }
    }

    private float calculatemAhValue(float startPercentage, float endPercentage, long timeElapsed) {
        float batteryCapacity = this.batteryCapacity;
        float mAhValue = batteryCapacity * (endPercentage - startPercentage) / 100.0f;
        float mAhPerHour = mAhValue / (timeElapsed / (1000.0f * 60.0f * 60.0f)); // Convert timeElapsed to hours

        Log.v("batteryCapacity", "batteryCapacity: "+ batteryCapacity);

        return mAhPerHour;
    }

    private void handleChargingEvent(Intent intent) {
        if (chargingStartTime == 0) {
            chargingStartTime = System.currentTimeMillis();
        }

        float currentCharge = intent.getIntExtra(String.valueOf(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW), 0) / 1000.0f;
        long chargingTimeElapsed = System.currentTimeMillis() - chargingStartTime;

        cumulativeChargemAh += (currentCharge * chargingTimeElapsed) / (1000 * 60 * 60); // Convert to mAh

        dischargingStartTime = 0; // Reset discharging start time
    }

    private void handleDischargingEvent(Intent intent) {
        if (dischargingStartTime == 0) {
            dischargingStartTime = System.currentTimeMillis();
        }

        float currentDischarge = intent.getIntExtra(String.valueOf(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW), 0) / 1000.0f;
        long dischargingTimeElapsed = System.currentTimeMillis() - dischargingStartTime;

        cumulativeDischargemAh += (currentDischarge * dischargingTimeElapsed) / (1000 * 60 * 60); // Convert to mAh

        chargingStartTime = 0; // Reset charging start time
    }

    public interface BatteryStatusListener {
        void onBatteryStatusChanged(float batteryPercentage, float cumulativeChargemAh,
                                    float cumulativeDischargemAh, int chargingStatus, long timeElapsed);
    }
}