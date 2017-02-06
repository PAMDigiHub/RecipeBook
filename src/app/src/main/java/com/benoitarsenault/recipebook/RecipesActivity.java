package com.benoitarsenault.recipebook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.SearchView;

import com.benoitarsenault.recipebook.model.Recipe;
import com.benoitarsenault.recipebook.model.RecipesDbHelper;
import com.benoitarsenault.recipebook.model.RecipesProvider;
import com.benoitarsenault.recipebook.model.SortOrder;
import com.benoitarsenault.recipebook.model.adapters.RecipesAdapter;

public class RecipesActivity extends AppCompatActivity {

    private static final String KEY_SORT_ORDER = "KEY_SORT_ORDER";
    private static final String KEY_SEARCH_CRITERIA = "KEY_SEARCH_CRITERIA";

    private ListView recipeListView;
    private RecipesAdapter adapter;
    private String searchCriteria;
    private SortOrder sortOrder;
    private SearchView searchView;

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
                Intent intent = new Intent(RecipesActivity.this, AddRecipeActivity.class);
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

        adapter = new RecipesAdapter(RecipesActivity.this, RecipesProvider.getInstance().getOrderedRecipes(RecipesActivity.this, searchCriteria, sortOrder));
        recipeListView = (ListView) findViewById(R.id.recipe_listview);
        recipeListView.setAdapter(adapter);
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selectedRecipe = RecipesDbHelper.recipeFromCursor((Cursor) adapter.getItem(position));

                Intent editRecipeIntent = new Intent(RecipesActivity.this, EditRecipeActivity.class);
                editRecipeIntent.putExtra(EditRecipeActivity.EXTRA_RECIPE_ID, selectedRecipe.getId());
                startActivity(editRecipeIntent);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.recipe_spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_order_choices, android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
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

        searchView = (SearchView) findViewById(R.id.recipe_searchview);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refreshCursor();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchCriteria = newText;
                refreshCursor();
                return false;
            }
        });

        if (savedInstanceState != null) {
            searchView.setQuery(searchCriteria, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCursor();
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
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void refreshCursor() {
        adapter.changeCursor(RecipesProvider.getInstance().getOrderedRecipes(this, searchCriteria, sortOrder));
        adapter.notifyDataSetChanged();
    }
}
