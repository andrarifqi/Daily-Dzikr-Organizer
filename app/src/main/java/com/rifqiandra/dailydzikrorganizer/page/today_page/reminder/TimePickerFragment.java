package com.rifqiandra.dailydzikrorganizer.page.today_page.reminder;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.widget.TimePicker;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment  {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // TODO : implement the functionality
            }
        }, hour, minute, DateFormat.is24HourFormat(getActivity()));

    }
}
