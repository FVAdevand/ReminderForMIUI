package fvadevand.reminderformiui;

import android.content.Context;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Created by Vladimir on 31.01.2018.
 */

public class ImagePickerPreference extends DialogPreference {

    private int mImageResourceId;

    public ImagePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.dialog_image_grid);
        setDialogIcon(null);

    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        mImageResourceId = imageResourceId;
        persistInt(mImageResourceId);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        if (restorePersistedValue) {

        }
        super.onSetInitialValue(restorePersistedValue, defaultValue);
    }
}
