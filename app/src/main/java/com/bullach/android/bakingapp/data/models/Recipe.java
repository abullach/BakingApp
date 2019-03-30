package com.bullach.android.bakingapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bullach.android.bakingapp.data.RecipeConverter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class Recipe implements Parcelable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @TypeConverters(RecipeConverter.class)
    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @TypeConverters(RecipeConverter.class)
    @SerializedName("steps")
    private List<Step> steps;

    @SerializedName("servings")
    private int servings;

    @SerializedName("image")
    private String image;

    public Recipe() {
    }

    public Recipe(int recipeId, String recipeName, List<Ingredient> ingredientList, List<Step> stepsList,
                  int recipeServings, String recipeImage) {
        id = recipeId;
        name = recipeName;
        ingredients = ingredientList;
        steps = stepsList;
        servings = recipeServings;
        image = recipeImage;
    }

    @Ignore
    private Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
