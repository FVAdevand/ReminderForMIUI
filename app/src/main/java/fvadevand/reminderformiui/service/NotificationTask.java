package fvadevand.reminderformiui.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;
import fvadevand.reminderformiui.data.NotificationDbHelper;
import fvadevand.reminderformiui.utilities.ReminderUtils;

/**
 * Created by Vladimir on 27.01.2018.
 */

public class NotificationTask {

    public static final String ACTION_RESEND_NOTIFICATION = "resend_notification";
    public static final String ACTION_DELETE_NOTIFICATION = "delete_notification";

    private NotificationTask() {
    }

    static void executeTask(Context context, String action, int notificationId) {
        if (action.equals(ACTION_RESEND_NOTIFICATION)) {
            resendNotification(context);
        } else if (action.equals(ACTION_DELETE_NOTIFICATION)) {
            deleteNotification(context, notificationId);
        }
    }

    private static void resendNotification(Context context) {
        NotificationDbHelper dbHelper = new NotificationDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(NotificationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            int notificationId = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_NOTIFICATIONS_ID));
            int imageId = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_IMAGE_ID));
            String title = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TITLE));
            String message = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_MESSAGE));
            ReminderUtils.sendNotification(context, notificationId, imageId, title, message);
        }
        cursor.close();
    }

    private static void deleteNotification(Context context, int rowId) {
        NotificationDbHelper dbHelper = new NotificationDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = NotificationEntry.COLUMN_NOTIFICATIONS_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(rowId)};
        int deletedRows = db.delete(NotificationEntry.TABLE_NAME,
                selection,
                selectionArgs);
        if (deletedRows > 0) {
            ReminderUtils.deleteNotification(context, rowId);
        }
    }

}
