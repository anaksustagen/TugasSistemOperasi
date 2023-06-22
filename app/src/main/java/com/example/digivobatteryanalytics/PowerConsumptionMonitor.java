package com.example.digivobatteryanalytics;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.BatteryManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class PowerConsumptionMonitor {
    private static final String TAG = "PowerConsumptionMonitor";
    private static final int INTERVAL_MILLISECONDS = 1000; // Interval to calculate power consumption (in milliseconds)

    private Context context;
    private ConnectivityManager.NetworkCallback networkCallback;
    private Timer powerConsumptionTimer;
    private float batteryLevelStart;
    private float powerConsumptionTotal;

    public PowerConsumptionMonitor(Context context) {
        this.context = context;
    }

    public void startMonitoring() {
        // Get the initial battery level
        batteryLevelStart = getBatteryLevel();

        // Start monitoring network changes
        startNetworkMonitoring();

        // Start timer to calculate power consumption
        powerConsumptionTimer = new Timer();
        powerConsumptionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                calculatePowerConsumption();
            }
        }, INTERVAL_MILLISECONDS, INTERVAL_MILLISECONDS);
    }

    public void stopMonitoring() {
        // Stop network monitoring
        stopNetworkMonitoring();

        // Stop the power consumption timer
        if (powerConsumptionTimer != null) {
            powerConsumptionTimer.cancel();
            powerConsumptionTimer = null;
        }
    }

    private void startNetworkMonitoring() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest.Builder requestBuilder = new NetworkRequest.Builder();
        requestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                // Network available
            }

            @Override
            public void onLost(Network network) {
                // Network lost
            }
        };

        connectivityManager.registerNetworkCallback(requestBuilder.build(), networkCallback);
    }

    private void stopNetworkMonitoring() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
            networkCallback = null;
        }
    }

    private void calculatePowerConsumption() {
        float batteryLevelEnd = getBatteryLevel();

        // Calculate the power consumption based on the battery level difference
        float powerConsumption = batteryLevelStart - batteryLevelEnd;
        powerConsumptionTotal += powerConsumption;

        // Update the battery level for the next interval
        batteryLevelStart = batteryLevelEnd;

        Log.d(TAG, "Power Consumption (mAh): " + powerConsumptionTotal);
    }

    private float getBatteryLevel() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale;
        return batteryPct * 100;
    }
}
