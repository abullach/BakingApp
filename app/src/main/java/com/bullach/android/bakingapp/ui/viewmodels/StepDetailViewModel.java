package com.bullach.android.bakingapp.ui.viewmodels;

import com.bullach.android.bakingapp.data.models.Step;
import com.bullach.android.bakingapp.utilities.SingleLiveEvent;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class StepDetailViewModel extends ViewModel {

    private List<Step> stepsList;
    private int currentPosition;
    private final SingleLiveEvent<Step> navigateToStepDetail = new SingleLiveEvent<>();

    public void init(List<Step> steps, int position) {
        stepsList = steps;
        setCurrentPosition(position);
    }

    public void setCurrentPosition(int position) {
        currentPosition = position;
        navigateToCurrentStep();
    }

    public void nextStep() {
        currentPosition++;
        navigateToCurrentStep();
    }

    public void previousStep() {
        currentPosition--;
        navigateToCurrentStep();
    }

    public boolean hasNext() {
        return currentPosition < stepsList.size() - 1;
    }

    public boolean hasPrevious() {
        return currentPosition > 0;
    }

    private void navigateToCurrentStep() {
        navigateToStepDetail.setValue(stepsList.get(currentPosition));
    }

    public SingleLiveEvent<Step> getNavigateToStepDetail() {
        return navigateToStepDetail;
    }

    public int getNumberOfSteps() {
        return stepsList.size();
    }
}
