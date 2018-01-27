package fvadevand.reminderformiui.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;
import fvadevand.reminderformiui.data.NotificationDbHelper;
import fvadevand.reminderformiui.utilities.ReminderUtils;

/**
 * Created by Vladimir on 26.01.2018.
 */

public class NotificationIntentService extends IntentService {

    private static final String LOG_TAG = NotificationIntentService.class.getSimpleName();

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationDbHelper dbHelper = new NotificationDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(NotificationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            int imageId = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_IMAGE_ID));
            String title = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TITLE));
            String message = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_MESSAGE));
            ReminderUtils.sendNotification(this, imageId, title, message);
        }
        cursor.close();
    }
}
