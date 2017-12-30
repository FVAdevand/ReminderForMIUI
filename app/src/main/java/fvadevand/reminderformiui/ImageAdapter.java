package fvadevand.reminderformiui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Vladimir on 25.12.2017.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private Integer[] mThumbIds = {
            R.drawable.add_color,
            R.drawable.agenda_color,
            R.drawable.alarm_clock_color,
            R.drawable.alarm_color,
            R.drawable.archive_color,
            R.drawable.attachment_color,
            R.drawable.battery_color,
            R.drawable.binoculars_color,
            R.drawable.bluetooth_color,
            R.drawable.briefcase_color,
            R.drawable.calendar_color,
            R.drawable.checked_color,
            R.drawable.cloud_color,
            R.drawable.cloud_download_color,
            R.drawable.cloud_find_color,
            R.drawable.cloud_upload_color,
            R.drawable.compact_disc_color,
            R.drawable.controls_color,
            R.drawable.database_color,
            R.drawable.diamond_color,
            R.drawable.edit_color,
            R.drawable.fax_color,
            R.drawable.file_color,
            R.drawable.flag_1_color,
            R.drawable.flag_2_color,
            R.drawable.flag_3_color,
            R.drawable.flag_4_color,
            R.drawable.folder_color,
            R.drawable.garbage_color,
            R.drawable.gift_color,
            R.drawable.home_color,
            R.drawable.hourglass_color,
            R.drawable.id_card_color,
            R.drawable.idea_color,
            R.drawable.internet_color,
            R.drawable.like_color,
            R.drawable.list_color,
            R.drawable.locked_color,
            R.drawable.map_color,
            R.drawable.microphone_color,
            R.drawable.notebook_color,
            R.drawable.notepad_color,
            R.drawable.paper_plane_color,
            R.drawable.photo_camera_color,
            R.drawable.placeholder_color,
            R.drawable.push_pin_color,
            R.drawable.search_color,
            R.drawable.server_color,
            R.drawable.settings_color,
            R.drawable.smartphone_color,
            R.drawable.speaker_color,
            R.drawable.star_color,
            R.drawable.success_color,
            R.drawable.television_color,
            R.drawable.umbrella_color,
            R.drawable.users_color,
            R.drawable.video_camera_color,
            R.drawable.worldwide_color
    };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItemView = convertView;
        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(mContext).inflate(R.layout.image_grid_item, null);
        }
        ImageView imageView = gridItemView.findViewById(R.id.grid_item_IV);
        imageView.setImageResource(mThumbIds[position]);

        return gridItemView;
    }
}
