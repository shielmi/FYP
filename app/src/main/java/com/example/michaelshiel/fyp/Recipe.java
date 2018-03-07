package com.example.michaelshiel.fyp;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelshiel on 28/02/2018.
 */

public class Recipe {
    private String name;
    private String calories;
    private String prepTime;
    private String cookTime;
    private String protein;
    private String carbs;
    private String fat;
    private String measurement;
    private List ingredients;
    private List instructions;
    private String survingSuggestion;
    private String writer;
    private String url;
    private DatabaseReference theReference;

    public Recipe() {
    }

    public Recipe(String name, String calories, String prepTime, String cookTime, String protein, String carbs, String fat, String measurement,
                  List ingredients, List instructions, String survingSuggestion, String writer, String URL, DatabaseReference r) {

        this.name = name;
        this.calories = calories;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.measurement = measurement;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.survingSuggestion = survingSuggestion;
        this.writer = writer;
        this.url = URL;


    }

    public String getName() {
        return name;
    }

    public String getCalories() {
        return calories;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public String getProtein() {
        return protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public String getFat() {
        return fat;
    }

    public List getIngredients() {
        return this.ingredients;
    }

    public List getInstructions() {
        return this.instructions;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getSurvingSuggestion() {
        return survingSuggestion;
    }

    public String getWriter() {
        return writer;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public void setIngredients(List ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(List instructions) {
        this.instructions = instructions;
    }

    public void setSurvingSuggestion(String survingSuggestion) {
        this.survingSuggestion = survingSuggestion;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}





