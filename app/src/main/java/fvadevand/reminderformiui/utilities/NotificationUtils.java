package fvadevand.reminderformiui.utilities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.lang.reflect.Field;

import fvadevand.reminderformiui.MainActivity;
import fvadevand.reminderformiui.R;
import fvadevand.reminderformiui.data.NotificationContract;
import fvadevand.reminderformiui.service.NotificationIntentService;
import fvadevand.reminderformiui.service.NotificationTask;

/**
 * Created by Vladimir on 13.02.2018.
 */

public class NotificationUtils {

    public static final String MAIN_REMINDER_NOTIFICATION_CHANNEL_ID = "fvadevand.reminderformiui.main_reminder_notification_channel";
    public static final String SERVICE_REMINDER_NOTIFICATION_CHANNEL_ID = "fvadevand.reminderformiui.service_reminder_notification_channel";
    private static final String LOG_TAG = NotificationUtils.class.getSimpleName();

    // TODO: add sound and vibration to notification
    public static void notifyNotification(Context context, Bundle notificationBundle) {

        int notificationId = notificationBundle.getInt(NotificationContract.NotificationEntry._ID);
        int imageId = notificationBundle.getInt(NotificationContract.NotificationEntry.COLUMN_IMAGE_ID);
        String title = notificationBundle.getString(NotificationContract.NotificationEntry.COLUMN_TITLE);
        String message = notificationBundle.getString(NotificationContract.NotificationEntry.COLUMN_MESSAGE);

        RemoteViews notificationView = new RemoteViews(context.getPackageName(), R.layout.custom_push);
        notificationView.setImageViewResource(R.id.large_image_IV, imageId);
        notificationView.setTextViewText(R.id.title_TV, title);
        if (TextUtils.isEmpty(message)) {
            notificationView.setViewVisibility(R.id.message_TV, View.GONE);
        } else {
            notificationView.setTextViewText(R.id.message_TV, message);
        }

        Intent startActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, 0);

        Intent startServiceIntent = new Intent(context, NotificationIntentService.class);
        startServiceIntent.setAction(NotificationTask.ACTION_DELETE_NOTIFICATION);

        Bundle notificationIdBundle = new Bundle();
        notificationIdBundle.putInt(NotificationContract.NotificationEntry._ID, notificationId);

        startServiceIntent.putExtra(NotificationTask.NOTIFICATION_BUNDLE, notificationIdBundle);
        PendingIntent startServicePendingIntent = PendingIntent.getService(context, notificationId, startServiceIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.btn_clear, startServicePendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    MAIN_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        Notification notification = new NotificationCompat.Builder(context, MAIN_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(ReminderUtils.getMonocolorImageId(imageId))
                .setContent(notificationView)
                .setContentIntent(startActivityPendingIntent)
                .setOngoing(true)
                .build();

        prepareNotificationForMiui(notification);

        notificationManager.notify(notificationId, notification);
    }

    public static void deleteNotification(Context context, int notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;
        notificationManager.cancel(notificationId);
    }

    public static void deleteAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;
        notificationManager.cancelAll();
    }

    private static void prepareNotificationForMiui(Notification notification) {
        try {
            @SuppressLint("PrivateApi") Object miuiNotification = Class.forName("android.app.MiuiNotification").newInstance();
            Field customizedIconField = miuiNotification.getClass().getDeclaredField("customizedIcon");
            customizedIconField.setAccessible(true);
            customizedIconField.set(miuiNotification, true);

            Field extraNotificationField = notification.getClass().getField("extraNotification");
            extraNotificationField.setAccessible(true);
            extraNotificationField.set(notification, miuiNotification);
        } catch (Exception e) {
            Log.i(LOG_TAG, "OS is not MIUI");
        }
    }
}
