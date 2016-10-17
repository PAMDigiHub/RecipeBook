package com.benoitarsenault.recipebook.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by sseag on 2016-10-05.
 */

public class RecipesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "com.benoitarsenault.recipebook.db";
    private static final int DATABASE_VERSION = 1;

    public RecipesDbHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
        String path = context.getDatabasePath("com.benoitarsenault.recipebook.db").toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RecipeContract.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(RecipeContract.SQL_DELETE_IF_EXISTS);

        onCreate(db);
    }

    public static Recipe recipeFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(RecipeContract.COLUMN_ID));
        String title = cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_TITLE));
        String duration = cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_DURATION));
        int portions = cursor.getInt(cursor.getColumnIndex(RecipeContract.COLUMN_PORTIONS));
        String ingredients = cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_INGREDIENTS));
        String steps = cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_STEPS));

        return new Recipe(id, title, duration, portions, splitToArrayList(ingredients), splitToArrayList(steps));
    }

    public static ArrayList<String> splitToArrayList(String joinedString) {
        ArrayList<String> list = new ArrayList<>();
        for (String item : TextUtils.split(joinedString, "|")) {
            list.add(item);
        }
        return list;
    }

    public static String joinArrayListToString(ArrayList<String> list) {
        return TextUtils.join("|", list.toArray());
    }

    private ContentValues getRecipeValues(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeContract.COLUMN_TITLE, recipe.getTitle());
        values.put(RecipeContract.COLUMN_DURATION,recipe.getDuration());
        values.put(RecipeContract.COLUMN_PORTIONS,recipe.getPortions());
        values.put(RecipeContract.COLUMN_INGREDIENTS,joinArrayListToString(recipe.getIngredients()));
        values.put(RecipeContract.COLUMN_STEPS,joinArrayListToString(recipe.getSteps()));
        return values;
    }

    public Cursor getOrdered(String criteria, SortOrder order) {
        SQLiteDatabase db = getReadableDatabase();


        String[] projection = RecipeContract.ALL_COLUMNS;
        String sort = "lower(" + RecipeContract.COLUMN_TITLE + ") " + order.getSqliteValue();

        String lowercaseCriteria = "%" + criteria.toLowerCase() + "%";
        String selection = String.format("lower(%s) LIKE ? OR lower(%s) LIKE ?",
                RecipeContract.COLUMN_TITLE, RecipeContract.COLUMN_INGREDIENTS);
        String[] selectionArgs = {lowercaseCriteria, lowercaseCriteria};

        Cursor cursor = db.query(
                RecipeContract.TABLE,                     // The table to query
                projection,                                 // The columns to return
                selection,                                  // The columns for the WHERE clause
                selectionArgs,                              // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sort                                        // The sort order
        );

        return cursor;
    }

    public void insertRecipe(Recipe recipe) {
        SQLiteDatabase db = getWritableDatabase();

        db.insert(RecipeContract.TABLE, null, getRecipeValues(recipe));
    }

    public void update(Recipe recipe) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = RecipeContract.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(recipe.getId())};

        db.update(
                RecipeContract.TABLE,
                getRecipeValues(recipe),
                selection,
                selectionArgs);
    }

    public Recipe findById(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = RecipeContract.ALL_COLUMNS;
        String selection = RecipeContract.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                RecipeContract.TABLE,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (cursor.moveToFirst() == true) {
            //recipe found
            return recipeFromCursor(cursor);
        } else {
            return null;
        }
    }

    public void deleteById(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = RecipeContract.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(RecipeContract.TABLE, selection, selectionArgs);

    }

    private static class RecipeContract {
        public static final String TABLE = "recipe";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_PORTIONS = "portions";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_STEPS = "steps";
        public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_TITLE, COLUMN_DURATION, COLUMN_PORTIONS, COLUMN_INGREDIENTS, COLUMN_STEPS};

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_DURATION + " TEXT," +
                COLUMN_PORTIONS + " INTEGER," +
                COLUMN_INGREDIENTS + " TEXT," +
                COLUMN_STEPS + " TEXT)";
        public static final String SQL_DELETE_IF_EXISTS = "DROP TABLE IF EXISTS " + TABLE;
    }


}
