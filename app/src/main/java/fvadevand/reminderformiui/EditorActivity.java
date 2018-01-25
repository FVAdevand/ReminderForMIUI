package fvadevand.reminderformiui;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.util.Date;

import fvadevand.reminderformiui.utilities.ReminderUtils;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener, ImageGridDialogFragment.ImageGridDialogListener {

    private EditText mTitleET;
    private EditText mMessageET;
    private ImageButton mChooseImageButton;
    private int mImageId;
    private InputMethodManager mInputMethodManager;
    private SparseIntArray mImageIdArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mImageIdArray = ReminderUtils.getImageIdArray();

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        Button sendCloseButton = findViewById(R.id.send_close_button);
        sendCloseButton.setOnClickListener(this);

        mImageId = R.drawable.add_color;
        mChooseImageButton = findViewById(R.id.notification_icon_IB);
        mChooseImageButton.setImageResource(mImageId);
        mChooseImageButton.setOnClickListener(this);

        mTitleET = findViewById(R.id.title_ET);
        mMessageET = findViewById(R.id.message_ET);
        mTitleET.requestFocus();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.notification_icon_IB: {
                DialogFragment dialog = new ImageGridDialogFragment();
                dialog.show(getFragmentManager(), "ImageGridDialog");
                break;
            }

            case R.id.send_button: {
                sendNotification();
                mTitleET.setText("");
                mMessageET.setText("");
                mTitleET.requestFocus();
                mInputMethodManager.showSoftInput(mTitleET, InputMethodManager.SHOW_IMPLICIT);
                break;
            }

            case R.id.send_close_button: {
                sendNotification();
                finishAffinity();
            }
        }
    }

    private void sendNotification() {

        String title = mTitleET.getText().toString().trim();
        String message = mMessageET.getText().toString().trim();

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.custom_push);
        notificationView.setImageViewResource(R.id.large_image_IV, mImageId);
        notificationView.setTextViewText(R.id.title_TV, title);
        if (TextUtils.isEmpty(message)) {
            notificationView.setViewVisibility(R.id.message_TV, View.GONE);
        } else {
            notificationView.setTextViewText(R.id.message_TV, message);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(mImageIdArray.get(mImageId))
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

        int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

    @Override
    public void onImageItemClick(int resourceId) {
        mImageId = resourceId;
        mChooseImageButton.setImageResource(resourceId);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

}
