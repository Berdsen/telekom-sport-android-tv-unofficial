package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.VideoFragmentGlueHost;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.model.GameEventDetails;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.model.VideoDetails;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.SportsService;
import de.berdsen.telekomsport_unofficial.services.interfaces.VideoUrlResolvedHandler;
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
    private GameEventDetails gameEventDetails;
    private VideoDetails videoDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.gameEventDetails = sportsService.getGameEventDetails();
        this.videoDetails = sportsService.getSelectedVideo();

        if (this.videoDetails == null) {
            Toast.makeText(context, "No Selected Game Event", Toast.LENGTH_LONG).show();
            return;
        }

        initializeAndStartPlayer(gameEventDetails, videoDetails);
        // initializePlayer();
    }

    private VideoPlayerGlue mPlayerGlue;
    private SimpleExoPlayer mPlayer;
    private DefaultBandwidthMeter mBandwidthMeter;

    private void initializeAndStartPlayer(GameEventDetails gameEventDetails, VideoDetails videoDetails) {
        mBandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
        TrackSelector mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), mTrackSelector);
        mPlayerAdapter = new LeanbackPlayerAdapter(context, mPlayer, 16);

        mPlayerGlue = new VideoPlayerGlue(getActivity(), mPlayerAdapter, null);
        mPlayerGlue.setHost(new VideoFragmentGlueHost(this));

        play(gameEventDetails, videoDetails);

        // ArrayObjectAdapter mRowsAdapter = initializeRelatedVideosRow();
        // setAdapter(mRowsAdapter);
    }

    private void play(GameEventDetails gameEventDetails, VideoDetails videoDetails) {
        mPlayerGlue.setTitle(gameEventDetails.getMetadata().getWeb().getTitle());
        mPlayerGlue.setSubtitle(gameEventDetails.getMetadata().getWeb().getDescription());

        if (videoDetails != null) {
            prepareMediaForPlaying(Uri.parse(apiConstants.getVideoUrl(Integer.toString(videoDetails.getVideoID()))));
        } else {
            Toast.makeText(context, "Could not find playable content", Toast.LENGTH_LONG).show();
            getFragmentManager().popBackStack();
        }
    }

    private View getCurrentView() {
        return this.getView();
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

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                restService.retrieveVideoUrl(mediaSourceUri, new VideoUrlResolvedHandler() {
                    @Override
                    public void resolvedVideoUrl(String urlString) {
                        if (urlString == null || urlString.length() == 0) {
                            Toast.makeText(context, "Could not get VideoUrl", Toast.LENGTH_LONG).show();
                            getFragmentManager().popBackStack();
                            return;
                        }

                        HttpDataSource.Factory factory = buildHttpDataSourceFactory();

                        //final DashMediaSource dms = new DashMediaSource(Uri.parse(urlString), factory, new DefaultDashChunkSource.Factory(factory), null, null);
                        //final ExtractorMediaSource ems = new ExtractorMediaSource(Uri.parse(urlString), factory, new DefaultExtractorsFactory(), null, null);
                        final HlsMediaSource hms = new HlsMediaSource(Uri.parse(urlString), factory, null, null);

                        getCurrentView().post(new Runnable() {
                            @Override
                            public void run() {
                                mPlayer.prepare(hms);
                                mPlayerGlue.play();
                            }
                        });
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

    @Override
    public void onDetach() {
        super.onDetach();
        this.mPlayer.release();
    }

}
