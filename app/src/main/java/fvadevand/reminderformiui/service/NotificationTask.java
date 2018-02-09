package fvadevand.reminderformiui.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;
import fvadevand.reminderformiui.utilities.ReminderUtils;

/**
 * Created by Vladimir on 27.01.2018.
 *
 */

public class NotificationTask {

    public static final String ACTION_RESEND_NOTIFICATIONS = "fvadevand.reminderformiui.resend_notifications";
    public static final String ACTION_DELETE_NOTIFICATION = "fvadevand.reminderformiui.delete_notification";
    public static final String ACTION_DELETE_ALL_NOTIFICATIONS = "fvadevand.reminderformiui.delete_all_notifications";
    public static final String ACTION_UPDATE_NOTIFICATION = "fvadevand.reminderformiui.update_notification";
    public static final String ACTION_INSERT_NOTIFICATION = "fvadevand.reminderformiui.insert_notification";
    public static final String NOTIFICATION_BUNDLE = "fvadevand.reminderformiui.notification_bundle";
    private static final String ACTION_NOTIFY_DELAYED_NOTIFICATION = "fvadevand.reminderformiui.notify_delayed_notification";
    private static final String LOG_TAG = NotificationTask.class.getSimpleName();

    private NotificationTask() {
    }

    static void executeTask(Context context, String action, Bundle bundle) {

        switch (action) {
            case ACTION_RESEND_NOTIFICATIONS:
                resendNotifications(context);
                break;
            case ACTION_DELETE_NOTIFICATION:
                deleteNotification(context, bundle);
                break;
            case ACTION_DELETE_ALL_NOTIFICATIONS:
                deleteAllNotifications(context);
                break;
            case ACTION_INSERT_NOTIFICATION:
                insertNotification(context, bundle);
                break;
            case ACTION_UPDATE_NOTIFICATION:
                updateNotification(context, bundle);
                break;
            case ACTION_NOTIFY_DELAYED_NOTIFICATION:
                ReminderUtils.notifyNotification(context, bundle);
                break;
        }
    }

    private static void resendNotifications(Context context) {
        Cursor cursor = context.getContentResolver().query(
                NotificationEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            Bundle notificationBundle = getBundleFromCursor(cursor);
            if (isDelay(notificationBundle)) {
                startDelayNotification(context, notificationBundle);
            } else {
                ReminderUtils.notifyNotification(context, notificationBundle);
            }
        }
        cursor.close();
    }

    private static void deleteNotification(Context context, Bundle bundle) {
        int rowId = bundle.getInt(NotificationEntry._ID);
        Uri uri = ContentUris.withAppendedId(NotificationEntry.CONTENT_URI, rowId);

        int rowsDeleted = context.getContentResolver().delete(uri, null, null);
        if (rowsDeleted > 0) {
            ReminderUtils.deleteNotification(context, rowId);
        }
    }

    private static void deleteAllNotifications(Context context) {
        int rowsDeleted = context.getContentResolver().delete(NotificationEntry.CONTENT_URI, null, null);
        if (rowsDeleted > 0) {
            ReminderUtils.deleteAllNotifications(context);
        }
    }

    private static void updateNotification(Context context, Bundle notificationBundle) {
        int id = notificationBundle.getInt(NotificationEntry._ID);
        Uri contentUri = ContentUris.withAppendedId(NotificationEntry.CONTENT_URI, id);

        ContentValues contentValues = getContentValues(notificationBundle);

        int rowsUpdated = context.getContentResolver().update(contentUri, contentValues, null, null);
        if (rowsUpdated > 0) {
            if (isDelay(notificationBundle)) {
                startDelayNotification(context, notificationBundle);
            } else {
                ReminderUtils.notifyNotification(context, notificationBundle);
            }
        }
    }

