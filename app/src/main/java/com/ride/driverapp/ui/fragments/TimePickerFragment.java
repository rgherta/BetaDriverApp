package com.ride.driverapp.ui.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.ride.driverapp.viewmodel.RidesViewModel;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    MutableLiveData<String> proposedTime;

    public TimePickerFragment(MutableLiveData<String> time) {
        proposedTime = time;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        DecimalFormat formatter = new DecimalFormat("00");
        String chosenTime = (String) TextUtils.concat(formatter.format(hourOfDay), ":" , formatter.format(minute));
        proposedTime.setValue(chosenTime);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        Calendar calendar = Calendar.getInstance();

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DecimalFormat formatter = new DecimalFormat("00");
        String chosenTime = (String) TextUtils.concat(formatter.format(hourOfDay), ":" , formatter.format(minute));
        proposedTime.setValue(chosenTime);

    }
}