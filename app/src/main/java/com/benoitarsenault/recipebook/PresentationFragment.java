package com.benoitarsenault.recipebook;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.benoitarsenault.recipebook.model.Recipe;
import com.benoitarsenault.recipebook.model.RecipesProvider;

/**
 * Created by sseag on 2016-10-25.
 */

public class PresentationFragment extends android.support.v4.app.Fragment {
    private static final String ARG_RECIPE_ID = "recipeId";
    private static final String ARG_CURRENT_STEP_ID = "currentStepId";

    private Recipe recipe;
    private int currentStepIndex;
    PresentationFragmentListener listener;

    public static PresentationFragment newInstance(int recipeId, int currentStepIndex) {

        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        args.putInt(ARG_CURRENT_STEP_ID, currentStepIndex);

        PresentationFragment fragment = new PresentationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        int recipeId = getArguments().getInt(ARG_RECIPE_ID);
        currentStepIndex = getArguments().getInt(ARG_CURRENT_STEP_ID);
        recipe = RecipesProvider.getInstance().getItemById(recipeId, getActivity());

        listener = (PresentationFragmentListener) getActivity();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_presentation, container, false);

        int numberOfSteps = recipe.getSteps().size();

        TextView contentTextView = (TextView) rootView.findViewById(R.id.fragment_presentation_stepcontent_textview);
        contentTextView.setText(recipe.getSteps().get(currentStepIndex));

        TextView completionTextView = (TextView) rootView.findViewById(R.id.fragment_presentation_completion_textview);
        completionTextView.setText((currentStepIndex + 1) + " / " + numberOfSteps);

        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_presentation_completion_progressbar);
        progressBar.setMax(numberOfSteps);
        progressBar.setProgress(currentStepIndex + 1);

        Button previousButton = (Button) rootView.findViewById(R.id.fragment_presentation_previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPresentationFragmentPreviousButtonClicked();
            }
        });

        Button nextButton = (Button) rootView.findViewById(R.id.fragment_presentation_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPresentationFragmentNextButtonClicked();
            }
        });

        return rootView;
    }

    public interface PresentationFragmentListener {
        void onPresentationFragmentPreviousButtonClicked();
        void onPresentationFragmentNextButtonClicked();
    }
}
