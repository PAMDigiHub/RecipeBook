package com.benoitarsenault.recipebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.benoitarsenault.recipebook.model.Recipe;
import com.benoitarsenault.recipebook.model.RecipesProvider;
import com.benoitarsenault.recipebook.model.SortOrder;
import com.benoitarsenault.recipebook.model.adapters.RecipesAdapter;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {

    private static final String KEY_SORT_ORDER = "KEY_SORT_ORDER";
    private static final String KEY_SEARCH_CRITERIA = "KEY_SEARCH_CRITERIA";

    private GridView recipeGridView;
    private RecipesAdapter adapter;
    private String searchCriteria;
    private SortOrder sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipesActivity.this,ManageRecipeActivity.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState != null) {
            searchCriteria = savedInstanceState.getString(KEY_SEARCH_CRITERIA, "");
            sortOrder = SortOrder.createFromInt(savedInstanceState.getInt(KEY_SORT_ORDER, 0));
        } else {
            searchCriteria = "";
            sortOrder = SortOrder.ASC;
        }

/*
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Bacon");
        ArrayList<String> steps = new ArrayList<>();
        steps.add("ajouter bacon");
        Recipe recipe = new Recipe("title","15 minutes",4,ingredients,steps);
        RecipesProvider.getInstance().addItem(recipe,RecipesActivity.this);
*/
        adapter = new RecipesAdapter(RecipesActivity.this, RecipesProvider.getInstance().getOrderedRecipes(RecipesActivity.this,searchCriteria,sortOrder));
        recipeGridView = (GridView) findViewById(R.id.recipe_gridview);
        recipeGridView.setAdapter(adapter);

        Spinner spinner = (Spinner) findViewById(R.id.recipe_spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_order_choices, android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        sortOrder = SortOrder.ASC;
                        refreshCursor();
                        break;
                    case 1:
                        sortOrder = SortOrder.DESC;
                        refreshCursor();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SORT_ORDER, sortOrder.getValue());
        outState.putString(KEY_SEARCH_CRITERIA, searchCriteria);

        super.onSaveInstanceState(outState);
    }

    private AdapterView.OnItemSelectedListener onSortSpinnerItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            sortOrder = SortOrder.createFromInt(position);
            refreshCursor();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private void refreshCursor() {
        adapter.changeCursor(RecipesProvider.getInstance().getOrderedRecipes(this, searchCriteria, sortOrder));
        adapter.notifyDataSetChanged();
    }
}
