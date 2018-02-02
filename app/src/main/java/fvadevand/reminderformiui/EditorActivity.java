package fvadevand.reminderformiui;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;
import fvadevand.reminderformiui.utilities.ReminderUtils;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener,
        ImageGridDialogFragment.ImageGridDialogListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_NOTIFICATION_LOADER_ID = 36;
    private EditText mTitleET;
    private EditText mMessageET;
    private ImageButton mChooseImageButton;
    private int mImageId;
    private InputMethodManager mInputMethodManager;
    private Toast mToast;
    private Uri mCurrentNotificationUri;
    private boolean isEditMode;

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

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        Button sendCloseButton = findViewById(R.id.send_close_button);
        sendCloseButton.setOnClickListener(this);

        // TODO: add a Shared Preferences.

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
                if (notifyNotification()) {
                    mTitleET.setText("");
                    mMessageET.setText("");
                }
                mTitleET.requestFocus();
                mInputMethodManager.showSoftInput(mTitleET, InputMethodManager.SHOW_IMPLICIT);
                break;
            }

            case R.id.send_close_button: {
                if (notifyNotification()) {
                    finishAffinity();
                    break;
                }
                mTitleET.requestFocus();
                mInputMethodManager.showSoftInput(mTitleET, InputMethodManager.SHOW_IMPLICIT);
                break;
            }
        }
    }

    private boolean notifyNotification() {

        String title = mTitleET.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            if (mToast != null) mToast.cancel();
            mToast = Toast.makeText(this, "Please, enter title", Toast.LENGTH_SHORT);
            mToast.show();
            return false;
        }

        String message = mMessageET.getText().toString().trim();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationEntry.COLUMN_IMAGE_ID, mImageId);
        contentValues.put(NotificationEntry.COLUMN_TITLE, title);
        contentValues.put(NotificationEntry.COLUMN_MESSAGE, message);

        Uri contentUri;
        if (isEditMode) {
            contentUri = mCurrentNotificationUri;
            int rowsUpdated = getContentResolver().update(contentUri, contentValues, null, null);
            if (rowsUpdated == 0) {
                return false;
            }
            isEditMode = false;
            setTitle(R.string.editor_activity_title_new_mode);
            getLoaderManager().getLoader(EXISTING_NOTIFICATION_LOADER_ID).reset();
        } else {
            contentUri = getContentResolver().insert(NotificationEntry.CONTENT_URI, contentValues);
            if (contentUri == null) {
                return false;
            }
        }
        int notificationId = (int) ContentUris.parseId(contentUri);
        ReminderUtils.sendNotification(this, notificationId, mImageId, title, message);
        return true;
    }

    @Override
    public void onImageItemClick(int resourceId) {
        mImageId = resourceId;
        mChooseImageButton.setImageResource(resourceId);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
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
        mTitleET.setText("");
        mMessageET.setText("");
        mImageId = R.drawable.add_color;
        mChooseImageButton.setImageResource(mImageId);
    }
}
