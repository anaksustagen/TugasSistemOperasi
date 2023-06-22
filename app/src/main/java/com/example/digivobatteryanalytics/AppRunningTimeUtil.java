package com.example.digivobatteryanalytics;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppRunningTimeUtil {
    private Context context;
    private Map<String, Long> startTimes;
    private double batteryCapacity_mAh;

    public AppRunningTimeUtil(Context context, double batteryCapacity_mAh) {
        this.context = context;
        this.startTimes = new HashMap<>();
        this.batteryCapacity_mAh = batteryCapacity_mAh;
    }

    public void startTimer() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
                startTimes.put(processInfo.processName, SystemClock.elapsedRealtime());
            }
        }
    }

    public Map<String, Double> getTotalRunningTimes() {
        Map<String, Double> totalRunningTimes = new HashMap<>();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager != null) {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - (24 * 60 * 60 * 1000); // One day ago (in milliseconds)

            UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, endTime);
            UsageEvents.Event event = new UsageEvents.Event();

            List<CustomEvent> eventList = new ArrayList<>();

            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                CustomEvent customEvent = new CustomEvent(
                        event.getPackageName(),
                        event.getTimeStamp(),
                        event.getEventType()
                );
                eventList.add(customEvent);
            }

            // Sort the event list based on event time in descending order
            Collections.sort(eventList, new Comparator<CustomEvent>() {
                @Override
                public int compare(CustomEvent event1, CustomEvent event2) {
                    return Long.compare(event2.getTimeStamp(), event1.getTimeStamp());
                }
            });

            // Calculate running time for each package
            for (CustomEvent sortedEvent : eventList) {
                if (sortedEvent.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND || sortedEvent.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    String packageName = sortedEvent.getPackageName();
                    long eventStartTime = sortedEvent.getTimeStamp();
                    long eventEndTime = endTime;

                    // Check if the package has a start time recorded
                    if (startTimes.containsKey(packageName)) {
                        eventEndTime = startTimes.get(packageName);
                        startTimes.remove(packageName); // Remove the start time for this package
                    }

                    long runningTimeMillis = eventEndTime - eventStartTime;
                    double totalmAh = calculateTotalmAh(runningTimeMillis, 500);
                    totalRunningTimes.put(packageName, totalmAh);
                } else if (sortedEvent.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    String packageName = sortedEvent.getPackageName();
                    long eventStartTime = sortedEvent.getTimeStamp();

                    // Store the start time for this package
                    startTimes.put(packageName, eventStartTime);
                }
            }
        }

        return totalRunningTimes;
    }

//    private double calculateTotalmAh(long runningTimeMillis) {
//        double mAh = (runningTimeMillis * batteryCapacity_mAh) / (24 * 60 * 60 * 1000); // Convert running time to days
//        return Math.min(mAh, batteryCapacity_mAh); // Ensure the calculated mAh does not exceed battery capacity
//    }

    private double calculateTotalmAh(long runningTimeMillis, double powerConsumption_mW) {
        double totalmAh = (runningTimeMillis * powerConsumption_mW) / (1000 * 3600);
//        System.out.println("Total mAh: " + totalmAh);

        return Math.min(totalmAh, batteryCapacity_mAh);
    }

    private double calculateTotalmAw(long runningTimeMillis, double powerConsumption_mW) {
        double mAw = (runningTimeMillis * powerConsumption_mW) / (60 * 60 * 1000); // Convert running time to hours
        return Math.min(mAw, batteryCapacity_mAh); // Ensure the calculated mAw does not exceed battery capacity
    }

    private static class CustomEvent {
        private String packageName;
        private long timeStamp;
        private int eventType;

        public CustomEvent(String packageName, long timeStamp, int eventType) {
            this.packageName = packageName;
            this.timeStamp = timeStamp;
            this.eventType = eventType;
        }

        public String getPackageName() {
            return packageName;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public int getEventType() {
            return eventType;
        }
    }
}
