package fvadevand.reminderformiui.data;

import android.provider.BaseColumns;

/**
 * Created by Vladimir on 26.01.2018.
 */

public class NotificationContract {
    public static final class NotificationEntry implements BaseColumns {
        public static final String TABLE_NAME = "notifications";
        public static final String COLUMN_NOTIFICATIONS_ID = "notifications_id";
        public static final String COLUMN_IMAGE_ID = "image_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MESSAGE = "message";
    }
}
