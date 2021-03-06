package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import java.util.List;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.AndroidApplication;
import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.model.EpgData;
import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.GameEventDetails;
import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.model.VideoDetails;
import de.berdsen.telekomsport_unofficial.services.PicassoCache;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.SportsService;
import de.berdsen.telekomsport_unofficial.services.interfaces.EpgResolvedHandler;
import de.berdsen.telekomsport_unofficial.services.interfaces.GameEventResolvedHandler;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseBrowseFragment;
import de.berdsen.telekomsport_unofficial.ui.presenter.EventCardItem;
import de.berdsen.telekomsport_unofficial.ui.presenter.EventCardPresenter;
import de.berdsen.telekomsport_unofficial.utils.ParseUtils;

/**
 * Created by Berdsen on 14.10.2017.
 */

public class SportsVideoViewFragment extends AbstractBaseBrowseFragment implements OnItemViewClickedListener {

    @Inject
    RestService restService;

    @Inject
    SessionService sessionService;

    @Inject
    PicassoCache picassoCache;

    @Inject
    SportsService sportsService;

    @Inject
    AndroidApplication androidApplication;

    public static final String SELECTED_SPORT_PARAMETER = "__SELECTED_SPORT__";

    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Sport givenSport = getArguments().getParcelable(SELECTED_SPORT_PARAMETER);

        if (givenSport == null) {
            return;
        }

        createLayout(givenSport);
    }

    private void createLayout(final Sport givenSport) {

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        setOnItemViewClickedListener(this);
        setTitle(givenSport.getTitle());
        setAdapter(mRowsAdapter);

        androidApplication.setLoading(true);
        restService.retrieveSportVideos(givenSport, new EpgResolvedHandler() {

            @Override
            public void epgDataResolved(Sport resolvedSport, List<EpgData> epgList) {
                androidApplication.setLoading(false);
                createListEntries(givenSport, epgList);
            }
        });
    }

    private void createListEntries(Sport givenSport, List<EpgData> epgList) {
        int headerItemId = 0;
        for (EpgData epgData : epgList) {
            if (epgData.getEvents() == null || epgData.getEvents().size() == 0) {
                continue;
            }

            ArrayObjectAdapter rowAdapter = new ArrayObjectAdapter(new EventCardPresenter(picassoCache));

            HeaderItem headerItem = new HeaderItem(headerItemId++, epgData.getTitle());

            for (GameEvent e : epgData.getEvents()) {
                rowAdapter.add(ParseUtils.createCardItem(e, givenSport.getBaseUrl()));
            }

            mRowsAdapter.add(new ListRow(headerItem, rowAdapter));
        }

    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof EventCardItem) {
            EventCardItem cardItem = (EventCardItem) item;

            GameEvent event = (GameEvent) cardItem.getItem();

            if (event == null) {
                //TODO: show error
                return;
            }

            androidApplication.setLoading(true);
            restService.retrieveEventDetails(event, decideNextView);

            /*
            if (event.getTargetPlayable() && false / * is_live * / ) {
                // go directly to play
                restService.retrieveEventDetails(event, showVideoPlaybackPage);
            } else {
                // go to details page
                restService.retrieveEventDetails(event, showEventDetailsPage);
            }
            */
        }
    }

    private GameEventResolvedHandler decideNextView = new GameEventResolvedHandler() {

        @Override
        public void onGameEventResolved(GameEvent event, GameEventDetails eventDetails) {

            androidApplication.setLoading(false);

            if (eventDetails == null) return;

            sportsService.setGameEvent(event, eventDetails);

            VideoDetails vd = eventDetails.getLiveContent();

            if (vd != null) {
                sportsService.setSelectedVideo(vd);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.sportsOverviewContainer, new VideoPlaybackFragment());
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            } else {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.sportsOverviewContainer, new SelectedVideoDetailsFragment());
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        }

    };
}
