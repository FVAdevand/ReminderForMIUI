package fvadevand.reminderformiui;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Date;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;
import fvadevand.reminderformiui.data.NotificationDbHelper;
import fvadevand.reminderformiui.utilities.ReminderUtils;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener, ImageGridDialogFragment.ImageGridDialogListener {

    private EditText mTitleET;
    private EditText mMessageET;
    private ImageButton mChooseImageButton;
    private int mImageId;
    private InputMethodManager mInputMethodManager;

    // TODO 2: add AlarmManager to send notification. Send notification after some time.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

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
                saveNotification();
                mTitleET.setText("");
                mMessageET.setText("");
                mTitleET.requestFocus();
                mInputMethodManager.showSoftInput(mTitleET, InputMethodManager.SHOW_IMPLICIT);
                break;
            }

            case R.id.send_close_button: {
                saveNotification();
                finishAffinity();
            }
        }
    }

    private void saveNotification() {

        // TODO 1: change notificationId on _ID from SQL database

        String title = mTitleET.getText().toString().trim();
        String message = mMessageET.getText().toString().trim();
        int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationDbHelper dbHelper = new NotificationDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationEntry.COLUMN_NOTIFICATIONS_ID, notificationId);
        contentValues.put(NotificationEntry.COLUMN_IMAGE_ID, mImageId);
        contentValues.put(NotificationEntry.COLUMN_TITLE, title);
        contentValues.put(NotificationEntry.COLUMN_MESSAGE, message);
        long savedRowId = db.insert(
                NotificationEntry.TABLE_NAME,
                null,
                contentValues);
        if (savedRowId > 0) {
            ReminderUtils.sendNotification(this, notificationId, mImageId, title, message);
        }
    }

    @Override
    public void onImageItemClick(int resourceId) {
        mImageId = resourceId;
        mChooseImageButton.setImageResource(resourceId);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

}
