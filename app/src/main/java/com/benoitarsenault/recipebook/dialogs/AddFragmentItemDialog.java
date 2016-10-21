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
public class AddFragmentItemDialog extends android.support.v4.app.DialogFragment {

    private static final String ARG_TEXT = "text";
    private EditText editText;
    private boolean allowEmptyText = false;
    private AddFragmentItemDialogListener listener;


    public static AddFragmentItemDialog newInstance(String text) {

        Bundle args = new Bundle();
        args.putString(ARG_TEXT,text);
        AddFragmentItemDialog fragment = new AddFragmentItemDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddFragmentItemDialog newInstance() {

        Bundle args = new Bundle();

        AddFragmentItemDialog fragment = new AddFragmentItemDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.text_input_dialog, null);

        editText = (EditText) view.findViewById(R.id.text_input_dialog_edit_text);
        editText.setText(getArguments().getString(ARG_TEXT,""));
        editText.setSelection(editText.getText().length());
        editText.requestFocus();

        listener = (AddFragmentItemDialogListener) getTargetFragment();

        builder.setTitle("Add item")
                .setView(view)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!allowEmptyText && editText.getText().length() == 0) {
                            Toast.makeText(getContext(), "Empty text is not allowed", Toast.LENGTH_SHORT).show();

                        } else {
                            listener.addFragmentItemDialogPositiveClick(getTag(), editText.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }

    public interface AddFragmentItemDialogListener {
        void addFragmentItemDialogPositiveClick(String tag, String newText);
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
