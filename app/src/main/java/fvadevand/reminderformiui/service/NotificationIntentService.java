package fvadevand.reminderformiui.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

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
        String action = intent.getAction();
        Log.e(LOG_TAG, action);
        int notificationId = intent.getIntExtra(ReminderUtils.KEY_NOTIFICATION_ID, 0);
        Log.e(LOG_TAG, notificationId + "");
        NotificationTask.executeTask(this, action, notificationId);
    }
}
