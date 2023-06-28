package com.example.digivobatteryanalytics.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.example.digivobatteryanalytics.AnalyticsV3;
import com.example.digivobatteryanalytics.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class General {

    public static int generateNotificationId() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        return uniqueId.hashCode();
    }

    public static void showWASettingsDialog(Activity context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String whatsAppNumber = sharedPreferences.getString("config_wa_number", "");

        // Create a dialog box
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);

        AlertDialog  alertDialog = dialogBuilder.create();

        // Get references to the dialog components
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        EditText dialogInput = dialogView.findViewById(R.id.dialog_input);
        Button dialogClose = dialogView.findViewById(R.id.dialog_close);
        Button dialogSave = dialogView.findViewById(R.id.dialog_save);

        // Set an InputFilter to accept only WhatsApp numbers
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuilder sb = new StringBuilder();
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (Character.isDigit(c) || c == '+' || c == '(' || c == ')' || c == ' ' || c == '-') {
                        sb.append(c);
                    }
                }
                return sb.toString();
            }
        };
        dialogInput.setFilters(new InputFilter[]{filter});

        // Set the dialog title
        dialogTitle.setText("Whatsapp Number");
        dialogInput.setText(whatsAppNumber);

        // Set click listeners for the buttons
        AlertDialog finalAlertDialog1 = alertDialog;
        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the dialog
                finalAlertDialog1.dismiss();
            }
        });

        AlertDialog finalAlertDialog = alertDialog;
        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text input from the dialog
                String inputText = dialogInput.getText().toString();
                sharedPreferences.edit().putString("config_wa_number", inputText).commit();

                // Close the dialog
                finalAlertDialog.dismiss();
            }
        });

        // Create the AlertDialog object


        // Show the dialog
        alertDialog.show();
    }

    public static String getAlertMessage(String type){
        String alertMessage = "";

        if(type != null){
            if(type.equals("morethan85")){
                alertMessage = "\uD83D\uDD0B *BATERAI SUDAH TERISI 85%*\n" +
                        "\n" +
                        "Untuk menghindari over-charge dan membuat baterai lebih awet, disarankan untuk mencabut charger yang terhubung pada smartphone Anda.";
            }else if(type.equals("belowthan20")){
                alertMessage = "\uD83E\uDEAB *BATERAI DI BAWAH 20%*\n" +
                        "\n" +
                        "Untuk menghindari kehabisan daya, silahkan charge smartphone Anda sekarang juga.";
            }else if(type.equals("fastestdraining")){
                alertMessage = "⚠️ *PENURUNAN BATERAI TERLALU CEPAT*\n" +
                        "\n" +
                        "Dalam waktu beberapa menit terakhir kami menemukan bahwa baterai smartphone Anda telah turun 5% atau lebih.\n" +
                        "Silahkan cek lagi penggunaan aplikasi/proses latar belakang yang mungkin menjadi penyebabnya.";
            }
        }
        return alertMessage;
    }

    public static boolean sendingAlertToWhatsapp(String type, Context context){
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String whatsAppNumber = sharedPreferences.getString("config_wa_number", "");

            if((whatsAppNumber != null) && (!whatsAppNumber.equals(""))){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("auth_id", "462b970aacb92cc9de3c9329d45eb221|878546a820f2885b2e5f3e50ae6fa731");
                jsonObject.put("to", sharedPreferences.getString("config_wa_number", ""));
                jsonObject.put("msg", getAlertMessage(type));

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
                Request request = new Request.Builder()
                        .url("https://hulkcore.watzap.id/sendmessagebalance")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                String responseBodyString = responseBody.string();

                Log.v("responseBodyString", responseBodyString);

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(responseBodyString);


                return jsonResponse.getBoolean("status");
            }else{
                return false;
            }


        } catch (IOException | JSONException e) {
//            throw new RuntimeException(e);

            Log.v("RuntimeException", e.getMessage());

            return false;
        }
    }

    public static void showHeadsUpNotification(Context context, String title, String message, Map<String,PendingIntent> actButtons) {
        // Create a unique notification channel ID
        String channelId = "digibatt_alert_notification";

        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Digibatt Alert Notification";
            String channelDescription = "For Alert Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.setDescription(channelDescription);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Create an intent to handle the action buttons
        Intent intent = new Intent(context, AnalyticsV3.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long[] vibrationPattern = {0, 100, 200, 300, 400, 500, 600};

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setContentIntent(pendingIntent)
                .setVibrate(vibrationPattern)
                .setAutoCancel(true);

        //For Automatic Adding Button If Exist
        if(actButtons != null){
            for (Map.Entry<String, PendingIntent> entry : actButtons.entrySet()) {
                String key = entry.getKey();
                PendingIntent value = entry.getValue();
                builder.addAction(0, key, value);
            }
        }

        // Show the notification
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(generateNotificationId(), builder.build());
    }

}
