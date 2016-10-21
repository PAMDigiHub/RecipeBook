package com.benoitarsenault.recipebook.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Benoit Arsenault on 2016-09-27.
 */

public class UpdateItemDialogFragment extends DialogFragment {

    private static final String ARG_ITEM_POSITION ="itemPosition" ;
    private UpdateItemConfirmationDialogListener listener;

    public static UpdateItemDialogFragment newInstance(int position) {
        
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_POSITION,position);
        UpdateItemDialogFragment fragment = new UpdateItemDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Do you really want to update ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onUpdateItemDialogPositiveClick(getArguments().getInt(ARG_ITEM_POSITION));
                    }
                })
                .setNegativeButton("No", null);

        return builder.create();
    }

    public interface UpdateItemConfirmationDialogListener {
        void onUpdateItemDialogPositiveClick(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (UpdateItemConfirmationDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement UpdateItemConfirmationDialogListener");
        }
    }

}
