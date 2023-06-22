package com.example.digivobatteryanalytics.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;

public class RAMUsageUtil {

    public static long getRAMUsageByPackage(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(new int[]{getPidByPackageName(context, packageName)});
            if (memoryInfoArray.length > 0) {
                Debug.MemoryInfo memoryInfo = memoryInfoArray[0];
                return memoryInfo.getTotalPrivateDirty() * 1024; // Convert to bytes
            }
        }
        return 0;
    }

    private static int getPidByPackageName(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
                if (processInfo.processName.equals(packageName)) {
                    return processInfo.pid;
                }
            }
        }
        return 0;
    }

}