    private static void insertNotification(Context context, Bundle notificationBundle) {

        ContentValues contentValues = getContentValues(notificationBundle);
        Uri contentUri = context.getContentResolver().insert(NotificationEntry.CONTENT_URI, contentValues);
        if (contentUri != null) {
            int notificationId = (int) ContentUris.parseId(contentUri);
            notificationBundle.putInt(NotificationEntry._ID, notificationId);
            if (isDelay(notificationBundle)) {
                startDelayNotification(context, notificationBundle);
            } else {
                ReminderUtils.notifyNotification(context, notificationBundle);
            }
        }
    }

    private static ContentValues getContentValues(Bundle bundle) {
        int imageId = bundle.getInt(NotificationEntry.COLUMN_IMAGE_ID);
        String title = bundle.getString(NotificationEntry.COLUMN_TITLE);
        String message = bundle.getString(NotificationEntry.COLUMN_MESSAGE);
        long utcTimeInMillis = bundle.getLong(NotificationEntry.COLUMN_DATE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationEntry.COLUMN_IMAGE_ID, imageId);
        contentValues.put(NotificationEntry.COLUMN_TITLE, title);
        contentValues.put(NotificationEntry.COLUMN_MESSAGE, message);
        contentValues.put(NotificationEntry.COLUMN_DATE, utcTimeInMillis);

        return contentValues;
    }

    private static Bundle getBundleFromCursor(Cursor cursor) {
        int notificationId = cursor.getInt(cursor.getColumnIndex(NotificationEntry._ID));
        int imageId = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_IMAGE_ID));
        String title = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TITLE));
        String message = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_MESSAGE));
        long utcTimeInMillis = cursor.getLong(cursor.getColumnIndex(NotificationEntry.COLUMN_DATE));

        Bundle notificationBundle = new Bundle();
        notificationBundle.putInt(NotificationEntry._ID, notificationId);
        notificationBundle.putString(NotificationEntry.COLUMN_TITLE, title);
        notificationBundle.putString(NotificationEntry.COLUMN_MESSAGE, message);
        notificationBundle.putInt(NotificationEntry.COLUMN_IMAGE_ID, imageId);
        notificationBundle.putLong(NotificationEntry.COLUMN_DATE, utcTimeInMillis);

        return notificationBundle;
    }

    private static void startDelayNotification(Context context, Bundle notificationBundle) {

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int id = notificationBundle.getInt(NotificationEntry._ID);

        Intent serviceIntent = new Intent(context, NotificationIntentService.class);
        serviceIntent.setAction(ACTION_NOTIFY_DELAYED_NOTIFICATION);
        serviceIntent.putExtra(NOTIFICATION_BUNDLE, notificationBundle);

        PendingIntent alarmIntent = PendingIntent.getService(context, id, serviceIntent, 0);

        long utcTimeInMillis = notificationBundle.getLong(NotificationEntry.COLUMN_DATE);
        long localTimeInMillis = ReminderUtils.getLocalTimeInMillis(utcTimeInMillis);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, localTimeInMillis, alarmIntent);
    }

    private static boolean isDelay(Bundle notificationBundle) {
        long utcTimeNotificationInMillis = notificationBundle.getLong(NotificationEntry.COLUMN_DATE);
        long localTimeNotificationInMillis = ReminderUtils.getLocalTimeInMillis(utcTimeNotificationInMillis);

//        Calendar currentCal = Calendar.getInstance();
//        currentCal.setTimeInMillis(System.currentTimeMillis());
//        Calendar utcCal = Calendar.getInstance();
//        utcCal.setTimeInMillis(utcTimeNotificationInMillis);
//        Calendar localCal = Calendar.getInstance();
//        localCal.setTimeInMillis(localTimeNotificationInMillis);
//        Log.i(LOG_TAG, "notif " + ReminderUtils.formatTime(context, localCal) + " " + ReminderUtils.formatShortDate(context, localCal));
//        Log.i(LOG_TAG, "UTC " + ReminderUtils.formatTime(context, utcCal) + " " + ReminderUtils.formatShortDate(context, utcCal));
//        Log.i(LOG_TAG, "current " + ReminderUtils.formatTime(context, currentCal) + " " + ReminderUtils.formatShortDate(context, currentCal));

        return localTimeNotificationInMillis > System.currentTimeMillis();
    }
}
