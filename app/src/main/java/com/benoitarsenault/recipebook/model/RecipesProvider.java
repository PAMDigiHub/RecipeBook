package com.benoitarsenault.recipebook.model;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by sseag on 2016-10-05.
 */
public class RecipesProvider {
    private static RecipesProvider ourInstance = new RecipesProvider();

    public static RecipesProvider getInstance() {
        return ourInstance;
    }

    private RecipesProvider() {
    }

    public Cursor getOrderedRecipes(Context context, String criteria, SortOrder order) {
        return db(context).getOrdered(criteria, order);
    }

    private RecipesDbHelper db(Context context) {
        return new RecipesDbHelper(context);
    }
    public Recipe getItemById(int id, Context context){
        return db(context).findById(id);
    }

    public void removeItemById(int id, Context context){
        db(context).deleteById(id);
    }

    public void addItem(Recipe recipe, Context context){
        db(context).insertRecipe(recipe);
    }

    public void updateItem(Recipe recipe, Context context){
        db(context).update(recipe);
    }
}
