package fvadevand.reminderformiui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by Vladimir on 06.02.2018.
 */

public class AlarmSetDialog extends DialogFragment {

    OnAlarmSetListener mListener;
    private TextView mTimeTextView;
    private TextView mDateTextView;
    private Calendar mCalendar;
    TimePickerDialog.OnTimeSetListener timeCallBack = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);
            mTimeTextView.setText(formatTime(mCalendar));
        }
    };

    DatePickerDialog.OnDateSetListener dateCallBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.YEAR, year);
            mDateTextView.setText(formatDate(mCalendar));
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        @SuppressLint("InflateParams") View rootView = layoutInflater.inflate(R.layout.dialog_alarm, null);
        mCalendar = Calendar.getInstance();
        mTimeTextView = rootView.findViewById(R.id.tv_time);
        mTimeTextView.setText(formatTime(mCalendar));
        mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);
                TimePickerDialog timeDialog = new TimePickerDialog(v.getContext(),
                        timeCallBack,
                        hour,
                        minute,
                        DateFormat.is24HourFormat(v.getContext()));
                timeDialog.show();
            }
        });

        mDateTextView = rootView.findViewById(R.id.tv_date);
        mDateTextView.setText(formatDate(mCalendar));
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = mCalendar.get(Calendar.YEAR);
                int month = mCalendar.get(Calendar.MONTH);
                int day = mCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog = new DatePickerDialog(v.getContext(),
                        dateCallBack,
                        year,
                        month,
                        day);
                dateDialog.show();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setTitle(R.string.alarm_dialog_title)
                .setPositiveButton(R.string.btn_set, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onAlarmSet(mCalendar);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mListener == null) {
            try {
                mListener = (OnAlarmSetListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnAlarmSetListener");
            }
        }
    }

    private String formatTime(Calendar calendar) {
        Format timeFormat = new SimpleDateFormat(getString(R.string.format_time), Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }

    private String formatDate(Calendar calendar) {
        Format dateFormat = new SimpleDateFormat(getString(R.string.formate_date), Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public void setOnAlarmSetListener(OnAlarmSetListener listener) {
        mListener = listener;
    }

    public interface OnAlarmSetListener {
        void onAlarmSet(Calendar calendar);
    }
}