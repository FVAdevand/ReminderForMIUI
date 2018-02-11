package fvadevand.reminderformiui.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import fvadevand.reminderformiui.R;
import fvadevand.reminderformiui.utilities.ReminderUtils;

/**
 * Created by Vladimir on 26.01.2018.
 *
 */

public class NotificationIntentService extends IntentService {

    private static final String LOG_TAG = NotificationIntentService.class.getSimpleName();

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        boolean isStartForeground = false;
        String action = intent.getAction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (action.equals(NotificationTask.ACTION_RESEND_NOTIFICATIONS) || action.equals(NotificationTask.ACTION_NOTIFY_DELAYED_NOTIFICATION)) {
                NotificationChannel channel = new NotificationChannel(ReminderUtils.SERVICE_REMINDER_NOTIFICATION_CHANNEL_ID,
                        getString(R.string.service_notification_channel_name),
                        NotificationManager.IMPORTANCE_LOW);

                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                int imageId = sharedPreferences.getInt(getString(R.string.pref_image_key), ReminderUtils.getDefaultImageId());

                Notification notification = new NotificationCompat.Builder(this, ReminderUtils.SERVICE_REMINDER_NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(ReminderUtils.getMonocolorImageId(imageId))
                        .setContentTitle("update notifications")
                        .setOngoing(true)
                        .build();

                startForeground(78598, notification);
                isStartForeground = true;
            }
        }

        Bundle notificationBundle = intent.getBundleExtra(NotificationTask.NOTIFICATION_BUNDLE);
        NotificationTask.executeTask(this, action, notificationBundle);

        if (isStartForeground) {
            stopForeground(true);
        }
    }
}
