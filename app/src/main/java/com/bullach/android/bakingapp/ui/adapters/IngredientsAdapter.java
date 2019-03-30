package com.bullach.android.bakingapp.ui.adapters;

import android.view.ViewGroup;

import com.bullach.android.bakingapp.data.models.Ingredient;
import com.bullach.android.bakingapp.ui.viewholders.IngredientViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Member variable for list of ingredients */
    private List<Ingredient> mIngredientList;
    /** The selected recipe name */
    private String mRecipeName;

    public IngredientsAdapter(List<Ingredient> ingredients, String recipeName) {
        mIngredientList = ingredients;
        mRecipeName = recipeName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return IngredientViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Ingredient ingredient = mIngredientList.get(position);
        ((IngredientViewHolder) holder).bindTo(ingredient);
    }

    @Override
    public int getItemCount() {
        return mIngredientList != null ? mIngredientList.size() : 0;
    }

    public void addAll(List<Ingredient> ingredients) {
        mIngredientList.clear();
        mIngredientList.addAll(ingredients);
        notifyDataSetChanged();
    }
}
