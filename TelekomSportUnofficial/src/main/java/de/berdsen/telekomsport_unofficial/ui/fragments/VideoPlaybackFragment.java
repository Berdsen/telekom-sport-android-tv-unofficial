package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.app.VideoFragmentGlueHost;
import android.support.v17.leanback.app.VideoSupportFragmentGlueHost;
import android.text.Html;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.demo.TrackSelectionHelper;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.AndroidApplication;
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
 * Created by Berdsen on 19.10.2017.
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

    @Inject
    AndroidApplication androidApplication;

    private LeanbackPlayerAdapter mPlayerAdapter;
    private GameEventDetails gameEventDetails;
    private VideoDetails videoDetails;

    private VideoPlayerGlue mPlayerGlue;
    private SimpleExoPlayer mPlayer;
    private DefaultBandwidthMeter mBandwidthMeter;

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
    }

    private void initializeAndStartPlayer(GameEventDetails gameEventDetails, VideoDetails videoDetails) {
        mBandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidthMeter);

        final DefaultTrackSelector mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        DefaultTrackSelector.Parameters parameters = mTrackSelector.getParameters();
        parameters.withAllowMixedMimeAdaptiveness(true);
        parameters.withAllowNonSeamlessAdaptiveness(true);
        //parameters.withExceedRendererCapabilitiesIfNecessary(true);
        //parameters.withExceedVideoConstraintsIfNecessary(true);
        mTrackSelector.setParameters(parameters);

        final TrackSelectionHelper trackSelectionHelper = new TrackSelectionHelper(mTrackSelector, videoTrackSelectionFactory);

        mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), mTrackSelector);
        mPlayerAdapter = new LeanbackPlayerAdapter(context, mPlayer, 16);

        mPlayerGlue = new VideoPlayerGlue(getActivity(), mPlayerAdapter, new VideoPlayerGlue.OnActionClickedListener() {

            @Override
            public void onQualityChanged() {
                trackSelectionHelper.showSelectionDialog(getActivity(), "QualityChooser", mTrackSelector.getCurrentMappedTrackInfo(), 0);
            }
        });

        mPlayerGlue.setHost(new VideoSupportFragmentGlueHost(this));

        play(gameEventDetails, videoDetails);
    }

    private void play(GameEventDetails gameEventDetails, VideoDetails videoDetails) {
        mPlayerGlue.setTitle(gameEventDetails.getMetadata().getWeb().getTitle());
        mPlayerGlue.setSubtitle(Html.fromHtml(gameEventDetails.getMetadata().getWeb().getDescription()));

        if (videoDetails != null) {
            prepareMediaForPlaying(Uri.parse(apiConstants.getVideoUrl(Integer.toString(videoDetails.getVideoID()))));
        } else {
            Toast.makeText(context, "Could not find playable content", Toast.LENGTH_LONG).show();
            getFragmentManager().popBackStack();
        }
    }

    private void prepareMediaForPlaying(final Uri mediaSourceUri) {
        androidApplication.setLoading(true);
        restService.retrieveVideoUrl(mediaSourceUri, new VideoUrlResolvedHandler() {
            @Override
            public void resolvedVideoUrl(String urlString, String errorMessage) {

                androidApplication.setLoading(false);
                if (urlString == null || urlString.length() == 0) {
                    Toast.makeText(context, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    getFragmentManager().popBackStack();
                    return;
                }

                final MediaSource mediaSource = buildMediaSource(Uri.parse(urlString));

                mPlayer.prepare(mediaSource);
                mPlayerGlue.play();
            }
        });
    }

    private MediaSource buildMediaSource(Uri uri) {
        int type = Util.inferContentType(uri.getLastPathSegment());
        HttpDataSource.Factory factory = buildHttpDataSourceFactory();

        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(
                        uri,
                        factory,
                        new DefaultSsChunkSource.Factory(factory),
                        null,
                        null);
            case C.TYPE_DASH:
                return new DashMediaSource(
                        uri,
                        factory,
                        new DefaultDashChunkSource.Factory(factory),
                        null,
                        null);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, factory, null, null);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(
                        uri, factory, new DefaultExtractorsFactory(), null, null);
            default:
            {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
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

        //TODO: check both datasource types and default bandwith
        //return new OkHttpDataSourceFactory(client, userAgent, mBandwidthMeter);
        //return new DefaultHttpDataSourceFactory(userAgent, mBandwidthMeter);

        return new OkHttpDataSourceFactory(client, userAgent, mBandwidthMeter);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        this.mPlayer.stop();
        this.mPlayer.release();
    }
}
