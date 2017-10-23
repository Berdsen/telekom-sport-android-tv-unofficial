package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.VideoFragmentGlueHost;
import android.support.v17.leanback.media.PlaybackGlue;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.List;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.model.BaseContent;
import de.berdsen.telekomsport_unofficial.model.ContentGroup;
import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.GameEventDetails;
import de.berdsen.telekomsport_unofficial.model.PlayerContent;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.model.VideoContent;
import de.berdsen.telekomsport_unofficial.model.VideoDetails;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SportsService;
import de.berdsen.telekomsport_unofficial.services.interfaces.GameEventResolvedHandler;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseVideoFragment;
import de.berdsen.telekomsport_unofficial.ui.listener.VideoPlayerGlue;
import de.berdsen.telekomsport_unofficial.utils.ApplicationConstants;

/**
 * Created by berthm on 19.10.2017.
 */

public class VideoPlaybackFragment extends AbstractBaseVideoFragment {

    @Inject
    RestService restService;

    @Inject
    SportsService sportsService;

    @Inject
    TelekomApiConstants apiConstants;

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
                initializeAndStartPlayer(gameEventDetails);
            }
        });

        // initializePlayer();
    }

    private VideoPlayerGlue mPlayerGlue;
    private SimpleExoPlayer mPlayer;

    private void initializeAndStartPlayer(GameEventDetails gameEventDetails) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), mTrackSelector);
        mPlayerAdapter = new LeanbackPlayerAdapter(context, mPlayer, 16);

        mPlayerGlue = new VideoPlayerGlue(getActivity(), mPlayerAdapter, null);
        mPlayerGlue.setHost(new VideoFragmentGlueHost(this));

        play(gameEventDetails);

        // ArrayObjectAdapter mRowsAdapter = initializeRelatedVideosRow();
        // setAdapter(mRowsAdapter);
    }

    private void play(GameEventDetails videoDetails) {
        mPlayerGlue.setTitle(videoDetails.getMetadata().getWeb().getTitle());
        mPlayerGlue.setSubtitle(videoDetails.getMetadata().getWeb().getDescription());


        PlayerContent videoContent = null;

        for (ContentGroup group : videoDetails.getContentGroups()) {

            for (BaseContent content : group.getContentEntries()) {
                if (!(content instanceof PlayerContent)) continue;

                videoContent = (PlayerContent)content;
                break;
            }

            if (videoContent != null) {
                break;
            }
        }

        if (videoContent != null) {
            List<VideoDetails> detailsList = videoContent.getVideoDetails();
            if (detailsList != null && detailsList.size() > 0) {
                prepareMediaForPlaying(Uri.parse(apiConstants.getVideoUrl(Integer.toString(detailsList.get(0).getVideoID()))));
                mPlayerGlue.play();
            }
        } else {
            Toast.makeText(context, "Could not find playable content", Toast.LENGTH_LONG).show();
        }

    }

    private void prepareMediaForPlaying(Uri mediaSourceUri) {
        String userAgent = ApplicationConstants.getUserAgentValue();
        MediaSource mediaSource =
                new ExtractorMediaSource(
                        mediaSourceUri,
                        new DefaultDataSourceFactory(getActivity(), userAgent),
                        new DefaultExtractorsFactory(),
                        null,
                        null);

        mPlayer.prepare(mediaSource);
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
