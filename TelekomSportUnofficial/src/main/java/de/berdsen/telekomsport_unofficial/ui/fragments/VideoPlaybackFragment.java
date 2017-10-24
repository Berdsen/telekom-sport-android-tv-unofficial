package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.VideoFragmentGlueHost;
import android.support.v17.leanback.media.PlaybackGlue;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
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
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.SportsService;
import de.berdsen.telekomsport_unofficial.services.interfaces.GameEventResolvedHandler;
import de.berdsen.telekomsport_unofficial.services.model.CookieJarImpl;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseVideoFragment;
import de.berdsen.telekomsport_unofficial.ui.listener.VideoPlayerGlue;
import de.berdsen.telekomsport_unofficial.utils.ApplicationConstants;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by berthm on 19.10.2017.
 */

public class VideoPlaybackFragment extends AbstractBaseVideoFragment {

    private final static String TAG = "VideoPlaybackFragment";

    @Inject
    RestService restService;

    @Inject
    SessionService sessionService;

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
    private DefaultBandwidthMeter mBandwidthMeter;

    private void initializeAndStartPlayer(GameEventDetails gameEventDetails) {
        mBandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
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
            }
        } else {
            Toast.makeText(context, "Could not find playable content", Toast.LENGTH_LONG).show();
        }

    }

    private void prepareMediaForPlaying(final Uri mediaSourceUri) {
        /*
        OkHttpDataSourceFactory ds = new OkHttpDataSourceFactory(client, userAgent, new TransferListener<DataSource>() {
            @Override
            public void onTransferStart(DataSource source, DataSpec dataSpec) {

            }

            @Override
            public void onBytesTransferred(DataSource source, int bytesTransferred) {

            }

            @Override
            public void onTransferEnd(DataSource source) {

            }
        }, null);

        MediaSource mediaSource = new ExtractorMediaSource(mediaSourceUri, new OkHttpDataSourceFactory(client, userAgent, null, null), null, null, null);
        */

        final View currentView = this.getView();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String videoUrl = restService.retrieveVideoUrl(mediaSourceUri);
                HttpDataSource.Factory factory = buildHttpDataSourceFactory();
                final DashMediaSource dms = new DashMediaSource(Uri.parse(videoUrl), factory, new DefaultDashChunkSource.Factory(factory), null, null);
                final ExtractorMediaSource ems = new ExtractorMediaSource(Uri.parse(videoUrl), factory, new DefaultExtractorsFactory(), null, null);
                final HlsMediaSource hms = new HlsMediaSource(Uri.parse(videoUrl), factory, null, null);

                currentView.post(new Runnable() {
                    @Override
                    public void run() {
                        mPlayer.prepare(hms);
                        mPlayerGlue.play();
                    }
                });
            }
        });

        t.start();


        return;

        /*
        MediaSource mediaSource =
                new ExtractorMediaSource(
                        mediaSourceUri,
                        ds,
                        new DefaultExtractorsFactory(),
                        null,
                        null);
        */
        //mPlayer.prepare(mediaSource);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory() {
        String userAgent = ApplicationConstants.getUserAgentValue();
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl())
                .build();

        if (sessionService.isAuthenticated()) {
            List<HttpCookie> cookies = sessionService.getAuthCookies();
            List<Cookie> listOfCookies = new ArrayList<>();

            for (HttpCookie c : cookies) {
                Cookie parsedCookie = Cookie.parse(HttpUrl.parse(apiConstants.getBaseUrl()), c.toString());
                listOfCookies.add(parsedCookie);
            }
            client.cookieJar().saveFromResponse(HttpUrl.parse(apiConstants.getBaseUrl()), listOfCookies);
        }

        return new OkHttpDataSourceFactory(client, userAgent, mBandwidthMeter);
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
