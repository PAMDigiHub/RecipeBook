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

import com.benoitarsenault.recipebook.dialogs.DeleteRecipeDialogFragment;
import com.benoitarsenault.recipebook.dialogs.DurationDialogFragment;
import com.benoitarsenault.recipebook.dialogs.UpdateRecipeDialogFragment;
import com.benoitarsenault.recipebook.model.Recipe;
import com.benoitarsenault.recipebook.model.RecipesProvider;

import java.util.ArrayList;

public class EditRecipeActivity extends AppCompatActivity implements SimpleListFragment.OnSimpleListFragmentInteractionListener, DeleteRecipeDialogFragment.DeleteRecipeDialogListener, UpdateRecipeDialogFragment.UpdateRecipeDialogListener, DurationDialogFragment.DurationDialogListener {

    public static final String EXTRA_RECIPE_ID = "recipeId";
    private static final String TAG_DURATION = "duration";

    public int recipeId;

    private ArrayAdapter<CharSequence> spinnerAdapter;
    private Spinner portionSpinner;
    private TextView nameTextView;
    private TextView durationTextView;
    private SimpleListFragment ingredientFragment;
    private SimpleListFragment stepsFragment;
    private Button deleteButton;
    private Button updateButton;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recipeId = getIntent().getExtras().getInt(EXTRA_RECIPE_ID);

        recipe = RecipesProvider.getInstance().getItemById(recipeId, this);

        getSupportActionBar().setTitle(recipe.getTitle());
        nameTextView = (TextView) findViewById(R.id.recipe_form_name_edit_text);
        nameTextView.setText(recipe.getTitle());

        durationTextView = (TextView) findViewById(R.id.duration_textview);
        durationTextView.setText(recipe.getDuration());
        durationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationDialogFragment durationDialog = DurationDialogFragment.newInstance(durationTextView.getText().toString());
                durationDialog.show(getFragmentManager(), TAG_DURATION);
            }
        });

        portionSpinner = (Spinner) findViewById(R.id.portion_spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.portions_choices, android.R.layout.simple_spinner_item);
        portionSpinner.setAdapter(spinnerAdapter);
        portionSpinner.setSelection(recipe.getPortions() - 1);


        ingredientFragment = (SimpleListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_form_fragment_ingredients);
        ingredientFragment.setTitle("Ingredients");

        ArrayList<String> test = recipe.getIngredients();
        ingredientFragment.setItems(recipe.getIngredients());

        stepsFragment = (SimpleListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_form_fragment_steps);
        stepsFragment.setTitle("Steps");
        stepsFragment.setItems(recipe.getSteps());

        deleteButton = (Button) findViewById(R.id.activity_edit_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteRecipeDialogFragment deleteDialog = DeleteRecipeDialogFragment.newInstance();
                deleteDialog.show(getFragmentManager(), "");
            }
        });

        updateButton = (Button) findViewById(R.id.activity_edit_update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateRecipeDialogFragment updateDialog = UpdateRecipeDialogFragment.newInstance();
                updateDialog.show(getFragmentManager(), "");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onSimpleListFragmentItemsChanged(int fragmentId) {

    }

    @Override
    public void onDeleteRecipeDialogPositiveClick(String tag) {
        RecipesProvider.getInstance().removeItemById(recipe.getId(), EditRecipeActivity.this);
        finish();
    }

    @Override
    public void onUpdateRecipeDialogPositiveClick(String tag) {
        recipe.setTitle(nameTextView.getText().toString());
        recipe.setDuration(durationTextView.getText().toString());
        recipe.setPortions((int) portionSpinner.getSelectedItemPosition() + 1);
        recipe.setIngredients(ingredientFragment.getItems());
        recipe.setSteps(stepsFragment.getItems());

        RecipesProvider.getInstance().updateItem(recipe, this);
        finish();
    }

    @Override
    public void onDurationSelectedPositiveClick(String duration) {
        durationTextView.setText(duration);
    }
}
