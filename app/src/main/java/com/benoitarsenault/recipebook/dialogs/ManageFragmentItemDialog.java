package com.benoitarsenault.recipebook.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.benoitarsenault.recipebook.R;


/**
 * Created by Benoit Arsenault on 2016-09-19.
 */
public class ManageFragmentItemDialog extends android.support.v4.app.DialogFragment implements DeleteFragmentItemDialog.DeleteFragmentItemDialogListener {

    private static final String ARG_TEXT = "text";
    private static final String ARG_POSITION = "position";
    private EditText editText;
    private boolean allowEmptyText = false;
    private ManageFragmentItemDialogListener listener;

    public static ManageFragmentItemDialog newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION,position);
        ManageFragmentItemDialog fragment = new ManageFragmentItemDialog();
        fragment.setArguments(args);
        return fragment;
    }
    public static ManageFragmentItemDialog newInstance(int position, String text) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION,position);
        args.putString(ARG_TEXT,text);
        ManageFragmentItemDialog fragment = new ManageFragmentItemDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {

        final int position = getArguments().getInt(ARG_POSITION);
        final String text = getArguments().getString(ARG_TEXT,"");

        listener = (ManageFragmentItemDialogListener) getTargetFragment();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogContent = inflater.inflate(R.layout.manage_fragment_item_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogContent).
                setTitle("Manage item");

        final EditText editText = (EditText) dialogContent.findViewById(R.id.manage_fragment_item_dialog_text_edittext);
        editText.setText(text);
        editText.setSelection(text.length());

        Button updateButton;
        updateButton = (Button) dialogContent.findViewById(R.id.manage_fragment_item_dialog_update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.length()>0) {
                    listener.manageFragmentItemDialogUpdateClick(getTag(), position, editText.getText().toString());
                    dismiss();
                }else{
                    Toast.makeText(getActivity(), "Empty text is not allowed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button deleteButton;
        deleteButton = (Button) dialogContent.findViewById(R.id.manage_fragment_item_dialog_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFragmentItemDialog dialog = DeleteFragmentItemDialog.newInstance(position);
                dialog.setTargetFragment(ManageFragmentItemDialog.this,0);
                dialog.show(getFragmentManager(),"delete");
            }
        });

        return builder.create();
    }

    @Override
    public void onDeleteFragmentItemDialogPositiveClick(String tag, int position) {
        listener.manageFragmentItemDialogDeleteClick(getTag(),position);
        dismiss();
    }


    public interface ManageFragmentItemDialogListener {
        void manageFragmentItemDialogUpdateClick(String tag, int position, String newText);
        void manageFragmentItemDialogDeleteClick(String tag, int position);

    }

    public boolean isAllowEmptyText() {
        return allowEmptyText;
    }

    public void setAllowEmptyText(boolean allowEmptyText) {
        this.allowEmptyText = allowEmptyText;
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



}
