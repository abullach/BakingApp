package com.bullach.android.bakingapp.ui.viewholders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bullach.android.bakingapp.R;
import com.bullach.android.bakingapp.data.models.Recipe;
import com.bullach.android.bakingapp.databinding.ItemRecipeBinding;
import com.bullach.android.bakingapp.ui.activities.RecipeDetailsActivity;
import com.bullach.android.bakingapp.utilities.Constants;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private final ItemRecipeBinding binding;

    public RecipeViewHolder(@NonNull ItemRecipeBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindTo(final Recipe recipe, int position) {
        binding.setRecipe(recipe);
        // Set the recipe name
        binding.textRecipeName.setText(recipe.getName());
        // Set the recipe image
        if (recipe.getImage().isEmpty()) {
            // If the image URL is empty use an image from the drawable folder instead
            binding.ivRecipeImage.setImageResource(R.drawable.icons8_cookies_48);
        } else {
            // If the image URL exists, use the Picasso library to load the image
            Picasso.with(binding.getRoot().getContext())
                    .load(recipe.getImage())
                    .error(R.drawable.icons8_cookies_48)
                    .placeholder(R.drawable.icons8_cookies_48)
                    .into(binding.ivRecipeImage);
        }

        // Set a onClickListener to handle click events on recipes in the list
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Wrap the parcelable into a bundle
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.RECIPE_EXTRA, recipe);
                // Create the Intent which will start the RecipeDetailsActivity
                Intent intent = new Intent(view.getContext(), RecipeDetailsActivity.class);
                // Pass the bundle through to the Intent
                intent.putExtra(Constants.RECIPE_EXTRA, bundle);
                // Once the Intent has been created, start the RecipeDetailsActivity
                view.getContext().startActivity(intent);
            }
        });

        binding.executePendingBindings();
    }

    public static RecipeViewHolder create(ViewGroup parent) {
        // Inflate the layout
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Create the binding
        ItemRecipeBinding binding =
                ItemRecipeBinding.inflate(layoutInflater, parent, false);
        return new RecipeViewHolder(binding);
    }
}


