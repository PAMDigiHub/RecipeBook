package com.benoitarsenault.recipebook.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
public class EditItemDialogFragment extends android.support.v4.app.DialogFragment {

   public static final String ARG_TEXT = "text";
   public static final String ARG_TITLE = "title";
    public static final String ARG_POSITIVETEXT = "positiveText";
    public static final String ARG_REQUEST_CODE = "requestCode";

    private EditText editText;
   private boolean allowEmptyText = false;
   private EditItemDialogListener listener;


    public static EditItemDialogFragment newInstance(String text, String title/*,int requestCode*/) {

        Bundle args = new Bundle();
        args.putString(ARG_TEXT,text);
        args.putString(ARG_TITLE,title);
       /* args.putInt(ARG_REQUEST_CODE,requestCode);*/

        EditItemDialogFragment fragment = new EditItemDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static EditItemDialogFragment newInstance(String text, String title, String positiveButtonText/*,int requestCode*/) {

        Bundle args = new Bundle();
        args.putString(ARG_TEXT,text);
        args.putString(ARG_TITLE,title);
        args.putString(ARG_POSITIVETEXT,positiveButtonText);
        /*args.putInt(ARG_REQUEST_CODE,requestCode);*/

        EditItemDialogFragment fragment = new EditItemDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

   @Override
   public final Dialog onCreateDialog(Bundle savedInstanceState) {

       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       LayoutInflater inflater = getActivity().getLayoutInflater();
       View view = inflater.inflate(R.layout.text_input_dialog, null);


       String text = getArguments().getString(ARG_TEXT);
       String title = getArguments().getString(ARG_TITLE);
       final int requestCode = getArguments().getInt(ARG_REQUEST_CODE);
       String positiveText = "Apply";
       if(getArguments().getString(ARG_POSITIVETEXT)!=null){
           positiveText = getArguments().getString(ARG_POSITIVETEXT);
       }

       editText = (EditText) view.findViewById(R.id.text_input_dialog_edit_text);
       editText.setText(text);
       editText.setSelection(editText.getText().length());
       editText.requestFocus();

       listener = (EditItemDialogListener) getTargetFragment();


       builder.setTitle(title)
               .setView(view)
               .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if (!allowEmptyText && editText.getText().length() == 0) {
                           Toast.makeText(getContext(), "Empty text is not allowed", Toast.LENGTH_SHORT).show();

                       } else {
                           listener.onEditItemDialogPositiveClick(getTag(),requestCode,editText.getText().toString());
                       }
                   }
               })
               .setNegativeButton("Cancel", null);

       return builder.create();
   }

   public interface EditItemDialogListener {
       void onEditItemDialogPositiveClick(String tag,  int requestCode,String newText);
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
