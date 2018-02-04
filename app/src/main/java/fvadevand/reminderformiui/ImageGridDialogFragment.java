package fvadevand.reminderformiui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by Vladimir on 25.12.2017.
 */

public class ImageGridDialogFragment extends DialogFragment {

    ImageGridDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View rootView = layoutInflater.inflate(R.layout.dialog_image_grid, null);

        GridView imageGV = rootView.findViewById(R.id.image_GV);
        final Adapter adapter = new ImageAdapter(getActivity());
        imageGV.setAdapter((ListAdapter) adapter);
        imageGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onImageItemClick((int) adapter.getItem(position));
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .create();
    }

    //    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.dialog_image_grid, container, false);
//        GridView imageGV = rootView.findViewById(R.id.image_GV);
//        final Adapter adapter = new ImageAdapter(getActivity());
//        imageGV.setAdapter((ListAdapter) adapter);
//        imageGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mListener.onImageItemClick((int) adapter.getItem(position));
//                dismiss();
//            }
//        });
//
//        return rootView;
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mListener == null) {
            try {
                mListener = (ImageGridDialogListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    public void setOnImageGridDialogListener(ImageGridDialogListener listener) {
        mListener = listener;
    }

    public interface ImageGridDialogListener {
        void onImageItemClick(int resourceId);
    }
}
