package fvadevand.reminderformiui;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RemoteViews;

import java.lang.reflect.Field;

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

        mImageIdArray = new SparseIntArray();
        mImageIdArray.put(R.drawable.add_color, R.drawable.add);
        mImageIdArray.put(R.drawable.agenda_color, R.drawable.agenda);
        mImageIdArray.put(R.drawable.alarm_color, R.drawable.alarm);
        mImageIdArray.put(R.drawable.alarm_clock_color, R.drawable.alarm_clock);
        mImageIdArray.put(R.drawable.archive_color, R.drawable.archive);
        mImageIdArray.put(R.drawable.attachment_color, R.drawable.attachment);
        mImageIdArray.put(R.drawable.battery_color, R.drawable.battery);
        mImageIdArray.put(R.drawable.binoculars_color, R.drawable.binoculars);
        mImageIdArray.put(R.drawable.bluetooth_color, R.drawable.bluetooth);
        mImageIdArray.put(R.drawable.briefcase_color, R.drawable.briefcase);
        mImageIdArray.put(R.drawable.calendar_color, R.drawable.calendar);
        mImageIdArray.put(R.drawable.checked_color, R.drawable.checked);
        mImageIdArray.put(R.drawable.cloud_color, R.drawable.cloud);
        mImageIdArray.put(R.drawable.cloud_download_color, R.drawable.cloud_download);
        mImageIdArray.put(R.drawable.cloud_find_color, R.drawable.cloud_find);
        mImageIdArray.put(R.drawable.cloud_upload_color, R.drawable.cloud_upload);
        mImageIdArray.put(R.drawable.compact_disc_color, R.drawable.compact_disc);
        mImageIdArray.put(R.drawable.controls_color, R.drawable.controls);
        mImageIdArray.put(R.drawable.database_color, R.drawable.database);
        mImageIdArray.put(R.drawable.diamond_color, R.drawable.diamond);
        mImageIdArray.put(R.drawable.edit_color, R.drawable.edit);
        mImageIdArray.put(R.drawable.fax_color, R.drawable.fax);
        mImageIdArray.put(R.drawable.file_color, R.drawable.file);
        mImageIdArray.put(R.drawable.flag_1_color, R.drawable.flag_1);
        mImageIdArray.put(R.drawable.flag_2_color, R.drawable.flag_2);
        mImageIdArray.put(R.drawable.flag_3_color, R.drawable.flag_3);
        mImageIdArray.put(R.drawable.flag_4_color, R.drawable.flag_4);
        mImageIdArray.put(R.drawable.folder_color, R.drawable.folder);
        mImageIdArray.put(R.drawable.garbage_color, R.drawable.garbage);
        mImageIdArray.put(R.drawable.gift_color, R.drawable.gift);
        mImageIdArray.put(R.drawable.home_color, R.drawable.home);
        mImageIdArray.put(R.drawable.hourglass_color, R.drawable.hourglass);
        mImageIdArray.put(R.drawable.id_card_color, R.drawable.id_card);
        mImageIdArray.put(R.drawable.idea_color, R.drawable.idea);
        mImageIdArray.put(R.drawable.internet_color, R.drawable.internet);
        mImageIdArray.put(R.drawable.like_color, R.drawable.like);
        mImageIdArray.put(R.drawable.list_color, R.drawable.list);
        mImageIdArray.put(R.drawable.locked_color, R.drawable.locked);
        mImageIdArray.put(R.drawable.map_color, R.drawable.map);
        mImageIdArray.put(R.drawable.microphone_color, R.drawable.microphone);
        mImageIdArray.put(R.drawable.notebook_color, R.drawable.notebook);
        mImageIdArray.put(R.drawable.notepad_color, R.drawable.notepad);
        mImageIdArray.put(R.drawable.paper_plane_color, R.drawable.paper_plane);
        mImageIdArray.put(R.drawable.photo_camera_color, R.drawable.photo_camera);
        mImageIdArray.put(R.drawable.placeholder_color, R.drawable.placeholder);
        mImageIdArray.put(R.drawable.push_pin_color, R.drawable.push_pin);
        mImageIdArray.put(R.drawable.search_color, R.drawable.search);
        mImageIdArray.put(R.drawable.server_color, R.drawable.server);
        mImageIdArray.put(R.drawable.settings_color, R.drawable.settings);
        mImageIdArray.put(R.drawable.smartphone_color, R.drawable.smartphone);
        mImageIdArray.put(R.drawable.speaker_color, R.drawable.speaker);
        mImageIdArray.put(R.drawable.star_color, R.drawable.star);
        mImageIdArray.put(R.drawable.success_color, R.drawable.success);
        mImageIdArray.put(R.drawable.television_color, R.drawable.television);
        mImageIdArray.put(R.drawable.umbrella_color, R.drawable.umbrella);
        mImageIdArray.put(R.drawable.users_color, R.drawable.users);
        mImageIdArray.put(R.drawable.video_camera_color, R.drawable.video_camera);
        mImageIdArray.put(R.drawable.worldwide_color, R.drawable.worldwide);


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
        notificationView.setTextViewText(R.id.message_TV, message);

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

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    @Override
    public void onImageItemClick(int resourceId) {
        mImageId = resourceId;
        mChooseImageButton.setImageResource(resourceId);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

}
