package com.bullach.android.bakingapp.data;

import com.bullach.android.bakingapp.data.models.Ingredient;
import com.bullach.android.bakingapp.data.models.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class RecipeConverter implements Serializable {

    @TypeConverter
    public static String fromIngredientsList(List<Ingredient> ingredientList) {
        if (ingredientList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {
        }.getType();
        String json = gson.toJson(ingredientList, type);
        return json;
    }

    @TypeConverter
    public static List<Ingredient> toIngredientsList(String ingredientString) {
        if (ingredientString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {
        }.getType();
        List<Ingredient> productCategoriesList = gson.fromJson(ingredientString, type);
        return productCategoriesList;
    }


    @TypeConverter
    public static String fromStepsList(List<Step> stepList) {
        if (stepList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {
        }.getType();
        String json = gson.toJson(stepList, type);
        return json;
    }

    @TypeConverter
    public static List<Step> toStepsList(String stepString) {
        if (stepString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {
        }.getType();
        List<Step> productCategoriesList = gson.fromJson(stepString, type);
        return productCategoriesList;
    }
}
