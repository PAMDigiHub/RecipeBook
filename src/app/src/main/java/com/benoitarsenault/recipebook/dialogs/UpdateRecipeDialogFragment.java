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

public class UpdateRecipeDialogFragment extends DialogFragment {

    private static final String ARG_ID = "Id";
    private UpdateRecipeDialogListener listener;

    public static UpdateRecipeDialogFragment newInstance() {
        
        Bundle args = new Bundle();
        UpdateRecipeDialogFragment fragment = new UpdateRecipeDialogFragment();
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
                        listener.onUpdateRecipeDialogPositiveClick(getTag());
                    }
                }).setNeutralButton("Cancel",null)
                .setNegativeButton("No", null);

        return builder.create();
    }

    public interface UpdateRecipeDialogListener {
        void onUpdateRecipeDialogPositiveClick(String tag);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (UpdateRecipeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement UpdateItemConfirmationDialogListener");
        }
    }



}
