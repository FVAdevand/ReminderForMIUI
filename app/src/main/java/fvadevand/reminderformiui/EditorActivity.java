package fvadevand.reminderformiui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

import java.lang.reflect.Field;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mTitleET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        Button sendCloseButton = findViewById(R.id.send_close_button);
        sendCloseButton.setOnClickListener(this);

        mTitleET = findViewById(R.id.title_ET);
        mTitleET.requestFocus();

    }

    @Override
    public void onClick(View view) {

        int buttonId = view.getId();

        EditText messageET = findViewById(R.id.message_ET);

        String title = mTitleET.getText().toString();
        String message = messageET.getText().toString();

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.custom_push);
        notificationView.setImageViewResource(R.id.large_image_IV, R.drawable.ic_done_black_24px);
        notificationView.setTextViewText(R.id.title_TV, title);
        notificationView.setTextViewText(R.id.message_TV, message);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_done_white_24px)
                .setContent(notificationView)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .build();

        try {
            Object miuiNotification = Class.forName("android.app.MiuiNotification").newInstance();
            Field customizedIconField = miuiNotification.getClass().getDeclaredField("customizedIcon");
            customizedIconField.setAccessible(true);
            customizedIconField.set(miuiNotification, true);

            Field extraNotificationField = notification.getClass().getField("extraNotification");
            extraNotificationField.setAccessible(true);
            extraNotificationField.set(notification, miuiNotification);

        } catch (Exception e) {

        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

        switch (buttonId) {
            case R.id.send_button: {
                mTitleET.setText("");
                messageET.setText("");
                mTitleET.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(mTitleET, InputMethodManager.SHOW_IMPLICIT);
                }
                break;
            }

            case R.id.send_close_button: {
                finishAffinity();
            }
        }
    }

}
