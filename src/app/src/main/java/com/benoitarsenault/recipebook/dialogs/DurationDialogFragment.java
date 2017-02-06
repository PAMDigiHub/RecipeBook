package com.benoitarsenault.recipebook.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.benoitarsenault.recipebook.R;

/**
 * Created by sseag on 2016-10-19.
 */



public class DurationDialogFragment extends DialogFragment {
    private static final String ARG_DURATION = "duration";

    public static DurationDialogFragment newInstance(String duration) {

        Bundle args = new Bundle();
        args.putString(ARG_DURATION,duration);
        DurationDialogFragment fragment = new DurationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static DurationDialogFragment newInstance() {

        Bundle args = new Bundle();
        DurationDialogFragment fragment = new DurationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    DurationDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.duration_dialog, null);

        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.duration_dialog_timepicker);
        timePicker.setIs24HourView(true);

        String[] duration = getArguments().getString(ARG_DURATION,"0h15").split("h");

        int hour = Integer.parseInt(duration[0]);
        int minute = Integer.parseInt(duration[1]);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        builder.setView(view)
                .setMessage("Please select a duration")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDurationSelectedPositiveClick(timePicker.getCurrentHour()+"h"+timePicker.getCurrentMinute());
                    }
                })
                .setNegativeButton("No", null);

        return builder.create();
    }


    public interface DurationDialogListener {
        void onDurationSelectedPositiveClick(String duration);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DurationDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DurationDialogListener");
        }
    }
}
