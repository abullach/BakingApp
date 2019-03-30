package com.bullach.android.bakingapp.data;

import android.util.Log;

import com.bullach.android.bakingapp.data.models.Ingredient;
import com.bullach.android.bakingapp.data.models.Recipe;
import com.bullach.android.bakingapp.utilities.AppExecutors;
import com.bullach.android.bakingapp.utilities.RecipeInterface;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {

    private static final String TAG = RecipeRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile RecipeRepository sInstance;

    private final RecipeInterface mRecipeInterface;
    private final RecipesDatabase mRecipesDatabase;
    private final AppExecutors mExecutors;

    private RecipeRepository(RecipesDatabase recipeDatabase, RecipeInterface recipesInterface, AppExecutors executors) {
        mRecipesDatabase = recipeDatabase;
        mRecipeInterface = recipesInterface;
        mExecutors = executors;
    }

    public synchronized static RecipeRepository getInstance(
            RecipesDatabase recipeDatabase, RecipeInterface recipesInterface, AppExecutors executors) {
        Log.d(TAG, "Getting the RecipeRepository.");
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new RecipeRepository.");
                sInstance = new RecipeRepository(recipeDatabase, recipesInterface, executors);
            }
        }
        return sInstance;
    }

    /**
     * Make a network request by calling enqueue and provide a LiveData of recipe lists for ViewModel
     */
    public LiveData<List<Recipe>> loadAllRecipes() {
        final MutableLiveData<List<Recipe>> recipeListLiveData = new MutableLiveData<>();

        mRecipeInterface.getAllRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipeList = response.body();
                    Log.d(TAG, "Parsing finished. number of recipes: " +recipeList.size());
                    recipeListLiveData.setValue(recipeList);
                } else {
                    Log.d(TAG, "Error code: " +response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                recipeListLiveData.setValue(null);
                Log.d(TAG, "Unknown error: " +t.getMessage());
            }
        });
        return recipeListLiveData;
    }

    public List<Ingredient> getAllIngredients() {
        return mRecipesDatabase.ingredientsDao().getAllIngredients();
    }

    public List<Recipe> getAllRecipes() {
        return mRecipesDatabase.recipesDao().getAllRecipes();
    }

    public void saveRecipe(final Recipe recipe) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mRecipesDatabase.recipesDao().deleteRecipe();
//                mRecipesDatabase.recipesDao().insertAllRecipes(recipe);
                insertRecipes(recipe);


                mRecipesDatabase.ingredientsDao().deleteIngredient();
                insertIngredients(recipe.getIngredients());
            }
        });
    }

    private void insertRecipes(Recipe recipe) {
        mRecipesDatabase.recipesDao().insertRecipe(recipe);
    }

    private void insertIngredients(List<Ingredient> ingredients) {
        mRecipesDatabase.ingredientsDao().insertAllIngredients(ingredients);
    }
}
