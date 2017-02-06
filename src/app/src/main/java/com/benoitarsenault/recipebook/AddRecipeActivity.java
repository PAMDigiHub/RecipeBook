package com.benoitarsenault.recipebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.benoitarsenault.recipebook.dialogs.DurationDialogFragment;
import com.benoitarsenault.recipebook.fragments.SimpleListFragment;
import com.benoitarsenault.recipebook.model.Recipe;
import com.benoitarsenault.recipebook.model.RecipesProvider;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity implements DurationDialogFragment.DurationDialogListener {

    private static final String TAG_DURATION = "duration";
    private static final String STATE_DURATION = "stateDuration";
    private static final String STATE_INGREDIENTS = "stateIngredients";
    private static final String STATE_STEPS = "stateSteps";

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initLayout();

        if (savedInstanceState != null) {
            durationTextView.setText(savedInstanceState.getString(STATE_DURATION));
            ingredientFragment.setItems(savedInstanceState.getStringArrayList(STATE_INGREDIENTS));
            stepsFragment.setItems(savedInstanceState.getStringArrayList(STATE_STEPS));
        }
    }

    private void initLayout() {
        nameTextView = (TextView) findViewById(R.id.recipe_form_name_edit_text);

        durationTextView = (TextView) findViewById(R.id.duration_textview);
        durationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationDialogFragment durationDialog = DurationDialogFragment.newInstance(durationTextView.getText().toString());
                durationDialog.show(getFragmentManager(), TAG_DURATION);
            }
        });

        spinnerAdapter = ArrayAdapter.createFromResource(AddRecipeActivity.this, R.array.portions_choices, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        portionSpinner = (Spinner) findViewById(R.id.portion_spinner);
        portionSpinner.setAdapter(spinnerAdapter);

        ingredientFragment = (SimpleListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_form_fragment_ingredients);
        ingredientFragment.setTitle("Ingredients");
        ingredientFragment.setDisplayOrderEnabled(false);

        stepsFragment = (SimpleListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_form_fragment_steps);
        stepsFragment.setTitle("Steps");
        stepsFragment.setDisplayOrderEnabled(true);

        addButton = (Button) findViewById(R.id.content_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean listsAreNotEmpty = ingredientFragment.getItems().size() > 0 && stepsFragment.getItems().size() > 0;
                boolean nameIsNotEmpty = nameTextView.getText().length() > 0;

                if (nameIsNotEmpty && listsAreNotEmpty) {
                    String title = nameTextView.getText().toString();
                    String duration = durationTextView.getText().toString();
                    int portions = Integer.parseInt(portionSpinner.getSelectedItem().toString());
                    ArrayList<String> ingredients = ingredientFragment.getItems();
                    ArrayList<String> steps = stepsFragment.getItems();

                    Recipe recipe = new Recipe(title, duration, portions, ingredients, steps);
                    RecipesProvider.getInstance().addItem(recipe, AddRecipeActivity.this);
                    finish();
                } else {
                    Toast.makeText(AddRecipeActivity.this, "Name and lists must not be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // outState.putInt(EXTRA_RECIPE_ID,recipeId);
        outState.putString(STATE_DURATION, durationTextView.getText().toString());
        outState.putStringArrayList(STATE_INGREDIENTS, ingredientFragment.getItems());
        outState.putStringArrayList(STATE_STEPS, stepsFragment.getItems());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDurationSelectedPositiveClick(String duration) {
        durationTextView.setText(duration);
    }
}
