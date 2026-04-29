package com.example.apptuhorasalud.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.apptuhorasalud.HomeActivity;
import com.example.apptuhorasalud.R;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "tu_hora_salud_alarms";
    public static final String EXTRA_ALARM_ID = "alarmId";
    public static final String EXTRA_MEDICINE_NAME = "medicineName";
    public static final String EXTRA_DOSE = "dose";

    @Override
    public void onReceive(Context context, Intent intent) {
        int alarmId = intent.getIntExtra(EXTRA_ALARM_ID, 0);
        String medicineName = intent.getStringExtra(EXTRA_MEDICINE_NAME);
        String dose = intent.getStringExtra(EXTRA_DOSE);

        if (medicineName == null) medicineName = "Medicamento";
        if (dose == null) dose = "";

        ensureChannel(context);

        Intent contentIntent = new Intent(context, HomeActivity.class);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        PendingIntent contentPI = PendingIntent.getActivity(context, alarmId, contentIntent, flags);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Hora de tomar " + medicineName)
                .setContentText("Dosis: " + dose)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Dosis: " + dose))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setContentIntent(contentPI);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(alarmId, builder.build());
    }

    private void ensureChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Recordatorios de medicamentos",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Notificaciones para tomar tus medicamentos a tiempo");
                nm.createNotificationChannel(channel);
            }
        }
    }
}
