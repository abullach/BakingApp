package com.bullach.android.bakingapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bullach.android.bakingapp.R;
import com.bullach.android.bakingapp.data.RecipesDatabase;
import com.bullach.android.bakingapp.data.models.Recipe;
import com.bullach.android.bakingapp.data.models.Step;
import com.bullach.android.bakingapp.databinding.FragmentStepsListBinding;
import com.bullach.android.bakingapp.ui.activities.RecipeDetailsActivity;
import com.bullach.android.bakingapp.ui.adapters.StepsAdapter;
import com.bullach.android.bakingapp.ui.viewmodels.RecipeDetailViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class StepsListFragment extends Fragment {

    private RecipeDetailViewModel mViewModel;

    /** Member variable for the Recipe */
    private Recipe mRecipe;

    /** This field is used for data binding */
    private FragmentStepsListBinding mStepsListBinding;

    /** Member variable for the RecipesDatabase */
    private RecipesDatabase mRecipesDb;

    public StepsListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Instantiate FragmentMasterListIngredientsBinding using DataBindingUtil
        mStepsListBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_steps_list, container, false);
        View rootView = mStepsListBinding.getRoot();

        // Get the RecipeDatabase instance
        mRecipesDb = RecipesDatabase.getDatabase(this.getContext());

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = RecipeDetailsActivity.setupViewModel(getActivity());
        // Initialize a StepsAdapter
        setupStepsAdapter();
    }

    /**
     * Create a StepsAdapter and set it to the RecyclerView
     */
    private void setupStepsAdapter() {
        final StepsAdapter adapter = new StepsAdapter(mViewModel);
        mStepsListBinding.rvSteps.setAdapter(adapter);
        mStepsListBinding.rvSteps.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Use this setting to improve performance if you know that changes in content do not
        // change the child layout size in the RecyclerView
        mStepsListBinding.rvSteps.setHasFixedSize(true);

        // observe steps list
        mViewModel.getStepsList().observe(getViewLifecycleOwner(), new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                adapter.addAll(steps);
            }
        });
    }
}
