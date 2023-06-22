package com.example.digivobatteryanalytics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class AnalyticsV3 extends AppCompatActivity {

    private ProgressBar batteryPercentageProgress;
    private TextView batteryPercentageText;
    private TextView txtBatterySubText;
    private static final int REQUEST_PACKAGE_USAGE_STATS = 1;
    private IntentFilter batteryIntentFilter;

    private long screenOnDuration = 0;
    private long screenOnTime = 0;
    private long screenOffTime = 0;
    private long screenOffDuration = 0;

    private BroadcastReceiver screenReceiver;
    private IntentFilter screenIntent;

    private TextView txtTotalTimeScreenOn;
    private TextView txtTotalTimeScreenOff;

    private TextView txtAnalyticsBattCurrent;
    private TextView txtAnalyticsAvgBattUsage;
    private TextView txtAnalyticsBattTemp;
    private TextView txtAnalyticsBattVoltage;

    private ProgressBar progressBatteryCurrent;
    private ProgressBar progressAvgBatteryUsage;
    private ProgressBar progressBatteryTemp;
    private ProgressBar progressBatteryVoltage;

    public long currentTime = 0;
    private double lastMahBattery = 0;

    private TextView lblBatteryHealth;
    private ProgressBar progressBatteryHealth;
    private TextView txtAnalyticsBattHealth;

    private TextView txtDevicesModel;
    private TextView txtAndroidVersion;
    private TextView txtBatteryCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_v3);

        batteryPercentageProgress = findViewById(R.id.batteryPercentageProgress);
        batteryPercentageText = findViewById(R.id.batteryPercentageText);
        txtBatterySubText = findViewById(R.id.txtBatterySubText);

        txtTotalTimeScreenOn = findViewById(R.id.txtTotalTimeScreenOn);
        txtTotalTimeScreenOff = findViewById(R.id.txtTotalTimeScreenOff);

        batteryPercentageProgress.setProgress(0);
        batteryPercentageText.setText("0%");
        txtBatterySubText.setText("Measuring Battery...");

        txtAnalyticsBattCurrent = findViewById(R.id.txtAnalyticsBattCurrent);
        txtAnalyticsAvgBattUsage = findViewById(R.id.txtAnalyticsAvgBattUsage);
        txtAnalyticsBattTemp = findViewById(R.id.txtAnalyticsBattTemp);
        txtAnalyticsBattVoltage = findViewById(R.id.txtAnalyticsBattVoltage);

        progressBatteryCurrent = findViewById(R.id.progressBatteryCurrent);
        progressAvgBatteryUsage = findViewById(R.id.progressAvgBatteryUsage);
        progressBatteryTemp = findViewById(R.id.progressBatteryTemp);
        progressBatteryVoltage = findViewById(R.id.progressBatteryVoltage);

        lblBatteryHealth = findViewById(R.id.lblBatteryHealth);
        progressBatteryHealth = findViewById(R.id.progressBatteryHealth);
        txtAnalyticsBattHealth = findViewById(R.id.txtAnalyticsBattHealth);

        txtAndroidVersion = findViewById(R.id.txtAndroidVersion);
        txtAndroidVersion.setText("("+Build.VERSION.SDK_INT+") " + getAndroidVersionName(Build.VERSION.SDK_INT));

        txtDevicesModel = findViewById(R.id.txtDevicesModel);
        txtDevicesModel.setText(Build.MANUFACTURER);

        txtBatteryCapacity = findViewById(R.id.txtBatteryCapacity);
        txtBatteryCapacity.setText(getBatteryCapacity() + " mA");

        currentTime = System.currentTimeMillis();

        //Ini pas mulai app langsung counting screen on timenya
        if(screenOnTime == 0){
            screenOnTime = System.currentTimeMillis();
        }

        screenReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    screenOnTime = System.currentTimeMillis();
                    Log.d("screenOnDuration", "A screenOnTime: "+ screenOnTime);
                    Log.d("screenOnDuration", "A screenOnTime: "+ screenOffTime);

                    if((screenOffTime > 0) && (screenOnTime > 0)){
                        screenOffDuration += screenOnTime - screenOffTime;
                    }

                    updateUiInformation();

                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    screenOffTime = System.currentTimeMillis();

                    Log.d("screenOnDuration", "B screenOnTime: "+ screenOnTime);
                    Log.d("screenOnDuration", "B screenOnTime: "+ screenOffTime);

                    if((screenOffTime > 0) && (screenOnTime > 0)){
                        screenOnDuration += screenOffTime - screenOnTime;
                    }

                    updateUiInformation();

                    Log.d("screenOnDuration", "ACTION_SCREEN_OFF: "+ screenOnDuration);
                }
            }
        };

        Context context = this;

        int MY_PERMISSION_REQUEST_GET_PACKAGE_SIZE = 1;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_PACKAGE_SIZE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_PACKAGE_SIZE},
                    MY_PERMISSION_REQUEST_GET_PACKAGE_SIZE);
        }else if (!isPackageUsageStatsPermissionGranted(this)) {
            requestPackageUsageStatsPermission(this, REQUEST_PACKAGE_USAGE_STATS);
        } else {
            initializeAnalytics(context);
        }
    }

    private String formatScreenOnDuration(long screenDurationParams) {
        long minutes = (screenDurationParams / (1000 * 60)) % 60;
        long hours = (screenDurationParams / (1000 * 60 * 60));
        long seconds = screenDurationParams / 1000;
        long days = hours / 24;

        if((hours == 0) && (minutes > 0)){
            return minutes + "M "+seconds + "S";
        }else if((hours > 0) && (days <= 0)){
            return hours + "H "+minutes + "M";
        }else if(days > 0){
            return days + "D "+hours + "H";
        }else{
            return "0M "+seconds+"S";
        }
    }

    private void updateUiInformation(){
        txtTotalTimeScreenOn.setText(formatScreenOnDuration(screenOnDuration));
        txtTotalTimeScreenOff.setText(formatScreenOnDuration(screenOffDuration));
    }

    private void initializeAnalytics(Context context){
        batteryIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryChangedTreceiver,batteryIntentFilter);

        screenIntent = new IntentFilter(Intent.ACTION_SCREEN_ON);
        screenIntent.addAction(Intent.ACTION_SCREEN_ON);
        screenIntent.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, screenIntent);
    }


    public int getCurrentBatteryCapacity(Intent batteryStatus) {

        int capacity = getBatteryCapacity().intValue();
        int voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);

        if (capacity > 0 && voltage > 0) {
            float batteryCapacity = (capacity / 100f) * (voltage / 1000f);
            return Math.round(batteryCapacity);
        } else {
            return -1; // Unable to retrieve battery capacity
        }
    }

    private BroadcastReceiver batteryChangedTreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPercentage = (level / (float) scale) * 100.0f;

            Log.v("BGSTEST", "level: " + level);
            Log.v("BGSTEST", "scale: " + scale);
            Log.v("BGSTEST", "percentage: " + batteryPercentage);

            //=getBatteryCapacity*(batteryPercentage/100)
            double currentMahValue = getCurrentBatteryCapacity(intent);
            if(lastMahBattery == 0){
                lastMahBattery = currentMahValue;
            }

            int batteryVol = (int) (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0));
            float fullVoltage = (float) (batteryVol * 0.001);

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            int batteryTempCelcius = temperature / 10;

            double currentMaDischarging = (lastMahBattery - currentMahValue) * fullVoltage;


            batteryPercentageProgress.setProgress(Float.valueOf(batteryPercentage).intValue());
            batteryPercentageText.setText(Float.valueOf(batteryPercentage).intValue() + "%");

            String fullText = "Battery at "+Float.valueOf(batteryPercentage).intValue() + "%"+" lasts for";
            String specialText = Float.valueOf(batteryPercentage).intValue() + "%";
            SpannableString spannableString = new SpannableString(fullText);
            int startIndex = fullText.indexOf(specialText);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#cc3b6b"));
            spannableString.setSpan(colorSpan, startIndex, startIndex + specialText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtBatterySubText.setText(spannableString);

            float batteryVoltageAvg = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) / 1000.0f; // Convert voltage from mV to V
            float batteryCapacity = getBatteryCapacity().floatValue(); // Get battery capacity in percentage
            float batteryCharge = level / (float) scale; // Calculate battery charge as a ratio
            float batteryUsage = batteryCapacity * batteryCharge; // Calculate average battery usage in percentage
