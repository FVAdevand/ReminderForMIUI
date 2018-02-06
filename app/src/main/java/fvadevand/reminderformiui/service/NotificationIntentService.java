package fvadevand.reminderformiui.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

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
//        int notificationId = intent.getIntExtra(ReminderUtils.KEY_NOTIFICATION_ID, 0);
        Bundle notificationBundle = intent.getBundleExtra(NotificationTask.NOTIFICATION_BUNDLE);
        NotificationTask.executeTask(this, action, notificationBundle);
    }
}
