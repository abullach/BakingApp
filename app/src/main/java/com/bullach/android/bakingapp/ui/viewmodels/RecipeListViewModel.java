package com.bullach.android.bakingapp.ui.viewmodels;

import com.bullach.android.bakingapp.data.RecipeRepository;
import com.bullach.android.bakingapp.data.models.Recipe;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class RecipeListViewModel extends ViewModel {

    private final RecipeRepository mRepository;
    // LiveData member variable to cache the list of words.
    private LiveData<List<Recipe>> mRecipes;

    public RecipeListViewModel(RecipeRepository repository) {
        mRepository = repository;
        mRecipes = mRepository.loadAllRecipes();
    }

    /**
     * Returns LiveData of the list of Recipes
     */
    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    /**
     * Sets a new value for the list of recipes
     */
    public void setRecipes() {
        mRecipes = mRepository.loadAllRecipes();
    }
}
