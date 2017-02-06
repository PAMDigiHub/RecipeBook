package com.benoitarsenault.recipebook.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by sseag on 2016-10-26.
 */

public class DeleteFragmentItemDialog extends android.support.v4.app.DialogFragment {
    private static final String ARG_POSITION = "position";
    DeleteFragmentItemDialogListener listener;

    public static DeleteFragmentItemDialog newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        DeleteFragmentItemDialog fragment = new DeleteFragmentItemDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        listener = (DeleteFragmentItemDialogListener) getTargetFragment();
        final int position = getArguments().getInt(ARG_POSITION);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Do you really want to delete this Step ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDeleteFragmentItemDialogPositiveClick(getTag(), position);
                    }
                }).setNegativeButton("No",null);

        return builder.create();
    }

    public interface DeleteFragmentItemDialogListener {
        void onDeleteFragmentItemDialogPositiveClick(String tag, int position);

    }
}
