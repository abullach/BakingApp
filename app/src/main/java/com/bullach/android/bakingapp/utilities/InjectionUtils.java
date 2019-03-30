package com.bullach.android.bakingapp.utilities;

import android.content.Context;

import com.bullach.android.bakingapp.data.RecipeRepository;
import com.bullach.android.bakingapp.data.RecipesDatabase;
import com.bullach.android.bakingapp.ui.viewmodels.RecipeDetailViewModelFactory;
import com.bullach.android.bakingapp.ui.viewmodels.RecipeListViewModelFactory;

public class InjectionUtils {

    public static RecipeRepository provideRecipeRepository(Context context) {
        RecipesDatabase database = RecipesDatabase.getDatabase(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        RecipeInterface recipesInterface = RetrofitClient.getClient().create(RecipeInterface.class);
        return RecipeRepository.getInstance(database, recipesInterface, executors);
    }

    public static RecipeListViewModelFactory provideRecipeListViewModelFactory(Context context) {
        RecipeRepository repository = provideRecipeRepository(context);
        return new RecipeListViewModelFactory(repository);
    }

    public static RecipeDetailViewModelFactory provideRecipeDetailViewModelFactory(Context context) {
        RecipeRepository repository = provideRecipeRepository(context);
        return new RecipeDetailViewModelFactory(repository);
    }
}
