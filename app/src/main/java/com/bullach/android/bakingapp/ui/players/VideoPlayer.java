package com.bullach.android.bakingapp.ui.players;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.bullach.android.bakingapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class VideoPlayer implements LifecycleObserver, Player.EventListener {

    private static final String TAG = VideoPlayer.class.getSimpleName();

    private final Context context;
    private final PlayerView mPlayerView;
    private VideoPlayerState mVideoPlayerState;

    private SimpleExoPlayer mPlayer;

    public VideoPlayer(Context context, PlayerView playerView, VideoPlayerState playerState) {
        this.context = context;
        this.mPlayerView = playerView;
        this.mVideoPlayerState = playerState;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        // Starting with API level 24 Android supports multiple windows. The app can be visible
        // but not active in split window mode, therefore we need to initialize the mPlayer in onStart().
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
                Log.d(TAG, "mPlayerView.onResume");
            }
            Log.d(TAG, "onStart() - Player is started.");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        // Starting with API level 24 onStop() is guaranteed to be called and in the paused mode
        // the activity is eventually still visible. Therefor we need to wait releasing until onStop().
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
            Log.d(TAG, "onStop() - Player is stopped.");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        // Before API level 24 there is no guarantee of onStop() being called.
        // We have to release the mPlayer as early as possible in onPause().
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            if (mPlayerView != null) {
                mPlayerView.onPause();
                Log.d(TAG, "mPlayerView.onPause");
            }
            releasePlayer();
            Log.d(TAG, "onPause() - Player is paused.");
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            updateResumePosition();
            mPlayer.release();
            mPlayer = null;
            Log.d(TAG, "Player is released.");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M || mPlayer == null) {
            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
            Log.d(TAG, "onResume() - Player is resumed.");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    /**
     * Initialize the ExoPlayer.
     */
    private void initializePlayer() {
        if (mPlayer == null) {
            // Initialize the mPlayer
            mPlayer = ExoPlayerFactory.newSimpleInstance(context,
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            // Bind the mPlayer to the view.
            mPlayerView.setPlayer(mPlayer);
            mPlayerView.requestFocus();

            // MediaSource representing the media to be played.
            Uri uri = Uri.parse(mVideoPlayerState.videoUrl);
            MediaSource mediaSource = buildMediaSource(uri);

            // Start playback when media is ready and has buffered enough.
            mPlayer.setPlayWhenReady(mVideoPlayerState.whenReady);
            boolean haveResumePosition = mVideoPlayerState.window != C.INDEX_UNSET;
            if (haveResumePosition) {
                mPlayer.seekTo(mVideoPlayerState.window, mVideoPlayerState.position);
            }

            mPlayer.prepare(mediaSource, !haveResumePosition, false);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getString(R.string.app_name)));
        return new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void updateResumePosition() {
        if (mPlayer != null) {
            mVideoPlayerState.whenReady = mPlayer.getPlayWhenReady();
            mVideoPlayerState.window = mPlayer.getCurrentWindowIndex();
            mVideoPlayerState.position = Math.max(0, mPlayer.getContentPosition());
            mVideoPlayerState.position = mPlayer.isCurrentWindowSeekable() ? Math.max(0, mPlayer.getCurrentPosition()) : C.TIME_UNSET;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d(TAG, "Test onPlayerStateChanged");
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }
}
