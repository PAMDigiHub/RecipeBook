package com.benoitarsenault.recipebook.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.benoitarsenault.recipebook.PresentationFragment;
import com.benoitarsenault.recipebook.R;

/**
 * Created by sseag on 2016-10-25.
 */

public class PresentationLastPageDialog extends DialogFragment {

    PresentationLastPageDialogListener listener;


    public static PresentationLastPageDialog newInstance() {

        Bundle args = new Bundle();

        PresentationLastPageDialog fragment = new PresentationLastPageDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        listener = (PresentationLastPageDialogListener) getActivity();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.presentation_last_page_dialog, null);

        Button firstStepButton = (Button) view.findViewById(R.id.presentation_last_page_dialog_first_step_button);
        firstStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFirstStepButtonClicked();
                dismiss();
            }
        });

        Button detailButton = (Button) view.findViewById(R.id.presentation_last_page_dialog_detail_button);
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReturnToDetailClicked();
                dismiss();
            }
        });

        builder.setView(view).setTitle("Last step reached, please choose next action");
        return builder.create();
    }

    public interface PresentationLastPageDialogListener{
        public void onFirstStepButtonClicked();
        public void onReturnToDetailClicked();
    }
}
