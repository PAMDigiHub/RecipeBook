package com.benoitarsenault.recipebook.model;

import java.util.ArrayList;

/**
 * Created by sseag on 2016-10-05.
 */

public class Recipe {
    //private static int autoId=0;

    private int id;
    private String title;
    private String duration;
    private int portions;
    private ArrayList<String> ingredients;
    private ArrayList<String> steps;


    public Recipe(int id, String title, String duration, int portions, ArrayList<String> ingredients, ArrayList<String> steps) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.portions = portions;
        this.ingredients = ingredients;
        this.steps = steps;
    }
    public Recipe(String title, String duration, int portions, ArrayList<String> ingredients, ArrayList<String> steps) {
        this(-1,title,duration,portions,ingredients,steps);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }
}
