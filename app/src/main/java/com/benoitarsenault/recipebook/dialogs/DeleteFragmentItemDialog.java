package com.benoitarsenault.recipebook.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.benoitarsenault.recipebook.R;


/**
 * Created by Benoit Arsenault on 2016-09-19.
 */
public class DeleteFragmentItemDialog extends android.support.v4.app.DialogFragment {

    private static final String ARG_TEXT = "text";
    private static final String ARG_POSITION = "position";
    private EditText editText;
    private boolean allowEmptyText = false;
    private DeleteFragmentItemDialogListener listener;

    public static DeleteFragmentItemDialog newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION,position);
        DeleteFragmentItemDialog fragment = new DeleteFragmentItemDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {

        final int position = getArguments().getInt(ARG_POSITION);
        listener = (DeleteFragmentItemDialogListener) getTargetFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Do you really want to delete this item ?")
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            listener.deleteFragmentItemDialogPositiveClick(getTag(), position);

                    }
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }

    public interface DeleteFragmentItemDialogListener {
        void deleteFragmentItemDialogPositiveClick(String tag, int position);
    }
/*
   @Override
   public void onAttach(Activity activity) {
       super.onAttach(activity);
       try {
           listener = (EditItemDialogListener) activity;
       } catch (ClassCastException e) {
           // The activity doesn't implement the interface, throw exception
           throw new ClassCastException(activity.toString()
                   + " must implement EditItemDialogListener");
       }
   }*/


    public boolean isAllowEmptyText() {
        return allowEmptyText;
    }

    public void setAllowEmptyText(boolean allowEmptyText) {
        this.allowEmptyText = allowEmptyText;
    }
}
