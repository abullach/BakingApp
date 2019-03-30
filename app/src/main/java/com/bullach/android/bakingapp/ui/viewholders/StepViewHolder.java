package com.bullach.android.bakingapp.ui.viewholders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bullach.android.bakingapp.data.models.Step;
import com.bullach.android.bakingapp.databinding.ItemStepBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StepViewHolder extends RecyclerView.ViewHolder {

    private final ItemStepBinding binding;

    public StepViewHolder(@NonNull ItemStepBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindTo(final Step step, final int position) {
        binding.setStep(step);
        binding.executePendingBindings();
    }

    public static StepViewHolder create(ViewGroup parent) {
        // Inflate
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Create the binding
        ItemStepBinding binding =
                ItemStepBinding.inflate(layoutInflater, parent, false);
        return new StepViewHolder(binding);
    }
}
