package com.swipeme.www.swipeme;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public abstract class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new CustomTimePickerDialog(getActivity(), this,
                hour, minute, DateFormat.is24HourFormat(getActivity()));

    }

    public abstract void onTimeSet(TimePicker view, int hourOfDay, int minute);
}
