package fvadevand.reminderformiui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;
import fvadevand.reminderformiui.service.NotificationIntentService;
import fvadevand.reminderformiui.service.NotificationTask;
import fvadevand.reminderformiui.utilities.ReminderUtils;

/**
 * Displays list of notifications that were entered and stored in the app.
 * Created by Vladimir on 29.01.2018.
 */

public class NotificationAdapter extends CursorAdapter {

    private static final String LOG_TAG = NotificationAdapter.class.getCanonicalName();

    NotificationAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.custom_push, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView iconImageView = view.findViewById(R.id.large_image_IV);
        iconImageView.setImageResource(cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_IMAGE_ID)));

        TextView titleTextView = view.findViewById(R.id.title_TV);
        titleTextView.setText(cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TITLE)));

        TextView messageTextView = view.findViewById(R.id.message_TV);
        String message = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_MESSAGE));
        if (TextUtils.isEmpty(message)) {
            messageTextView.setVisibility(View.GONE);
        } else {
            messageTextView.setText(message);
        }

        ImageButton deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentView = (View) v.getParent();
                ListView lv = (ListView) parentView.getParent();
                int position = lv.getPositionForView(parentView);
                int notificationId = (int) getItemId(position);
                Intent serviceIntent = new Intent(v.getContext(), NotificationIntentService.class);
                serviceIntent.setAction(NotificationTask.ACTION_DELETE_NOTIFICATION);
                serviceIntent.putExtra(ReminderUtils.KEY_NOTIFICATION_ID, notificationId);
                v.getContext().startService(serviceIntent);
            }
        });
        deleteButton.setFocusable(false);
    }
}
