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
        imageIdArray.put(R.drawable.add_color, R.drawable.add);
        imageIdArray.put(R.drawable.agenda_color, R.drawable.agenda);
        imageIdArray.put(R.drawable.alarm_color, R.drawable.alarm);
//        imageIdArray.put(R.drawable.alarm_clock_color, R.drawable.alarm_clock);
        imageIdArray.put(R.drawable.archive_color, R.drawable.archive);
        imageIdArray.put(R.drawable.attachment_color, R.drawable.attachment);
        imageIdArray.put(R.drawable.battery_color, R.drawable.battery);
        imageIdArray.put(R.drawable.binoculars_color, R.drawable.binoculars);
        imageIdArray.put(R.drawable.bluetooth_color, R.drawable.bluetooth);
        imageIdArray.put(R.drawable.briefcase_color, R.drawable.briefcase);
        imageIdArray.put(R.drawable.calendar_color, R.drawable.calendar);
        imageIdArray.put(R.drawable.checked_color, R.drawable.checked);
        imageIdArray.put(R.drawable.cloud_color, R.drawable.cloud);
        imageIdArray.put(R.drawable.cloud_download_color, R.drawable.cloud_download);
        imageIdArray.put(R.drawable.cloud_find_color, R.drawable.cloud_find);
        imageIdArray.put(R.drawable.cloud_upload_color, R.drawable.cloud_upload);
        imageIdArray.put(R.drawable.compact_disc_color, R.drawable.compact_disc);
        imageIdArray.put(R.drawable.controls_color, R.drawable.controls);
        imageIdArray.put(R.drawable.database_color, R.drawable.database);
        imageIdArray.put(R.drawable.diamond_color, R.drawable.diamond);
        imageIdArray.put(R.drawable.edit_color, R.drawable.edit);
        imageIdArray.put(R.drawable.fax_color, R.drawable.fax);
        imageIdArray.put(R.drawable.file_color, R.drawable.file);
        imageIdArray.put(R.drawable.flag_1_color, R.drawable.flag_1);
        imageIdArray.put(R.drawable.flag_2_color, R.drawable.flag_2);
        imageIdArray.put(R.drawable.flag_3_color, R.drawable.flag_3);
        imageIdArray.put(R.drawable.flag_4_color, R.drawable.flag_4);
        imageIdArray.put(R.drawable.folder_color, R.drawable.folder);
        imageIdArray.put(R.drawable.garbage_color, R.drawable.garbage);
        imageIdArray.put(R.drawable.gift_color, R.drawable.gift);
        imageIdArray.put(R.drawable.home_color, R.drawable.home);
        imageIdArray.put(R.drawable.hourglass_color, R.drawable.hourglass);
        imageIdArray.put(R.drawable.id_card_color, R.drawable.id_card);
        imageIdArray.put(R.drawable.idea_color, R.drawable.idea);
        imageIdArray.put(R.drawable.internet_color, R.drawable.internet);
        imageIdArray.put(R.drawable.like_color, R.drawable.like);
        imageIdArray.put(R.drawable.list_color, R.drawable.list);
        imageIdArray.put(R.drawable.locked_color, R.drawable.locked);
        imageIdArray.put(R.drawable.map_color, R.drawable.map);
        imageIdArray.put(R.drawable.microphone_color, R.drawable.microphone);
        imageIdArray.put(R.drawable.notebook_color, R.drawable.notebook);
        imageIdArray.put(R.drawable.notepad_color, R.drawable.notepad);
        imageIdArray.put(R.drawable.paper_plane_color, R.drawable.paper_plane);
        imageIdArray.put(R.drawable.photo_camera_color, R.drawable.photo_camera);
        imageIdArray.put(R.drawable.placeholder_color, R.drawable.placeholder);
        imageIdArray.put(R.drawable.push_pin_color, R.drawable.push_pin);
        imageIdArray.put(R.drawable.search_color, R.drawable.search);
        imageIdArray.put(R.drawable.server_color, R.drawable.server);
        imageIdArray.put(R.drawable.settings_color, R.drawable.settings);
        imageIdArray.put(R.drawable.smartphone_color, R.drawable.smartphone);
        imageIdArray.put(R.drawable.speaker_color, R.drawable.speaker);
        imageIdArray.put(R.drawable.star_color, R.drawable.star);
        imageIdArray.put(R.drawable.success_color, R.drawable.success);
        imageIdArray.put(R.drawable.television_color, R.drawable.television);
        imageIdArray.put(R.drawable.umbrella_color, R.drawable.umbrella);
        imageIdArray.put(R.drawable.users_color, R.drawable.users);
        imageIdArray.put(R.drawable.video_camera_color, R.drawable.video_camera);
        imageIdArray.put(R.drawable.worldwide_color, R.drawable.worldwide);
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
