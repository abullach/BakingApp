package com.bullach.android.bakingapp.ui.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.bullach.android.bakingapp.data.models.Step;
import com.bullach.android.bakingapp.ui.viewholders.StepViewHolder;
import com.bullach.android.bakingapp.ui.viewmodels.RecipeDetailViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Member variable for the list of {@link Step}s */
    private List<Step> mStepsList;

    private RecipeDetailViewModel viewModel;

    public StepsAdapter(RecipeDetailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return StepViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Step step = mStepsList.get(position);
        ((StepViewHolder) holder).bindTo(step, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCurrentStep(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStepsList != null ? mStepsList.size() : 0;
    }

    /**
     * This method is to add a list of {@link Step}s
     *
     * @param stepList The step list is the data source of the adapter
     */
    public void addAll(List<Step> stepList) {
        mStepsList = stepList;
        notifyDataSetChanged();
    }
}
