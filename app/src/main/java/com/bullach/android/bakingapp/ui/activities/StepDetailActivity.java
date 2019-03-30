package com.bullach.android.bakingapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.bullach.android.bakingapp.R;
import com.bullach.android.bakingapp.data.models.Step;
import com.bullach.android.bakingapp.databinding.ActivityStepDetailBinding;
import com.bullach.android.bakingapp.ui.fragments.StepDetailFragment;
import com.bullach.android.bakingapp.ui.viewmodels.StepDetailViewModel;
import com.bullach.android.bakingapp.utilities.Constants;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class StepDetailActivity extends AppCompatActivity {

    /** ViewModel for RecipeListActivity */
    private StepDetailViewModel mStepDetailViewModel;

    private String mRecipeTitle;

    /** Variable to store the index of the step that this activity displays */
    private int mStepIndex;

    ArrayList<Step> mSteps = new ArrayList<>();

    /** This field is used for data binding **/
    private ActivityStepDetailBinding mStepDetailBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use DataBinding for the layout
        mStepDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);

        // Get the bundle with recipe data from the RecipeListActivity
        getRecipeDataFromBundle();

        // Set the title for a selected recipe
        setTitle(mRecipeTitle);

        // Show back arrow button in the actionbar
        showUpButton();

        mStepDetailViewModel = setupViewModel(this);

        if (savedInstanceState == null) {
            mStepDetailViewModel.init(mSteps, mStepIndex);
        }

        mStepDetailViewModel.getNavigateToStepDetail().observe(this, new Observer<Step>() {
            @Override
            public void onChanged(Step step) {
                // Get the total number of steps
                int numOfSteps = mStepDetailViewModel.getNumberOfSteps();

                // Create a new StepDetailFragment and pass current step and step total to it
                StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(step, numOfSteps);

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.flStepDetail, stepDetailFragment)
                        .commit();
            }
        });
    }


    /**
     * Gets recipe data from the RecipeDetailsActivity
     */
    private void getRecipeDataFromBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constants.RECIPE_EXTRA)) {
                mSteps = getIntent().getParcelableArrayListExtra(Constants.RECIPE_EXTRA);
                mStepIndex = getIntent().getIntExtra(Constants.STEP_INDEX_EXTRA, -1);
                mRecipeTitle = getIntent().getStringExtra(Constants.RECIPE_TITLE);
            }
        }
    }

    /**
     * Display the up button in the actionbar.
     */
    private void showUpButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Get the StepDetailViewModel.
     */
    public static StepDetailViewModel setupViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(StepDetailViewModel.class);
    }
}
