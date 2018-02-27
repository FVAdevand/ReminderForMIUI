package fvadevand.reminderformiui.utilities;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.SparseIntArray;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import fvadevand.reminderformiui.R;

/**
 * Created by Vladimir on 24.01.2018.
 *
 */

public class ReminderUtils {

    private static final String LOG_TAG = ReminderUtils.class.getSimpleName();


    private ReminderUtils() {
    }

    private static SparseIntArray getImageIdArray() {
        SparseIntArray imageIdArray = new SparseIntArray();
        imageIdArray.put(R.drawable.ic_add_color, R.drawable.ic_add);
        imageIdArray.put(R.drawable.ic_agenda_color, R.drawable.ic_agenda);
        imageIdArray.put(R.drawable.ic_attachment_color, R.drawable.ic_attachment);
        imageIdArray.put(R.drawable.ic_battery_color, R.drawable.ic_battery);
        imageIdArray.put(R.drawable.ic_briefcase_color, R.drawable.ic_briefcase);
        imageIdArray.put(R.drawable.ic_calendar_color, R.drawable.ic_calendar);
        imageIdArray.put(R.drawable.ic_database_color, R.drawable.ic_database);
        imageIdArray.put(R.drawable.ic_edit_color, R.drawable.ic_edit);
        imageIdArray.put(R.drawable.ic_file_color, R.drawable.ic_file);
        imageIdArray.put(R.drawable.ic_folder_color, R.drawable.ic_folder);
        imageIdArray.put(R.drawable.ic_garbage_color, R.drawable.ic_garbage);
        imageIdArray.put(R.drawable.ic_gift_color, R.drawable.ic_gift);
        imageIdArray.put(R.drawable.ic_home_color, R.drawable.ic_home);
        imageIdArray.put(R.drawable.ic_id_card_color, R.drawable.ic_id_card);
        imageIdArray.put(R.drawable.ic_idea_color, R.drawable.ic_idea);
        imageIdArray.put(R.drawable.ic_like_color, R.drawable.ic_like);
        imageIdArray.put(R.drawable.ic_locked_color, R.drawable.ic_locked);
        imageIdArray.put(R.drawable.ic_mail_color, R.drawable.ic_mail);
        imageIdArray.put(R.drawable.ic_microphone_color, R.drawable.ic_microphone);
        imageIdArray.put(R.drawable.ic_notebook_color, R.drawable.ic_notebook);
        imageIdArray.put(R.drawable.ic_photo_camera_color, R.drawable.ic_photo_camera);
        imageIdArray.put(R.drawable.ic_placeholder_color, R.drawable.ic_placeholder);
        imageIdArray.put(R.drawable.ic_print_color, R.drawable.ic_print);
        imageIdArray.put(R.drawable.ic_search_color, R.drawable.ic_search);
        imageIdArray.put(R.drawable.ic_settings_color, R.drawable.ic_settings);
        imageIdArray.put(R.drawable.ic_smartphone_color, R.drawable.ic_smartphone);
        imageIdArray.put(R.drawable.ic_speaker_color, R.drawable.ic_speaker);
        imageIdArray.put(R.drawable.ic_star_color, R.drawable.ic_star);
        imageIdArray.put(R.drawable.ic_umbrella_color, R.drawable.ic_umbrella);
        imageIdArray.put(R.drawable.ic_worldwide_color, R.drawable.ic_worldwide);


        return imageIdArray;
    }

    public static Integer[] getThumbIds() {
        SparseIntArray imageIdArray = getImageIdArray();
        int sizeArray = imageIdArray.size();
        Integer[] thumbIds = new Integer[sizeArray];
        for (int i = 0; i < sizeArray; i++) {
            thumbIds[i] = imageIdArray.keyAt(i);
        }
        return thumbIds;
    }

    public static int getDefaultImageId() {
        return getImageIdArray().keyAt(0);
    }

    public static int getMonocolorImageId(int multicolorImageId) {
        SparseIntArray imageIdArray = getImageIdArray();
        return imageIdArray.get(multicolorImageId);
    }

    public static String formatTime(Context context, Calendar calendar) {

        String formatTimeString;
        if (DateFormat.is24HourFormat(context)) {
            formatTimeString = context.getString(R.string.format_time_24h);
        } else {
            formatTimeString = context.getString(R.string.format_time_12h);
        }
        Format timeFormat = new SimpleDateFormat(formatTimeString, Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }

    public static String formatFullDate(Context context, Calendar calendar) {
        Format dateFormat = new SimpleDateFormat(context.getString(R.string.format_full_date), Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static String formatShortDate(Context context, Calendar calendar) {
        Format dateFormat = new SimpleDateFormat(context.getString(R.string.format_short_date), Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static long getUtcTimeInMillis(Calendar calendar) {
        long offset = calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
        return calendar.getTimeInMillis() - offset;
    }

    public static long getLocalTimeInMillis(long utcTimeInMillis) {
        long offset = TimeZone.getDefault().getOffset(utcTimeInMillis);
        return utcTimeInMillis + offset;
    }
}
