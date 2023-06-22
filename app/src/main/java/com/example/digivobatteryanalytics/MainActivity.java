package com.example.digivobatteryanalytics;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements BatteryStatusReceiver.BatteryStatusListener {

    private TextView tvBatteryLevel;
    private TextView tvmAh;
    private TextView tvTime;
    private TextView tvEvent;
    private TextView tvBatteryCapacity;
    private TextView tvBatteryTemp;
    private TextView tvWifiAvg;

    private int initialBatteryLevel;
    private long initialTime;
    private Handler handler;

    private BatteryStatusReceiver batteryStatusReceiver;
    private PowerConsumptionMonitor powerMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBatteryLevel = findViewById(R.id.tvBatteryLevel);
        tvmAh = findViewById(R.id.tvmAh);
        tvTime = findViewById(R.id.tvTime);
        tvEvent = findViewById(R.id.tvEvent);
        tvBatteryCapacity = findViewById(R.id.tvBatteryCapacity);
        tvBatteryTemp = findViewById(R.id.tvBatteryTemp);
        tvWifiAvg = findViewById(R.id.tvWifiAvg);

        registerBatteryStatusReceiver();

        powerMonitor = new PowerConsumptionMonitor(getApplicationContext());
        powerMonitor.startMonitoring();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBatteryStatusReceiver();

        if(handler != null){
            handler.removeCallbacks(batteryUpdateRunnable);
        }

        if(powerMonitor != null){
            powerMonitor.startMonitoring();
        }

    }

    private void registerBatteryStatusReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatusReceiver = new BatteryStatusReceiver(this, getBatteryCapacity().floatValue());
        registerReceiver(batteryStatusReceiver, filter);
    }

    private void unregisterBatteryStatusReceiver() {
        if (batteryStatusReceiver != null) {
            unregisterReceiver(batteryStatusReceiver);
            batteryStatusReceiver = null;
        }
    }

    private Runnable batteryUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            // Register the receiver to get the current battery level
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = registerReceiver(null, filter);

            // Calculate elapsed time and battery level difference
            long currentTime = System.currentTimeMillis();
            int currentBatteryLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int batteryLevelDiff = initialBatteryLevel - currentBatteryLevel;
            long timeDiff = currentTime - initialTime;

            // Calculate average mA value using Ohm's law (I = ΔQ / Δt)
            float averageCurrent = (batteryLevelDiff / (float) timeDiff) * 1000; // Convert to mA

            // Display the average mA value
//            System.out.println("Average Current: " + averageCurrent + " mA");

            double avgWifiPowerConsumtion = getAverageWifiCurrent(getApplicationContext());

            tvmAh.setText("Average Current: " + averageCurrent + " mA");
            tvWifiAvg.setText("WiFi Avg: " + avgWifiPowerConsumtion + " mA");

            // Schedule the next update
            handler.postDelayed(this, 1000); // Update every second
        }
    };

    @Override
    public void onBatteryStatusChanged(float batteryPercentage, float cumulativeChargemAh, float cumulativeDischargemAh, int chargingStatus, long timeElapsed) {

        int batteryCapacity = getBatteryCapacity().intValue();

        int batteryRealCapacity = (int)Math.round((batteryPercentage / 100d) * batteryCapacity);
        float mAhValueNew = batteryCapacity * batteryPercentage / 100.0f;

        tvBatteryCapacity.setText("Capacity: " + batteryCapacity + " mAh");
        tvBatteryTemp.setText("Temp: " + getBatteryTemperature() + " °C");

        tvBatteryLevel.setText("Battery Level: " + batteryPercentage + "% / " + batteryRealCapacity + " mAh");
//        tvmAh.setText("mAh: " + cumulativeChargemAh);
        tvTime.setText("Time Elapsed: " + timeElapsed + " ms");

        if (chargingStatus == BatteryManager.BATTERY_STATUS_CHARGING || chargingStatus == BatteryManager.BATTERY_STATUS_FULL) {
            //Charge
            tvEvent.setText("State: Charging");
        } else if (chargingStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            //Discharge
            tvEvent.setText("State: Discharge");
        } else {
            //Iddle
            tvEvent.setText("State: Unknown");
        }

        if(handler == null){
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = registerReceiver(null, filter);

            // Store initial battery level and time
            initialBatteryLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            initialTime = System.currentTimeMillis();

            Log.v("initialBatteryLevel", "initialBatteryLevel: " + initialBatteryLevel);
            Log.v("initialBatteryLevel", "initialTime: " + initialTime);

            // Start periodic updates
            handler = new Handler();
            handler.postDelayed(batteryUpdateRunnable, 1000); // Update every second
        }
    }

    private double getBatteryTemperature() {
        Intent intent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

        float temperatureCelsius = temperature / 10.0f;
        return temperatureCelsius;
    }


    public Double getBatteryCapacity() {

        // Power profile class instance
        Object mPowerProfile_ = null;

        // Reset variable for battery capacity
        double batteryCapacity = 0;

        // Power profile class name
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {

            // Get power profile class and create instance. We have to do this
            // dynamically because android.internal package is not part of public API
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);

        } catch (Exception e) {

            // Class not found?
            e.printStackTrace();
        }

        try {

            // Invoke PowerProfile method "getAveragePower" with param "battery.capacity"
            batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");

        } catch (Exception e) {

            // Something went wrong
            e.printStackTrace();
        }

        return batteryCapacity;
    }

    private double getAverageWifiCurrent(Context context) {
        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check if Wi-Fi is connected
        boolean isWifiConnected = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network[] networks = connectivityManager.getAllNetworks();
            for (Network network : networks) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    isWifiConnected = true;
                    break;
                }
            }
        } else {
            // Deprecated method for older Android versions
            // Use this block if you're targeting API levels below 23
            isWifiConnected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        }

        // Get the average mA usage of Wi-Fi
        if (isWifiConnected && batteryManager != null) {
            double averageCurrent = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000.0;
            return averageCurrent;
        }


        return 0.0; // Wi-Fi is not connected or unable to retrieve the average current
    }

}