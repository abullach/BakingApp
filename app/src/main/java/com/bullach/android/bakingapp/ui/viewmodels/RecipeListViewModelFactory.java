package com.bullach.android.bakingapp.ui.viewmodels;

import com.bullach.android.bakingapp.data.RecipeRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link RecipeRepository}
 */
public class RecipeListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RecipeRepository mRepository;

    public RecipeListViewModelFactory(RecipeRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeListViewModel(mRepository);
    }
}



