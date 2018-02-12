package fvadevand.reminderformiui.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;

/**
 * Created by Vladimir on 26.01.2018.
 *
 */

public class NotificationDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notificationDb.db";
    private static final int DATABASE_VERSION = 4;

    public NotificationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + NotificationEntry.TABLE_NAME + " (" +
                NotificationEntry._ID + " INTEGER PRIMARY KEY, " +
                NotificationEntry.COLUMN_IMAGE_ID + " INTEGER NOT NULL, " +
                NotificationEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NotificationEntry.COLUMN_MESSAGE + " TEXT, " +
                NotificationEntry.COLUMN_DATE + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NotificationEntry.TABLE_NAME);
        onCreate(db);
    }
}
