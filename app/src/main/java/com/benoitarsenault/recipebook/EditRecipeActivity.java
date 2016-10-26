package com.benoitarsenault.recipebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.benoitarsenault.recipebook.dialogs.DeleteRecipeDialogFragment;
import com.benoitarsenault.recipebook.dialogs.DurationDialogFragment;
import com.benoitarsenault.recipebook.dialogs.UpdateRecipeDialogFragment;
import com.benoitarsenault.recipebook.model.Recipe;
import com.benoitarsenault.recipebook.model.RecipesProvider;

import java.util.ArrayList;

public class EditRecipeActivity extends AppCompatActivity implements SimpleListFragment.OnSimpleListFragmentInteractionListener, DeleteRecipeDialogFragment.DeleteRecipeDialogListener, UpdateRecipeDialogFragment.UpdateRecipeDialogListener, DurationDialogFragment.DurationDialogListener {

    public static final String EXTRA_RECIPE_ID = "recipeId";
    private static final String TAG_DURATION = "duration";
    private static final String STATE_DURATION = "stateDuration";
    private static final String STATE_INGREDIENTS = "stateIngredients";
    private static final String STATE_STEPS = "stateSteps";

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
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        portionSpinner.setAdapter(spinnerAdapter);
        portionSpinner.setSelection(recipe.getPortions() - 1);

        ingredientFragment = (SimpleListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_form_fragment_ingredients);
        ingredientFragment.setTitle("Ingredients");
        ingredientFragment.setDisplayOrderEnabled(false);

        ArrayList<String> test = recipe.getIngredients();
        ingredientFragment.setItems(recipe.getIngredients());

        stepsFragment = (SimpleListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_form_fragment_steps);
        stepsFragment.setTitle("Steps");
        stepsFragment.setDisplayOrderEnabled(true);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(EXTRA_RECIPE_ID);
            durationTextView.setText(savedInstanceState.getString(STATE_DURATION));
            ingredientFragment.setItems(savedInstanceState.getStringArrayList(STATE_INGREDIENTS));
            stepsFragment.setItems(savedInstanceState.getStringArrayList(STATE_STEPS));
        }
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

        if(nameTextView.length()>0) {
            recipe.setTitle(nameTextView.getText().toString());
            recipe.setDuration(durationTextView.getText().toString());
            recipe.setPortions(portionSpinner.getSelectedItemPosition() + 1);
            recipe.setIngredients(ingredientFragment.getItems());
            recipe.setSteps(stepsFragment.getItems());

            RecipesProvider.getInstance().updateItem(recipe, this);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }else{
            Toast.makeText(this, "Empty text is not allowed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDurationSelectedPositiveClick(String duration) {
        durationTextView.setText(duration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_presentation) {
            Intent intent = new Intent(EditRecipeActivity.this, PresentationActivity.class);
            intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
            startActivity(intent);
        }
        if (id == R.id.action_sendmail) {
            sendMail();
        }


        return super.onOptionsItemSelected(item);
    }

    private void sendMail() {
        String email = "";
        String subject = "Recipe " + recipe.getTitle();

        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append("Title:" + recipe.getTitle() + newLine);
        sb.append("Duration : " + recipe.getDuration() + newLine);
        sb.append("Portions : " + recipe.getPortions() + newLine);
        sb.append("Ingredients : " + recipe.getIngredients() + newLine);
        sb.append("Steps :" + recipe.getSteps() + newLine);
        String body = sb.toString();

        String chooserTitle = "Send " + recipe.getTitle() + " as mail";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text
        startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_RECIPE_ID, recipeId);
        outState.putString(STATE_DURATION, durationTextView.getText().toString());
        outState.putStringArrayList(STATE_INGREDIENTS, ingredientFragment.getItems());
        outState.putStringArrayList(STATE_STEPS, stepsFragment.getItems());

        super.onSaveInstanceState(outState);
    }
}
