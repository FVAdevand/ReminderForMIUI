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
            R.drawable.ic_account_balance_black_24px,
            R.drawable.ic_account_circle_black_24px,
            R.drawable.ic_album_black_24px,
            R.drawable.ic_assignment_black_24px,
            R.drawable.ic_bookmark_black_24px,
            R.drawable.ic_bug_report_black_24px,
            R.drawable.ic_build_black_24px,
            R.drawable.ic_cake_black_24px,
            R.drawable.ic_flight_takeoff_black_24px,
            R.drawable.ic_lock_outline_black_24px,
            R.drawable.ic_mic_none_black_24px,
            R.drawable.ic_radio_black_24px,
            R.drawable.ic_shopping_cart_black_24px,
            R.drawable.ic_stars_black_24px,
            R.drawable.ic_work_black_24px
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
