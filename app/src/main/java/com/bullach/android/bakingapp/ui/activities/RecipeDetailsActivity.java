package com.bullach.android.bakingapp.ui.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bullach.android.bakingapp.R;
import com.bullach.android.bakingapp.data.RecipeConverter;
import com.bullach.android.bakingapp.data.models.Recipe;
import com.bullach.android.bakingapp.data.models.Step;
import com.bullach.android.bakingapp.databinding.ActivityRecipeDetailsBinding;
import com.bullach.android.bakingapp.ui.fragments.StepDetailFragment;
import com.bullach.android.bakingapp.ui.viewmodels.RecipeDetailViewModel;
import com.bullach.android.bakingapp.ui.viewmodels.RecipeDetailViewModelFactory;
import com.bullach.android.bakingapp.ui.widget.RecipeWidget;
import com.bullach.android.bakingapp.utilities.Constants;
import com.bullach.android.bakingapp.utilities.InjectionUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();

    /** ViewModel for RecipeListActivity */
    private RecipeDetailViewModel mRecipeDetailViewModel;

    /** This field is used for data binding **/
    private ActivityRecipeDetailsBinding mRecipeDetailBinding;

    /** Member variable for the recipe */
    private Recipe mRecipe;

    /** Member variable for total number of steps in the recipe */
    private int mTotalSteps;

    /** Member variable for whether it's single-pane (<code>true</code>), or two-pane layout (tablet in landscape only). */
    public boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use DataBinding for the layout
        mRecipeDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details);

        // Get the bundle with recipe data from the RecipeListActivity
        getRecipeDataFromBundle();

        // Handle error case
        if (mRecipe == null) {
            closeOnError();
            return;
        }
        // Set the recipe name as toolbar title
        setupToolbar();
        // Display the image of recipe
        displayImage();
        // Setup ViewModel
        mRecipeDetailViewModel = setupViewModel(this);

        // Check if it's a two pane or single pane layout
        if (mRecipeDetailBinding.flStepDetail != null) {
            // The StepDetailFragment container will only exist if it is a two pane layout (only tablets in landscape orientation).
            Log.d(TAG, "Device isTablet: " +getResources().getBoolean(R.bool.isTablet));
            mTwoPane = true;
        }

        if (savedInstanceState == null) {
            // Initialize RecipeDetailViewModel and if two pane layout set the current step to 0
            mRecipeDetailViewModel.init(mRecipe, mTwoPane);
            saveRecipeToSharedPreferences(mRecipe);
            sendRefreshBroadcastToWidget();
            // Toast.makeText(this, "sendRefreshBroadcastToWidget", Toast.LENGTH_LONG).show(); // Toast is for debugging purposes
            }

        // Observe click event on the steps in the list
        mRecipeDetailViewModel.getStepClickEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer position) {
                Step currentStep = mRecipeDetailViewModel.getCurrentStep().getValue();
                // If it's a two pane layout open the StepDetailFragment with the current selected step
                if (mTwoPane) {
                    // Get the total steps for this recipe
                    if(mRecipeDetailViewModel.getStepsList().getValue() != null) {
                        mTotalSteps = mRecipeDetailViewModel.getStepsList().getValue().size();
                    } else {
                        mTotalSteps = 0;
                    }
                    // Create a new StepDetailFragment and pass the current step to it
                    StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(currentStep, mTotalSteps);
                    // Add the fragment to its container using the FragmentManager and a transaction
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flStepDetail, stepDetailFragment)
                            .commit();
                } else {
                    // If it's not a two pane layout, start the RecipeDetailsActivity with the current selected step
                    ArrayList<Step> steps = new ArrayList<>(mRecipe.getSteps());
                    Intent intent = new Intent(RecipeDetailsActivity.this, StepDetailActivity.class);
                    intent.putParcelableArrayListExtra(Constants.RECIPE_EXTRA, steps);
                    intent.putExtra(Constants.STEP_INDEX_EXTRA, position);
                    intent.putExtra(Constants.RECIPE_TITLE, mRecipe.getName());
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Set the toolbar title.
     */
    private void setupToolbar() {
        // Set the title for a selected recipe
        setTitle(mRecipe.getName());
    }

    /**
     * Sets the recipe image if exist, otherwise sets a fallback image.
     */
    private void displayImage() {
        String imageUrl = mRecipe.getImage();
        if (imageUrl.isEmpty()) {
            // If image url is empty, fallback to a default image drawable
            mRecipeDetailBinding.ivRecipeImage.setImageResource(R.drawable.baking_ingredients);
        } else {
            // If image url exist try to load the image with Picasso
            Picasso.with(this)
                    .load(imageUrl)
                    .error(R.drawable.baking_ingredients)
                    .placeholder(R.drawable.baking_ingredients)
                    .into(mRecipeDetailBinding.ivRecipeImage);
        }
    }

    /**
     * Gets recipe data from the RecipeListActivity
     * <br>
     * @return The recipe data
     */
    private Recipe getRecipeDataFromBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constants.RECIPE_EXTRA)) {
                // Receive the Recipe object
                Bundle bundle = intent.getBundleExtra(Constants.RECIPE_EXTRA);
                mRecipe = bundle.getParcelable(Constants.RECIPE_EXTRA);
            }
        }
        return mRecipe;
    }

    /**
     * Get the RecipeDetailViewModel from the ViewModelFactory.
     */
    public static RecipeDetailViewModel setupViewModel(FragmentActivity activity) {
        RecipeDetailViewModelFactory factory = InjectionUtils.provideRecipeDetailViewModelFactory(activity);
        return ViewModelProviders.of(activity, factory).get(RecipeDetailViewModel.class);
    }

    private void closeOnError() {
        mRecipeDetailBinding.emptyView.setVisibility(View.VISIBLE);
    }

    private void sendRefreshBroadcastToWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));

        Intent updateAppWidgetIntent = new Intent();
        updateAppWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateAppWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        sendBroadcast(updateAppWidgetIntent);
    }

    private void saveRecipeToSharedPreferences(Recipe recipe) {
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // Save the recipe data used for launching the DetailActivity from the widget
        editor.putInt(getString(R.string.pref_recipe_id_key), recipe.getId());
        editor.putString(getString(R.string.pref_recipe_name_key), recipe.getName());

        // Get the ingredient list and use TypeConverter to convert the list to string
        String ingredientString = RecipeConverter.fromIngredientsList(recipe.getIngredients());
        // Save the string to SharedPreferences to use later for displaying in the app widget
        editor.putString(getString(R.string.pref_ingredient_string_key), ingredientString);

        // Get the step list and use TypeConverter to convert the list to string
        String stepString = RecipeConverter.fromStepsList(recipe.getSteps());
        // Save the string to SharedPreferences to use later for displaying in the app widget
        editor.putString(getString(R.string.pref_step_string_key), stepString);

        editor.putInt(getString(R.string.pref_servings_key), recipe.getServings());
        editor.putString(getString(R.string.pref_image_key), recipe.getImage());

        editor.apply();
    }
}
