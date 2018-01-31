package fvadevand.reminderformiui.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;

/**
 * Created by Vladimir on 30.01.2018.
 */

public class NotificationProvider extends ContentProvider {

    public static final String LOG_TAG = NotificationProvider.class.getSimpleName();
    private static final int NOTIFICATIONS = 100;
    private static final int NOTIFICATIONS_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(NotificationContract.CONTENT_AUTHORITY, NotificationContract.PATH_NOTIFICATIONS, NOTIFICATIONS);
        sUriMatcher.addURI(NotificationContract.CONTENT_AUTHORITY, NotificationContract.PATH_NOTIFICATIONS + "/#", NOTIFICATIONS_ID);
    }

    private NotificationDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new NotificationDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case NOTIFICATIONS:
                cursor = db.query(NotificationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case NOTIFICATIONS_ID:
                selection = NotificationEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(NotificationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case NOTIFICATIONS:
                return insertNotifications(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertNotifications(Uri uri, ContentValues values) {
        String title = values.getAsString(NotificationEntry.COLUMN_TITLE);
        if (TextUtils.isEmpty(title)) {
            throw new IllegalArgumentException("notification requires a title");
        }

        Integer imageId = values.getAsInteger(NotificationEntry.COLUMN_IMAGE_ID);
        if (imageId == null) {
            throw new IllegalArgumentException("notification requires a image_id");
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newRowId = db.insert(NotificationEntry.TABLE_NAME,
                null,
                values);
        if (newRowId == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, newRowId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case NOTIFICATIONS:
                rowsDeleted = db.delete(NotificationEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case NOTIFICATIONS_ID:
                selection = NotificationEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(NotificationEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case NOTIFICATIONS:
                return updateNotification(uri, values, selection, selectionArgs);
            case NOTIFICATIONS_ID:
                selection = NotificationEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNotification(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateNotification(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(NotificationEntry.COLUMN_TITLE)) {
            String title = values.getAsString(NotificationEntry.COLUMN_TITLE);
            if (TextUtils.isEmpty(title)) {
                throw new IllegalArgumentException("notification requires a title");
            }
        }

        if (values.containsKey(NotificationEntry.COLUMN_IMAGE_ID)) {
            Integer imageId = values.getAsInteger(NotificationEntry.COLUMN_IMAGE_ID);
            if (imageId == null) {
                throw new IllegalArgumentException("notification requires a image_id");
            }
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated = db.update(NotificationEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case NOTIFICATIONS:
                return NotificationEntry.CONTENT_LIST_TYPE;
            case NOTIFICATIONS_ID:
                return NotificationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri);
        }
    }


}
