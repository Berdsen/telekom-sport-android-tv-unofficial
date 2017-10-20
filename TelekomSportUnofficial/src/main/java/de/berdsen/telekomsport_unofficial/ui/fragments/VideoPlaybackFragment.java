package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.GameEventDetails;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SportsService;
import de.berdsen.telekomsport_unofficial.services.interfaces.GameEventResolvedHandler;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseVideoFragment;

/**
 * Created by berthm on 19.10.2017.
 */

public class VideoPlaybackFragment extends AbstractBaseVideoFragment {

    @Inject
    RestService restService;

    @Inject
    SportsService sportsService;

    private LeanbackPlayerAdapter mPlayerAdapter;
    private GameEvent gameEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.gameEvent = sportsService.getSelectedGameEvent();
        if (this.gameEvent == null) {
            Toast.makeText(context, "No Selected Game Event", Toast.LENGTH_LONG).show();
            return;
        }

        restService.retrieveEventDetails(gameEvent, new GameEventResolvedHandler() {
            @Override
            public void onGameEventResolved(GameEvent event, GameEventDetails gameEventDetails) {

            }
        });

        initializePlayer();
    }

    private void initializePlayer() {
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
    }
}
