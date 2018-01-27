package fvadevand.reminderformiui.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Vladimir on 27.01.2018.
 */

public class RebootReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = RebootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, NotificationIntentService.class);
        context.startService(startServiceIntent);
    }
}
