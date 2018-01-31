package fvadevand.reminderformiui.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Vladimir on 26.01.2018.
 */

public class NotificationContract {

    public static final String CONTENT_AUTHORITY = "fvadevand.reminderformiui";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NOTIFICATIONS = "notifications";

    private NotificationContract() {
    }

    public static final class NotificationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTIFICATIONS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTIFICATIONS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTIFICATIONS;

        public static final String TABLE_NAME = "notifications";
        public static final String COLUMN_IMAGE_ID = "image_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MESSAGE = "message";
    }
}
