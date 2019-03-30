package com.bullach.android.bakingapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bullach.android.bakingapp.R;
import com.bullach.android.bakingapp.data.models.Ingredient;
import com.bullach.android.bakingapp.data.models.Recipe;
import com.bullach.android.bakingapp.databinding.FragmentIngredientsListBinding;
import com.bullach.android.bakingapp.ui.adapters.IngredientsAdapter;
import com.bullach.android.bakingapp.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class IngredientsListFragment extends Fragment {

    /** Member variable for the Recipe */
    private Recipe mRecipe;

    /** Member variable for IngredientsAdapter */
    private IngredientsAdapter mIngredientsAdapter;

    /** This field is used for data binding */
    private FragmentIngredientsListBinding mIngredientListBinding;

    public IngredientsListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Instantiate FragmentMasterListIngredientsBinding using DataBindingUtil
        mIngredientListBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_ingredients_list, container, false);
        View rootView = mIngredientListBinding.getRoot();

        // Get the recipe data from the MainActivity
        mRecipe = getRecipeDataFromBundle();

        // Initialize a IngredientAdapter
        setupIngredientsAdapter();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the recipe data from the MainActivity
        mRecipe = getRecipeDataFromBundle();
    }

    /**
     * Gets recipe data from the RecipeListActivity
     *
     * @return The Recipe data
     */
    private Recipe getRecipeDataFromBundle() {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constants.RECIPE_EXTRA)) {
                // Receive the Recipe object from bundle
                Bundle bundle = intent.getBundleExtra(Constants.RECIPE_EXTRA);
                mRecipe = bundle.getParcelable(Constants.RECIPE_EXTRA);
            }
        }
        return mRecipe;
    }

    /**
     * Create a IngredientsAdapter and set it to the RecyclerView
     */
    private void setupIngredientsAdapter() {
        // Create a new list of the ingredient and indices
        List<Ingredient> ingredients = new ArrayList<>();

        // The IngredientsAdapter is responsible for displaying each ingredient in the list
        mIngredientsAdapter = new IngredientsAdapter(ingredients, mRecipe.getName());

        // A LinearLayoutManager is responsible for measuring and positioning item views within a
        // RecyclerView into a linear list.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        // Set the layout manager to the RecyclerView
        mIngredientListBinding.rvIngredients.setLayoutManager(layoutManager);
        // Use this setting to improve performance if you know that changes in content do not
        // change the child layout size in the RecyclerView
        mIngredientListBinding.rvIngredients.setHasFixedSize(true);

        // Set the IngredientsAdapter to the RecyclerView
        mIngredientListBinding.rvIngredients.setAdapter(mIngredientsAdapter);

        // Add a list of ingredients to the IngredientsAdapter
        mIngredientsAdapter.addAll(mRecipe.getIngredients());
    }
}
