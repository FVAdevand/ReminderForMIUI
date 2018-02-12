package fvadevand.reminderformiui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;
import fvadevand.reminderformiui.service.NotificationIntentService;
import fvadevand.reminderformiui.service.NotificationTask;
import fvadevand.reminderformiui.utilities.ReminderUtils;

/**
 * Displays list of notifications that were entered and stored in the app.
 * Created by Vladimir on 29.01.2018.
 */

public class ReminderAdapter extends CursorAdapter {

    private static final String LOG_TAG = ReminderAdapter.class.getCanonicalName();

    ReminderAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.reminder_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView iconImageView = view.findViewById(R.id.iv_icon_reminder);
        int imageId = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_IMAGE_ID));
        iconImageView.setImageResource(imageId);

        TextView titleTextView = view.findViewById(R.id.tv_title_reminder);
        String title = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TITLE));
        titleTextView.setText(title);

        TextView messageTextView = view.findViewById(R.id.tv_message_reminder);
        String message = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_MESSAGE));
        if (TextUtils.isEmpty(message)) {
            messageTextView.setVisibility(View.GONE);
        } else {
            messageTextView.setText(message);
        }

        TextView timeTextView = view.findViewById(R.id.tv_time_reminder);
        long localTimeInMillis = ReminderUtils.getLocalTimeInMillis(cursor.getLong(cursor.getColumnIndex(NotificationEntry.COLUMN_DATE)));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localTimeInMillis);
        String timeReminderString = ReminderUtils.formatTime(context, calendar) + " " + ReminderUtils.formatFullDate(context, calendar);
        timeTextView.setText(timeReminderString);

        ImageButton deleteButton = view.findViewById(R.id.btn_delete_reminder);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentView = (View) v.getParent();
                ListView lv = (ListView) parentView.getParent();
                int position = lv.getPositionForView(parentView);
                int notificationId = (int) getItemId(position);
                Intent serviceIntent = new Intent(v.getContext(), NotificationIntentService.class);
                serviceIntent.setAction(NotificationTask.ACTION_DELETE_NOTIFICATION);

                Bundle notificationBundle = new Bundle();
                notificationBundle.putInt(NotificationEntry._ID, notificationId);

                serviceIntent.putExtra(NotificationTask.NOTIFICATION_BUNDLE, notificationBundle);
                v.getContext().startService(serviceIntent);
                Snackbar snackbar = Snackbar.make(lv, R.string.message_reminder_deleted, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        deleteButton.setFocusable(false);

        ImageButton notifyButton = view.findViewById(R.id.btn_notify_reminder);
        if (localTimeInMillis > System.currentTimeMillis()) {
            notifyButton.setVisibility(View.VISIBLE);
            timeTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            notifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View parentView = (View) v.getParent();
                    ListView lv = (ListView) parentView.getParent();
                    int position = lv.getPositionForView(parentView);
                    int notificationId = (int) getItemId(position);

                    Intent serviceIntent = new Intent(v.getContext(), NotificationIntentService.class);
                    serviceIntent.setAction(NotificationTask.ACTION_UPDATE_NOTIFICATION);

                    Bundle notificationBundle = new Bundle();
                    notificationBundle.putInt(NotificationEntry._ID, notificationId);
                    notificationBundle.putLong(NotificationEntry.COLUMN_DATE, ReminderUtils.getUtcTimeInMillis(Calendar.getInstance()));

                    serviceIntent.putExtra(NotificationTask.NOTIFICATION_BUNDLE, notificationBundle);
                    v.getContext().startService(serviceIntent);
                    Snackbar snackbar = Snackbar.make(lv, R.string.message_notification_shown, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            });
            notifyButton.setFocusable(false);
        } else {
            notifyButton.setVisibility(View.GONE);
            timeTextView.setTextColor(ContextCompat.getColor(context, R.color.message_notification_text));
        }
    }
}
