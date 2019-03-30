package com.bullach.android.bakingapp.ui.adapters;

import android.view.ViewGroup;

import com.bullach.android.bakingapp.data.models.Recipe;
import com.bullach.android.bakingapp.ui.viewholders.RecipeViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipe> mRecipesList;

    public RecipesAdapter(List<Recipe> recipesList) {
        this.mRecipesList = recipesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecipeViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Recipe recipe = mRecipesList.get(position);
        ((RecipeViewHolder) holder).bindTo(recipe, position);
    }

    @Override
    public int getItemCount() {
        return mRecipesList != null ? mRecipesList.size() : 0;
    }

    /**
     * This method is to add or update the list of {@link Recipe}s.
     * <br>
     * @param recipeList The recipe list is the data source of the adapter.
     */
    public void addAll(List<Recipe> recipeList) {
        mRecipesList.clear();
        mRecipesList.addAll(recipeList);
        notifyDataSetChanged();
    }
}
