package com.bullach.android.bakingapp.ui.viewmodels;

import com.bullach.android.bakingapp.data.RecipeRepository;
import com.bullach.android.bakingapp.data.models.Ingredient;
import com.bullach.android.bakingapp.data.models.Recipe;
import com.bullach.android.bakingapp.data.models.Step;
import com.bullach.android.bakingapp.utilities.SingleLiveEvent;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecipeDetailViewModel extends ViewModel {
    private MutableLiveData<Recipe> recipeLiveData = new MutableLiveData<>();

    private MutableLiveData<List<Ingredient>> ingredientsLiveData = new MutableLiveData<>();

    private MutableLiveData<List<Step>> stepsList = new MutableLiveData<>();

    private MutableLiveData<Step> currentStep = new MutableLiveData<>();

    private final SingleLiveEvent<Integer> stepClickEvent = new SingleLiveEvent<>();

    private RecipeRepository repository;

    public RecipeDetailViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Recipe recipe, boolean twoPane) {

        repository.saveRecipe(recipe);

        setIngredients(recipe.getIngredients());
        setSteps(recipe.getSteps());
        if (twoPane) {
            setCurrentStep(0);
        }
    }

    public void setCurrentStep(int position) {
        currentStep.setValue(stepsList.getValue().get(position));
        stepClickEvent.setValue(position);
    }

    public LiveData<Step> getCurrentStep() {
        return currentStep;
    }

    private void setSteps(List<Step> steps) {
        stepsList.setValue(steps);
    }

    private void setIngredients(List<Ingredient> ingredients) {
        ingredientsLiveData.setValue(ingredients);
    }

    public LiveData<List<Step>> getStepsList() {
        return stepsList;
    }

    public LiveData<List<Ingredient>> getIngredientsLiveData() {
        return ingredientsLiveData;
    }

    private void setRecipeLiveData(Recipe recipe) {
        recipeLiveData.setValue(recipe);
    }

    public LiveData<Recipe> getRecipeLiveData() {
        return recipeLiveData;
    }

    public SingleLiveEvent<Integer> getStepClickEvent() {
        return stepClickEvent;
    }
}
