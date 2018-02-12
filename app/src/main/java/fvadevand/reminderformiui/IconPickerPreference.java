package fvadevand.reminderformiui;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import fvadevand.reminderformiui.utilities.ReminderUtils;

/**
 * Created by Vladimir on 31.01.2018.
 *
 */

public class IconPickerPreference extends Preference implements IconPickerDialog.onIconSetListener {

    private static final String LOG_TAG = IconPickerPreference.class.getSimpleName();
    private static final int DEFAULT_IMAGE_ID = ReminderUtils.getDefaultImageId();
    private int mImageId;
    private ImageView mIconImageView;

    public IconPickerPreference(Context context) {
        this(context, null);
    }

    public IconPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImageId = DEFAULT_IMAGE_ID;
        setWidgetLayoutResource(R.layout.image_pref);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mIconImageView = view.findViewById(R.id.iv_icon_notification);
        mIconImageView.setImageResource(mImageId);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mImageId = this.getPersistedInt(DEFAULT_IMAGE_ID);
        } else {
            persistInt(DEFAULT_IMAGE_ID);
        }
    }

    @Override
    protected void onClick() {
        super.onClick();
        Activity activity = (Activity) getContext();
        IconPickerDialog dialog = new IconPickerDialog();
        dialog.setOnIconSetListener(this);
        dialog.show(activity.getFragmentManager(), "ImageGridDialog");
    }

    @Override
    public void onIconSet(int resourceId) {
        persistInt(resourceId);
        mImageId = resourceId;
        mIconImageView.setImageResource(mImageId);
    }
}
