package fvadevand.reminderformiui;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Calendar;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;
import fvadevand.reminderformiui.service.NotificationIntentService;
import fvadevand.reminderformiui.service.NotificationTask;
import fvadevand.reminderformiui.utilities.ReminderUtils;

public class EditorActivity extends AppCompatActivity implements
        View.OnClickListener,
        IconPickerDialog.onIconSetListener,
        AlarmSetDialog.OnAlarmSetListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_NOTIFICATION_LOADER_ID = 36;
    private static final String LOG_TAG = EditorActivity.class.getSimpleName();
    boolean isChangeIcon;
    private EditText mTitleET;
    private EditText mMessageET;
    private ImageButton mChooseImageButton;
    private int mImageId;
    private int mImageIdDefault;
    private InputMethodManager mInputMethodManager;
    private Uri mCurrentNotificationUri;
    private boolean isEditMode;
    private TextInputLayout mTitleInputLayout;
    private CheckBox mDelayNotificationCheckBox;
    private Calendar mCalendar;

    // TODO: add AlarmManager to send notification. Send notification after some time.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mCurrentNotificationUri = getIntent().getData();
        isEditMode = (mCurrentNotificationUri != null);
        if (isEditMode) {
            setTitle(R.string.editor_activity_title_edit_mode);
            getLoaderManager().initLoader(EXISTING_NOTIFICATION_LOADER_ID, null, this);
        } else {
            setTitle(R.string.editor_activity_title_new_mode);
        }

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Button sendButton = findViewById(R.id.btn_send);
        sendButton.setOnClickListener(this);

        Button sendCloseButton = findViewById(R.id.btn_send_close);
        sendCloseButton.setOnClickListener(this);

        mTitleInputLayout = findViewById(R.id.et_layout_title);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mImageIdDefault = sharedPreferences.getInt(getString(R.string.pref_image_key), ReminderUtils.getDefaultImageId());
        isChangeIcon = sharedPreferences.getBoolean(getString(R.string.pref_change_icon_key),
                getResources().getBoolean(R.bool.change_icon_default));

        mImageId = mImageIdDefault;
        mChooseImageButton = findViewById(R.id.ibtn_notification_icon);
        mChooseImageButton.setImageResource(mImageId);
        mChooseImageButton.setOnClickListener(this);

        ImageButton alarmSetButton = findViewById(R.id.ibtn_alarm);
        alarmSetButton.setOnClickListener(this);

        mTitleET = findViewById(R.id.et_title);
        mMessageET = findViewById(R.id.et_message);
        mTitleET.requestFocus();

        mDelayNotificationCheckBox = findViewById(R.id.cb_delay_notification);
        mDelayNotificationCheckBox.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ibtn_notification_icon:
                DialogFragment dialog = new IconPickerDialog();
                dialog.show(getFragmentManager(), "ImageGridDialog");
                break;

            case R.id.ibtn_alarm:
                DialogFragment dialogAlarm = new AlarmSetDialog();
                dialogAlarm.show(getFragmentManager(), "AlarmSetDialog");
                break;

            case R.id.btn_send:
                if (notifyNotification()) {
                    mTitleInputLayout.setError(null);
                    clearFields();
                }
                mTitleET.requestFocus();
                mInputMethodManager.showSoftInput(mTitleET, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.btn_send_close:
                if (notifyNotification()) {
                    mTitleInputLayout.setError(null);
                    finishAffinity();
                    break;
                }
                mTitleET.requestFocus();
                mInputMethodManager.showSoftInput(mTitleET, InputMethodManager.SHOW_IMPLICIT);
                break;
        }
    }

    private boolean notifyNotification() {

        String title = mTitleET.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            mTitleInputLayout.setError(getString(R.string.error_enter_title));
            return false;
        }

        if (!mDelayNotificationCheckBox.isChecked()) mCalendar = Calendar.getInstance();
        Log.i(LOG_TAG, ReminderUtils.formatTime(this, mCalendar));

        String message = mMessageET.getText().toString().trim();

        Bundle notificationBundle = new Bundle();
        notificationBundle.putString(NotificationEntry.COLUMN_TITLE, title);
        notificationBundle.putString(NotificationEntry.COLUMN_MESSAGE, message);
        notificationBundle.putInt(NotificationEntry.COLUMN_IMAGE_ID, mImageId);

        Intent serviceIntent = new Intent(this, NotificationIntentService.class);

        if (isEditMode) {
            serviceIntent.setAction(NotificationTask.ACTION_UPDATE_NOTIFICATION);
            notificationBundle.putInt(NotificationEntry._ID, (int) ContentUris.parseId(mCurrentNotificationUri));
            isEditMode = false;
            setTitle(R.string.editor_activity_title_new_mode);
            getLoaderManager().getLoader(EXISTING_NOTIFICATION_LOADER_ID).reset();
        } else {
            serviceIntent.setAction(NotificationTask.ACTION_SEND_NOTIFICATION);
        }

        serviceIntent.putExtra(NotificationTask.NOTIFICATION_BUNDLE, notificationBundle);
        startService(serviceIntent);
        return true;
    }

    private void clearFields() {
        mTitleET.setText("");
        mMessageET.setText("");
        if (isChangeIcon) {
            mImageId = mImageIdDefault;
            mChooseImageButton.setImageResource(mImageId);
        }
        mDelayNotificationCheckBox.setChecked(false);
        mDelayNotificationCheckBox.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onIconSet(int resourceId) {
        mImageId = resourceId;
        mChooseImageButton.setImageResource(resourceId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                mCurrentNotificationUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            mTitleET.setText(cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TITLE)));
            mMessageET.setText(cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_MESSAGE)));
            mImageId = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_IMAGE_ID));
            mChooseImageButton.setImageResource(mImageId);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onAlarmSet(Calendar calendar) {
        mDelayNotificationCheckBox.setVisibility(View.VISIBLE);
        mDelayNotificationCheckBox.setChecked(true);
        String delayTimeString = getString(R.string.delay_notification,
                ReminderUtils.formatTime(this, calendar),
                ReminderUtils.formatShortDate(this, calendar));
        mDelayNotificationCheckBox.setText(delayTimeString);
        mCalendar = calendar;
    }
}
