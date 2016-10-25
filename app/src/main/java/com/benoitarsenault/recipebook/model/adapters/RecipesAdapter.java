package com.benoitarsenault.recipebook.model.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.benoitarsenault.recipebook.R;
import com.benoitarsenault.recipebook.model.Recipe;
import com.benoitarsenault.recipebook.model.RecipesDbHelper;

/**
 * Created by sseag on 2016-10-05.
 */

public class RecipesAdapter extends CursorAdapter {

    public RecipesAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.recipe_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = (TextView) view.findViewById(R.id.recipe_item_title);
        TextView duration = (TextView) view.findViewById(R.id.recipe_item_duration);
        TextView portions = (TextView) view.findViewById(R.id.recipe_item_portions);

        Recipe recipe = RecipesDbHelper.recipeFromCursor(cursor);
        if(recipe!=null){
            title.setText(recipe.getTitle());
            duration.setText("Duration : "+recipe.getDuration());
            portions.setText("Portions : "+recipe.getPortions()+"");
        }
    }
}
