package com.bullach.android.bakingapp.ui.activities;

import android.os.Bundle;
import android.view.View;

import com.bullach.android.bakingapp.R;
import com.bullach.android.bakingapp.data.models.Recipe;
import com.bullach.android.bakingapp.databinding.ActivityRecipeListBinding;
import com.bullach.android.bakingapp.ui.adapters.RecipesAdapter;
import com.bullach.android.bakingapp.ui.viewmodels.RecipeListViewModel;
import com.bullach.android.bakingapp.ui.viewmodels.RecipeListViewModelFactory;
import com.bullach.android.bakingapp.utilities.InjectionUtils;
import com.bullach.android.bakingapp.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

public class RecipeListActivity extends AppCompatActivity {

    /** Member variable for RecipeAdapter */
    private RecipesAdapter mRecipesAdapter;

    /** Member variable for data binding **/
    private ActivityRecipeListBinding mRecipeListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use DataBinding for the layout
        mRecipeListBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);
        // Show progress bar until data is loaded
        mRecipeListBinding.loadingIndicator.setVisibility(View.VISIBLE);
        mRecipeListBinding.rvRecipeList.setVisibility(View.INVISIBLE);

        // Check if internet connection is available
        boolean isConnected = NetworkUtils.checkNetworkAvailability(this);
        if (!isConnected) {
            // Show empty view if there is no internet connection.
            noInternetConnection();
        } else {
            // Setup the RecipesAdapter
            setupRecipesAdapter();

            // Observe data and update UI
            setupViewModel();

            //
            mRecipeListBinding.loadingIndicator.setVisibility(View.INVISIBLE);
            mRecipeListBinding.rvRecipeList.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Creates a GridLayoutManager and RecipesAdapter and set them to the RecyclerView.
     */
    private void setupRecipesAdapter() {
        // The number of columns in the grid layout depends on the device smallest width. Show 1 column for phones; 3 for tablets.
        final GridLayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.recipe_list_grid_spans));

        // Create new RecipesAdapter for displaying the list of recipes
        mRecipesAdapter = new RecipesAdapter(new ArrayList<>());

        // Set LayoutManager and Adapter to the  RecyclerView
        mRecipeListBinding.rvRecipeList.setLayoutManager(layoutManager);
        mRecipeListBinding.rvRecipeList.setAdapter(mRecipesAdapter);
    }

    /**
     * Get the RecipeListViewModel from the ViewModelFactory and observe the recipe list.
     * Each time the recipe data gets updated, the onChanged callback will be invoked and updates the UI.
     */
    private void setupViewModel() {
        // Get the RecipeListViewModel from the ViewModel factory
        RecipeListViewModelFactory factory = InjectionUtils.provideRecipeListViewModelFactory(this);
        RecipeListViewModel mRecipeListViewModel = ViewModelProviders.of(this, factory).get(RecipeListViewModel.class);

        // Retrieve live data object using getRecipes() method from the ViewModel. Observes and updates the list of recipes.
        mRecipeListViewModel.getRecipes().observe(RecipeListActivity.this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipe) {
                if (recipe != null) {
                    mRecipesAdapter.addAll(recipe);
                }
            }
        });
    }

    /**
     * Show empty view if there is no internet connection.
     */
    public void noInternetConnection() {
        mRecipeListBinding.loadingIndicator.setVisibility(View.INVISIBLE);
        mRecipeListBinding.emptyView.setText(R.string.recipe_list_no_internet_connection);
    }
}
