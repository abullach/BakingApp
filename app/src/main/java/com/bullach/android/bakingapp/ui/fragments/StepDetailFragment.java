package com.bullach.android.bakingapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bullach.android.bakingapp.R;
import com.bullach.android.bakingapp.data.models.Step;
import com.bullach.android.bakingapp.databinding.FragmentStepDetailBinding;
import com.bullach.android.bakingapp.ui.activities.StepDetailActivity;
import com.bullach.android.bakingapp.ui.players.VideoPlayer;
import com.bullach.android.bakingapp.ui.players.VideoPlayerState;
import com.bullach.android.bakingapp.ui.viewmodels.StepDetailViewModel;
import com.bullach.android.bakingapp.utilities.Constants;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class StepDetailFragment extends Fragment implements Player.EventListener {

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    /** Member variable for Step that this fragment displays */
    private Step mStep;
    private int mTotalSteps;

    private boolean isTablet;
    private boolean isLandscape;

    private StepDetailViewModel mViewModel;

    /** This field is used for data binding */
    private FragmentStepDetailBinding mStepDetailBinding;

    private PlayerView mVideoPlayerView;
    private VideoPlayerState mVideoPlayerState = new VideoPlayerState();

    public StepDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of StepDetailFragment that will be initialized
     * with the given arguments.
     */
    public static StepDetailFragment newInstance(Step step, int numOfSteps) {
        StepDetailFragment fragment = new StepDetailFragment();

        Bundle bundle = new Bundle();
        // Current steps passed in from the activities
        bundle.putParcelable(Constants.STEP_DATA, step);
        // Total number of steps
        bundle.putInt(Constants.STEP_TOTAL, numOfSteps);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Instantiate FragmentStepDetailBinding using DataBindingUtil
        mStepDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_detail, container, false);

        // Return the rootView
        return mStepDetailBinding.getRoot();
    }

    /**
     * During creation, if arguments have been supplied to the fragment
     * then parse those out.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = StepDetailActivity.setupViewModel(getActivity());

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore step data value from saved state
            mStep = savedInstanceState.getParcelable(Constants.STEP_DATA);
            mVideoPlayerState = savedInstanceState.getParcelable(Constants.PLAYER_STATE);
            mTotalSteps = savedInstanceState.getInt(Constants.STEP_TOTAL);
        } else {
            // Initialize mStep member with default step data value for the new instance
            if(getArguments() != null) {
                mStep = getArguments().getParcelable(Constants.STEP_DATA);
                mTotalSteps = getArguments().getInt(Constants.STEP_TOTAL, 0);
                // Player state management: clear the start position
                mVideoPlayerState.window = C.INDEX_UNSET;
                mVideoPlayerState.position = C.TIME_UNSET;
                mVideoPlayerState.whenReady = true;
            }
        }

        if(mStep != null) {
            // Set the header title with number of steps.
            if(mStep.getId() >=1 ) {
                String headerTitle = String.format(getResources().getString(R.string.steps_detail_header_title), mStep.getId(), (mTotalSteps -1));
                mStepDetailBinding.tvStepOf.setText(headerTitle);
            } else {
                // For step 0 use the short description as header title.
                mStepDetailBinding.tvStepOf.setText(mStep.getShortDescription());
            }

            // Sanitize the description text
            String sanitizedDescription = sanitizeDescription(mStep.getDescription());
            // Set the current step description.
            mStepDetailBinding.tvDescription.setText(sanitizedDescription);
        } else {
            Log.d(TAG, "mStep is null");
        }

        // Check if it's a tablet
        isTablet = getResources().getBoolean(R.bool.isTablet);
        // Check if it's landscape orientation
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        // Don't show navigation buttons for tablet devices in landscape orientation (with two pane layout).
        if(isTablet && isLandscape) {
            Log.d(TAG, "Device: Tablet and Orientation: Landscape");
            mStepDetailBinding.buttonPrev.setVisibility(View.GONE);
            mStepDetailBinding.buttonNext.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "Device: " +(isTablet ? "Tablet" : "Phone") +" and Orientation: " +(isLandscape ? "Landscape" : "Portrait"));

            hideNavigationButtonAtBothEnds();

            // Handle click events on next step button
            mStepDetailBinding.buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.nextStep();
                }
            });

            // Handle click events on previous step button
            mStepDetailBinding.buttonPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.previousStep();
                }
            });
        }

        // If step has a video url
        if(!mStep.getVideoURL().isEmpty()) {
            // Initialize and show the video player.
            mVideoPlayerView = mStepDetailBinding.playerView;
            mVideoPlayerState.videoUrl = mStep.getVideoURL();
            // Add LifecycleObserver to the VideoPlayer.
            getLifecycle().addObserver(
                    new VideoPlayer(getActivity(), mVideoPlayerView, mVideoPlayerState));

            if (!isTablet && isLandscape) {
                mStepDetailBinding.tvStepOf.setVisibility(View.GONE);
                // step details
                ConstraintSet constraintSet = new ConstraintSet();
                ConstraintLayout layout = mStepDetailBinding.clContent;
                constraintSet.clone(layout);
                constraintSet.clear(R.id.flPlayer, ConstraintSet.TOP);
                constraintSet.connect(R.id.flPlayer, ConstraintSet.TOP, R.id.clContent, ConstraintSet.TOP);

                constraintSet.clear(R.id.buttonPrev, ConstraintSet.TOP);
                constraintSet.clear(R.id.buttonPrev, ConstraintSet.BOTTOM);
                constraintSet.connect(R.id.buttonPrev, ConstraintSet.TOP, R.id.tvDescription, ConstraintSet.BOTTOM);

                constraintSet.clear(R.id.buttonNext, ConstraintSet.TOP);
                constraintSet.clear(R.id.buttonNext, ConstraintSet.BOTTOM);
                constraintSet.connect(R.id.buttonNext, ConstraintSet.TOP, R.id.tvDescription, ConstraintSet.BOTTOM);

                constraintSet.applyTo(layout);
                // Hide toolbar to allow fullscreen
                if(getActivity() != null) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                }
                // Expand to fullscreen view
                mVideoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                mVideoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                mVideoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

                hideSystemUi();
            }

            hideView(mStepDetailBinding.flPlayer, false);
        } else if(!mStep.getThumbnailURL().isEmpty()) {
            // check thumbnail url and load
            Picasso.with(getContext())
                    .load(mStep.getThumbnailURL())
                    .error(R.drawable.icons8_cookies_48)
                    .placeholder(R.drawable.icons8_cookies_48)
                    .into(mStepDetailBinding.ivEmpty);
        } else {
            hideView(mStepDetailBinding.flPlayer, true);
        }
    }

    /**
     * Replaces question mark "�" with the degree "°" in given string.
     * <br>
     * Some descriptions (e.g. Brownies recipe step 1) contain a question mark instead of the degree sign.
     * (Note: Normally the API should sanitize the data).
     */
    private String sanitizeDescription(String description) {
        if (description.contains(getString(R.string.question_mark))) {
            description = description.replace(getString(R.string.question_mark), getString(R.string.degree));
        }
        return description;
    }

    /**
     * Setter method for displaying current step
     */
    public void setStep(Step step) {
        mStep = step;
    }

    /**
     * Hide a particular view.
     * <br>
     * @param view the View which to hide.
     * @param hide whether to hide (<code>true</code>) the view or not (<code>false</code>).
     */
    private void hideView(View view, boolean hide) {
        if (hide) {
            view.setVisibility(View.GONE); // Do not use INVISIBLE
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hide Previous / Next button at the first / last recipe step.
     */
    private void hideNavigationButtonAtBothEnds() {
        // Hide the next step button if there is no next step.
        if (mViewModel.hasNext()) {
            hideView(mStepDetailBinding.buttonNext, false);
        } else {
            hideView(mStepDetailBinding.buttonNext, true);
        }

        // Hide the previous step button if there is no previous step.
        if (mViewModel.hasPrevious()) {
            hideView(mStepDetailBinding.buttonPrev, false);
        } else {
            hideView(mStepDetailBinding.buttonPrev, true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        // Save the current step state
        savedInstanceState.putParcelable(Constants.STEP_DATA, mStep);
        // Save the current video player state
        savedInstanceState.putParcelable(Constants.PLAYER_STATE, mVideoPlayerState);
        // Save the total number of steps
        savedInstanceState.putInt(Constants.STEP_TOTAL, mTotalSteps);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Enables the user to have a pure full-screen experience in a single-pane landscape mode.
     */
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
            int flagFullScreen = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            // Enable full-screen mode on PlayerView, empty ImageView
            mStepDetailBinding.playerView.setSystemUiVisibility(flagFullScreen);
            mStepDetailBinding.ivEmpty.setSystemUiVisibility(flagFullScreen);
        }
    }
}
