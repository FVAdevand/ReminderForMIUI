package fvadevand.reminderformiui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import fvadevand.reminderformiui.utilities.ReminderUtils;

/**
 * Created by Vladimir on 25.12.2017.
 *
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private Integer[] mThumbIds = ReminderUtils.getThumbIds();

    ImageAdapter(Context c) {
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
            gridItemView = LayoutInflater.from(mContext).inflate(R.layout.image_grid_item, parent, false);
        }
        ImageView imageView = gridItemView.findViewById(R.id.grid_item_IV);
        imageView.setImageResource(mThumbIds[position]);

        return gridItemView;
    }
}
