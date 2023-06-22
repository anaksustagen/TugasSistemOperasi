package com.example.digivobatteryanalytics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.Manifest;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.digivobatteryanalytics.adapter.LIstApplicationAdapter;

public class AnalyticsV2 extends AppCompatActivity {

    private HashMap<String, Long> uidRxBytesStartList =  new HashMap<String,Long>();
    private HashMap<String, Long> uidTxBytesStartList =  new HashMap<String,Long>();
    private HashMap<String, Long> startTimeUptimeScreen =  new HashMap<String,Long>();
    private HashMap<String, Long> screenTimeTotal =  new HashMap<String,Long>();

    private HashMap<String, AppInfoInterface> packageAnalticsList =  new HashMap<String,AppInfoInterface>();

    private Handler handler;
    private IntentFilter ifilter;
    private Intent batteryStatus;

    private static final long CHECK_INTERVAL = 60000; // 1 minute
    private static final long MAX_APP_USAGE_TIME = 300000; // 5 minutes

    IntentFilter intentfilter;
    private ListView listView;
    private LIstApplicationAdapter adapter;

    public double BatteryCapacityInmAh = 0;

    private ProgressBar batteryPercentageProgress;
    private TextView batteryPercentageText;
    private TextView loadingMeasuring;
    private IntentFilter screenIntent;

    private long screenOnTime = 0;
    private long screenOffTime = 0;
    private BroadcastReceiver screenReceiver;
    private long screenOnDuration = 0;

    private TextView screenOnPercentage;
    private TextView detilAnalyticsOfScreenOn;

    private TextView screenOffPercentage;
    private TextView detilAnalyticsOfScreenOff;
    private long screenOffDuration = 0;
    private long totalMahByApp = 0;

    private long startTimeAppRunning = 0;
    private long totalTimeAppRunning = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pusing_gan);

        screenOnPercentage = (TextView)findViewById(R.id.screenOnPercentage);
        detilAnalyticsOfScreenOn = (TextView)findViewById(R.id.detilAnalyticsOfScreenOn);

        screenOffPercentage = (TextView)findViewById(R.id.screenOffPercentage);
        detilAnalyticsOfScreenOff = (TextView)findViewById(R.id.detilAnalyticsOfScreenOff);

        batteryPercentageProgress = (ProgressBar) findViewById(R.id.batteryPercentageProgress);
        batteryPercentageProgress.setIndeterminate(false);
        batteryPercentageProgress.setProgress(0);
        batteryPercentageProgress.setMax(100);

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

                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    screenOffTime = System.currentTimeMillis();

                    Log.d("screenOnDuration", "B screenOnTime: "+ screenOnTime);
                    Log.d("screenOnDuration", "B screenOnTime: "+ screenOffTime);

                    if((screenOffTime > 0) && (screenOnTime > 0)){
                        screenOnDuration += screenOffTime - screenOnTime;
                    }


                    Log.d("screenOnDuration", "ACTION_SCREEN_OFF: "+ screenOnDuration);
