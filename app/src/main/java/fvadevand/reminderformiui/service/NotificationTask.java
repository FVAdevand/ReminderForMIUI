package fvadevand.reminderformiui.service;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;
import fvadevand.reminderformiui.utilities.ReminderUtils;

/**
 * Created by Vladimir on 27.01.2018.
 */

public class NotificationTask {

    public static final String ACTION_RESEND_NOTIFICATION = "fvadevand.reminderformiui.resend_notification";
    public static final String ACTION_DELETE_NOTIFICATION = "fvadevand.reminderformiui.delete_notification";
    public static final String ACTION_DELETE_ALL_NOTIFICATION = "fvadevand.reminderformiui.delete_all_notification";
    public static final String ACTION_UPDATE_NOTIFICATION = "fvadevand.reminderformiui.update_notification";
    public static final String ACTION_SEND_NOTIFICATION = "fvadevand.reminderformiui.send_notification";
    public static final String NOTIFICATION_BUNDLE = "fvadevand.reminderformiui.notification_bundle";
    private static final String LOG_TAG = NotificationTask.class.getSimpleName();

    private NotificationTask() {
    }

    static void executeTask(Context context, String action, Bundle bundle) {

        switch (action) {
            case ACTION_RESEND_NOTIFICATION:
                resendNotification(context);
                break;
            case ACTION_DELETE_NOTIFICATION:
                deleteNotification(context, bundle);
                break;
            case ACTION_DELETE_ALL_NOTIFICATION:
                deleteAllNotifications(context);
                break;
            case ACTION_SEND_NOTIFICATION:
                sendNotification(context, bundle);
                break;
            case ACTION_UPDATE_NOTIFICATION:
                updateNotification(context, bundle);
                break;
        }
    }

    private static void resendNotification(Context context) {
        Cursor cursor = context.getContentResolver().query(
                NotificationEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            int notificationId = cursor.getInt(cursor.getColumnIndex(NotificationEntry._ID));
            int imageId = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_IMAGE_ID));
            String title = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TITLE));
            String message = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_MESSAGE));
            ReminderUtils.sendNotification(context, notificationId, imageId, title, message);
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

        int imageId = notificationBundle.getInt(NotificationEntry.COLUMN_IMAGE_ID);
        String title = notificationBundle.getString(NotificationEntry.COLUMN_TITLE);
        String message = notificationBundle.getString(NotificationEntry.COLUMN_MESSAGE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationEntry.COLUMN_IMAGE_ID, imageId);
        contentValues.put(NotificationEntry.COLUMN_TITLE, title);
        contentValues.put(NotificationEntry.COLUMN_MESSAGE, message);

        int rowsUpdated = context.getContentResolver().update(contentUri, contentValues, null, null);
        if (rowsUpdated > 0) {
            ReminderUtils.sendNotification(context, id, imageId, title, message);
        }
    }

    private static void sendNotification(Context context, Bundle notificationBundle) {
        int imageId = notificationBundle.getInt(NotificationEntry.COLUMN_IMAGE_ID);
        String title = notificationBundle.getString(NotificationEntry.COLUMN_TITLE);
        String message = notificationBundle.getString(NotificationEntry.COLUMN_MESSAGE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationEntry.COLUMN_IMAGE_ID, imageId);
        contentValues.put(NotificationEntry.COLUMN_TITLE, title);
        contentValues.put(NotificationEntry.COLUMN_MESSAGE, message);

        Uri contentUri = context.getContentResolver().insert(NotificationEntry.CONTENT_URI, contentValues);
        if (contentUri != null) {
            int id = (int) ContentUris.parseId(contentUri);
            ReminderUtils.sendNotification(context, id, imageId, title, message);
        }
    }
}
