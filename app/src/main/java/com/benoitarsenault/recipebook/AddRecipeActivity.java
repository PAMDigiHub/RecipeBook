package com.benoitarsenault.recipebook;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddRecipeActivity extends AppCompatActivity implements SimpleListFragment.OnFragmentInteractionListener {

    private ArrayAdapter<CharSequence> spinnerAdapter;
    private Spinner spinner;

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


        spinner = (Spinner) findViewById(R.id.portion_spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(AddRecipeActivity.this,R.array.portions_choices,android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerAdapter);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
