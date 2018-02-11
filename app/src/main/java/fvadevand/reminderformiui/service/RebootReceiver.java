package fvadevand.reminderformiui.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by Vladimir on 27.01.2018.
 *
 */

public class RebootReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = RebootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, NotificationIntentService.class);
        startServiceIntent.setAction(NotificationTask.ACTION_RESEND_NOTIFICATIONS);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            context.startService(startServiceIntent);
        } else {
            context.startForegroundService(startServiceIntent);
        }
    }
}
