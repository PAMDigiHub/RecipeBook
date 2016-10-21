package com.benoitarsenault.recipebook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.benoitarsenault.recipebook.dialogs.DurationDialogFragment;
import com.benoitarsenault.recipebook.model.Recipe;
import com.benoitarsenault.recipebook.model.RecipesProvider;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity implements SimpleListFragment.OnSimpleListFragmentInteractionListener, DurationDialogFragment.DurationDialogListener {

    private static final String TAG_DURATION = "duration";
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private Spinner portionSpinner;
    private TextView nameTextView;
    private TextView durationTextView;
    private SimpleListFragment ingredientFragment;
    private SimpleListFragment stepsFragment;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTextView = (TextView) findViewById(R.id.recipe_form_name_edit_text);
        durationTextView = (TextView) findViewById(R.id.duration_textview);
        durationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationDialogFragment durationDialog = DurationDialogFragment.newInstance(durationTextView.getText().toString());
                durationDialog.show(getFragmentManager(),TAG_DURATION);
            }
        });

        portionSpinner = (Spinner) findViewById(R.id.portion_spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(AddRecipeActivity.this,R.array.portions_choices,android.R.layout.simple_spinner_item);
        portionSpinner.setAdapter(spinnerAdapter);

        ingredientFragment = (SimpleListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_form_fragment_ingredients);
        ingredientFragment.setTitle("Ingredients");
        stepsFragment = (SimpleListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_form_fragment_steps);
        stepsFragment.setTitle("Steps");

        addButton = (Button) findViewById(R.id.content_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = nameTextView.getText().toString();
                String duration = durationTextView.getText().toString();
                int portions = Integer.parseInt(portionSpinner.getSelectedItem().toString());
                ArrayList<String> ingredients = ingredientFragment.getItems();
                ArrayList<String> steps = stepsFragment.getItems();

                Recipe recipe = new Recipe(title,duration,portions,ingredients,steps);
                RecipesProvider.getInstance().addItem(recipe,AddRecipeActivity.this);
                finish();
            }
        });
    }


    @Override
    public void onSimpleListFragmentItemsChanged(int fragmentId) {
        if(fragmentId==R.id.recipe_form_fragment_ingredients){

        }
        if(fragmentId==R.id.recipe_form_fragment_steps){

        }

    }

    @Override
    public void onDurationSelectedPositiveClick(String duration) {
        durationTextView.setText(duration);
    }
}