//            float averageBatteryUsage = batteryUsage * batteryVoltageAvg; // Calculate average battery usage in mAh

            float averageBatteryUsage = (level / (float) scale) * batteryCapacity;;
            float dischargeCurrent = getBatteryDischargeCurrent(intent); // Convert to milliamperes

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            float currentMilliamps = (float) level * voltage / 1000f;

            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN);
            int batteryHealthPercentage = (health * 100) / BatteryManager.BATTERY_HEALTH_GOOD;
            double healthInMa = (getBatteryCapacity() * (batteryHealthPercentage / 100));

            // Display the average battery usage
//            System.out.println("Average Battery Usage: " + averageBatteryUsage + " mAh");

            txtAnalyticsBattCurrent.setText(getCurrentBatteryCapacity(intent) + " mAh");
            txtAnalyticsAvgBattUsage.setText(formatBeautifulFloat(averageBatteryUsage)+" mA/H");
            txtAnalyticsBattTemp.setText(batteryTempCelcius+"°C");
            txtAnalyticsBattVoltage.setText(formatBeautifulFloat(fullVoltage) + "V");


            progressBatteryHealth.setProgress(batteryHealthPercentage);
            txtAnalyticsBattHealth.setText(batteryHealthPercentage+"% ("+formatBeautifulFloat(Double.valueOf(healthInMa).floatValue())+" mA)");

            lastMahBattery = currentMahValue;
        }
    };

    public float getBatteryDischargeCurrent(Intent batteryStatus) {


        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int plugged = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);


        long elapsedTime = java.lang.System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime();
        float batteryPercentage = (level * 100) / (float) scale;

        Log.v("ELPTIME", "TIME: " + elapsedTime);
        Log.v("ELPTIME", "PRCT: " + batteryPercentage);

        float dischargeCurrent = -1;

        if (level != -1 && scale != -1) {
            if (status != BatteryManager.BATTERY_STATUS_CHARGING && plugged == 0) {
                dischargeCurrent = batteryPercentage / (elapsedTime / 1000f);
            }
        }

        return dischargeCurrent;
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

    private String formatBeautifulFloat(float amount){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setMaximumFractionDigits(2);
        String formattedNumber = decimalFormat.format(amount);

        return formattedNumber;
    }

    private boolean isPackageUsageStatsPermissionGranted(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    // Request the PACKAGE_USAGE_STATS permission
    private void requestPackageUsageStatsPermission(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        activity.startActivityForResult(intent, requestCode);
    }

    private String getAndroidVersionName(int sdkInt) {
        switch (sdkInt) {
            case Build.VERSION_CODES.BASE:
                return "Base";
            case Build.VERSION_CODES.BASE_1_1:
                return "Base 1.1";
            case Build.VERSION_CODES.CUPCAKE:
                return "Cupcake";
            case Build.VERSION_CODES.DONUT:
                return "Donut";
            case Build.VERSION_CODES.ECLAIR:
                return "Eclair";
            case Build.VERSION_CODES.ECLAIR_0_1:
                return "Eclair 0.1";
            case Build.VERSION_CODES.ECLAIR_MR1:
                return "Eclair MR1";
            case Build.VERSION_CODES.FROYO:
                return "Froyo";
            case Build.VERSION_CODES.GINGERBREAD:
                return "Gingerbread";
            case Build.VERSION_CODES.GINGERBREAD_MR1:
                return "Gingerbread MR1";
            case Build.VERSION_CODES.HONEYCOMB:
                return "Honeycomb";
            case Build.VERSION_CODES.HONEYCOMB_MR1:
                return "Honeycomb MR1";
            case Build.VERSION_CODES.HONEYCOMB_MR2:
                return "Honeycomb MR2";
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH:
                return "Ice Cream Sandwich";
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
                return "Ice Cream Sandwich MR1";
            case Build.VERSION_CODES.JELLY_BEAN:
                return "Jelly Bean";
            case Build.VERSION_CODES.JELLY_BEAN_MR1:
                return "Jelly Bean MR1";
            case Build.VERSION_CODES.JELLY_BEAN_MR2:
                return "Jelly Bean MR2";
            case Build.VERSION_CODES.KITKAT:
                return "KitKat";
            case Build.VERSION_CODES.KITKAT_WATCH:
                return "KitKat Watch";
            case Build.VERSION_CODES.LOLLIPOP:
                return "Lollipop";
            case Build.VERSION_CODES.LOLLIPOP_MR1:
                return "Lollipop MR1";
            case Build.VERSION_CODES.M:
                return "Marshmallow";
            case Build.VERSION_CODES.N:
                return "Nougat";
            case Build.VERSION_CODES.N_MR1:
                return "Nougat MR1";
            case Build.VERSION_CODES.O:
                return "Oreo";
            case Build.VERSION_CODES.O_MR1:
                return "Oreo MR1";
            case Build.VERSION_CODES.P:
                return "Pie";
            case Build.VERSION_CODES.Q:
                return "Android 10";
            case Build.VERSION_CODES.R:
                return "Android 11";
            case Build.VERSION_CODES.S:
                return "Android 12";
            case Build.VERSION_CODES.S_V2:
                return "Android 12";
            case Build.VERSION_CODES.TIRAMISU:
                return "Tiramisu";
            default:
                return "Unknown";
        }
    }
}