//                    detilAnalyticsOfScreenOn.setText("1,650 mAh in 14h 50m");

                    // Do something with screenOnDuration, like saving it to a database or displaying it
                }
            }
        };

        Context context = this;

        int MY_PERMISSION_REQUEST_GET_PACKAGE_SIZE = 1;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_PACKAGE_SIZE)
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

    private void initializeAnalytics(Context context){
        startTimeAppRunning = System.currentTimeMillis();

        batteryPercentageText = (TextView) findViewById(R.id.batteryPercentageText);
        loadingMeasuring = (TextView) findViewById(R.id.loadingMeasuring);
        BatteryCapacityInmAh = getBatteryCapacity();

        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryChangedTreceiver,intentfilter);

        screenIntent = new IntentFilter(Intent.ACTION_SCREEN_ON);
        screenIntent.addAction(Intent.ACTION_SCREEN_ON);
        screenIntent.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, screenIntent);

        listView = findViewById(R.id.listView);
        adapter = new LIstApplicationAdapter(LayoutInflater.from(this));
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Context context = this;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PACKAGE_USAGE_STATS) {
            if (isPackageUsageStatsPermissionGranted(this)) {
                initializeAnalytics(context);
            }
        }
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

    // Example usage in an Activity
    private static final int REQUEST_PACKAGE_USAGE_STATS = 1;

    private int updateScreenTimeBatteryConsumtion(long screenOnDurationParams, int batteryVol){
        double screenPowerPerMillis = 0.0001;
        double screenEnergyConsumed = screenOnDurationParams * screenPowerPerMillis;
        int batteryVoltage = batteryVol; // Battery voltage in Volts
        float fullVoltage = (float) (batteryVoltage * 0.001);
        double totalCapacity = screenEnergyConsumed / batteryVoltage * 1000;

        int mahIntVersion = Double.valueOf(totalCapacity).intValue();

        return mahIntVersion;
    }

    private String formatScreenOnDuration(long screenDurationParams) {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
//        Date durationDate = new Date(screenDurationParams);
//        String formattedDuration = sdf.format(durationDate);

        long minutes = (screenDurationParams / (1000 * 60)) % 60;
        long hours = (screenDurationParams / (1000 * 60 * 60));

        return hours + " H "+minutes + " M";
    }

    private String formatBeautifulFloat(float amount){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setMaximumFractionDigits(2);
        String formattedNumber = decimalFormat.format(amount);

        return formattedNumber;
    }

    private BroadcastReceiver batteryChangedTreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPercentage = (level / (float) scale) * 100.0f;

            batteryPercentageProgress.setProgress(Float.valueOf(batteryPercentage).intValue());
            batteryPercentageText.setText(Float.valueOf(batteryPercentage).intValue() + "%");

            int batteryVol = (int) (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0));
            float fullVoltage = (float) (batteryVol * 0.001);

            int totalMahScreenOn = updateScreenTimeBatteryConsumtion(screenOnDuration, batteryVol);
            double batteryPercentageUsage = ((totalMahScreenOn / getBatteryCapacity()) * 100);
            float totalMahPercentageByScreenOn = Double.valueOf(batteryPercentageUsage).floatValue();

            screenOnPercentage.setText(formatBeautifulFloat(totalMahPercentageByScreenOn) + "%");
            detilAnalyticsOfScreenOn.setText(totalMahScreenOn + " mAh in " + formatScreenOnDuration(screenOnDuration));

            double batteryPercentageUsageByTmh = ((totalMahByApp / getBatteryCapacity()) * 100);
            float totalBatteryPercentageUsageByTmh = Double.valueOf(batteryPercentageUsageByTmh).floatValue();

            screenOffPercentage.setText(formatBeautifulFloat(totalBatteryPercentageUsageByTmh)+"%");
            detilAnalyticsOfScreenOff.setText(totalMahByApp+" mAh in " + formatScreenOnDuration(totalTimeAppRunning));

            Log.d("ForegroundApp", "============= BEGIN =============");
            List<String> listOfForegroundApps = getAllPackageRunningForeGround();
            for (String packageName : listOfForegroundApps) {
                Log.d("ForegroundApp", "Package Name: " + packageName);
                estimateBatteryUsageForPackage(getApplicationContext(), packageName, batteryVol);
            }
            Log.d("ForegroundApp", "============= END =============");

        }
    };

    public Drawable getIconFromPkgName(Context context, String packageName){
        try
        {
            Drawable icon = context.getPackageManager().getApplicationIcon(packageName);
            return icon;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String getAppNameFromPkgName(Context context, String Packagename) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(Packagename, PackageManager.GET_META_DATA);
            String appName = (String) packageManager.getApplicationLabel(info);
            return appName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
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


    public void estimateBatteryUsageForPackage(Context context, String packageName, int batteryVol) {
        PackageManager packageManager = context.getPackageManager();
        int uid;
        try {
            uid = packageManager.getPackageUid(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            // Package not found, handle the exception here
            e.printStackTrace();
            return;
        }


        if(!uidRxBytesStartList.containsKey(packageName)){
            uidRxBytesStartList.put(packageName, TrafficStats.getUidRxBytes(uid));
            uidTxBytesStartList.put(packageName, TrafficStats.getUidTxBytes(uid));
        }


        long uidRxBytesEnd = TrafficStats.getUidRxBytes(uid);
        long uidTxBytesEnd = TrafficStats.getUidTxBytes(uid);

        long networkUsage = (uidRxBytesEnd - uidRxBytesStartList.get(packageName)) + (uidTxBytesEnd - uidTxBytesStartList.get(packageName));

        // Calculate energy consumption based on network usage estimation
        double networkPowerPerByte = 0.000001; // Energy consumed per byte of network data (adjust according to device and network type)
        double networkEnergyConsumed = networkUsage * networkPowerPerByte;

        // Estimate battery consumption based on screen-on time
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = powerManager.isInteractive();
        } else {
            isScreenOn = powerManager.isScreenOn();
        }

        long screenOnTime = 0;
        if (isScreenOn) {

            if(!startTimeUptimeScreen.containsKey(packageName)){
                startTimeUptimeScreen.put(packageName, System.currentTimeMillis());
            }

            long uptime = System.currentTimeMillis() - startTimeUptimeScreen.get(packageName);
            screenOnTime = uptime;

            screenTimeTotal.put(packageName, screenOnTime);
        }

        double screenPowerPerMillis = 0.0001; // Energy consumed per millisecond of screen-on time (adjust according to device)
        double screenEnergyConsumed = 0;
        if(screenTimeTotal.containsKey(packageName)){
            screenEnergyConsumed = screenTimeTotal.get(packageName) * screenPowerPerMillis;
        }

        // Calculate total estimated battery consumption
        double totalEnergyConsumed = networkEnergyConsumed + screenEnergyConsumed;

        // Convert energy consumption to mAh (assuming battery voltage of 3.7V)
        int batteryVoltage = batteryVol; // Battery voltage in Volts
        float fullVoltage = (float) (batteryVoltage * 0.001);
        double totalCapacity = totalEnergyConsumed / batteryVoltage * 1000;

        int mahIntVersion = Double.valueOf(totalCapacity).intValue();

        if(mahIntVersion > 0){
            AppInfoInterface appInfoInterface = new AppInfoInterface();
            appInfoInterface.packageName = packageName;
            appInfoInterface.packageTitle = getAppNameFromPkgName(context, packageName);
            appInfoInterface.totalMah = mahIntVersion;
            appInfoInterface.batteryVoltage = fullVoltage;
            appInfoInterface.screenTime = screenTimeTotal.get(packageName);
            appInfoInterface.totalRxBytes = (uidRxBytesEnd - uidRxBytesStartList.get(packageName));
            appInfoInterface.totalTxBytes = (uidTxBytesEnd - uidTxBytesStartList.get(packageName));
            appInfoInterface.packageIcon = getIconFromPkgName(context, packageName);

            double batteryPercentageUsage = ((mahIntVersion / BatteryCapacityInmAh) * 100);
            appInfoInterface.batteryPercentage = Double.valueOf(batteryPercentageUsage).floatValue();

            packageAnalticsList.put(packageName, appInfoInterface);

//            if(!packageAnalticsList.containsKey(appInfoInterface)){
//                adapter.addItem(appInfoInterface);
//            }else{
//                adapter.updateItem(adapter.getItemPosition(appInfoInterface), appInfoInterface);
//            }

        }

        updateAdapterList();

        Log.d("ForegroundApp","Estimated battery usage for package " + packageName + ": " + mahIntVersion + " f("+totalCapacity+") mAh, voltage: " + batteryVoltage);
    }

    public void updateAdapterList(){
        if(packageAnalticsList.values().size() > 0){
            loadingMeasuring.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        int totalMahByAppInner = 0;

        adapter = new LIstApplicationAdapter(LayoutInflater.from(this));
        for (AppInfoInterface value : packageAnalticsList.values()) {
            totalMahByAppInner += value.totalMah;
//            System.out.println(value); // Replace with your own logic
            adapter.addItem(value);
        }
        listView.setAdapter(adapter);

        totalTimeAppRunning = System.currentTimeMillis() - startTimeAppRunning;

        totalMahByApp = totalMahByAppInner;
    }

    private List<String> getAllPackageRunningForeGround() {
        List<String> listPackage = new ArrayList<>();

        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - MAX_APP_USAGE_TIME;

        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startTime);

            List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, calendar.getTimeInMillis(), currentTime);
            if (usageStatsList != null) {
                for (UsageStats usageStats : usageStatsList) {
                    if (usageStats.getLastTimeUsed() >= startTime) {
                        String packageName = usageStats.getPackageName();
                        // Skip your own app's package name to avoid killing your own app
                        if (!packageName.equals(getPackageName())) {
                            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                            if (activityManager != null) {
                                listPackage.add(packageName);
                            }
                        }
                    }
                }
            }
        }

        return listPackage;
    }
}