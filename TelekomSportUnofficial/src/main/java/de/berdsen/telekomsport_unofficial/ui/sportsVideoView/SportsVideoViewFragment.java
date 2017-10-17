package de.berdsen.telekomsport_unofficial.ui.sportsVideoView;

import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;

import java.util.List;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.model.EpgData;
import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.services.ImageCacheService;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.interfaces.EpgResolvedHandler;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseBrowseFragment;
import de.berdsen.telekomsport_unofficial.ui.presenter.DefaultCardPresenter;
import de.berdsen.telekomsport_unofficial.ui.presenter.EventCardPresenter;
import de.berdsen.telekomsport_unofficial.utils.ParseUtils;

/**
 * Created by Berdsen on 14.10.2017.
 */

public class SportsVideoViewFragment extends AbstractBaseBrowseFragment {
    @Inject
    RestService restService;

    @Inject
    SessionService sessionService;

    @Inject
    ImageCacheService imageCacheService;

    public static final String SELECTED_SPORT_PARAMETER = "__SELECTED_SPORT__";

    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Sport givenSport = getArguments().getParcelable(SELECTED_SPORT_PARAMETER);

        if (givenSport == null) {
            return;
        }

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        DefaultCardPresenter dcp = new DefaultCardPresenter();

        setTitle(givenSport.getTitle());
        setAdapter(mRowsAdapter);

        restService.retrieveSportVideos(givenSport, new EpgResolvedHandler() {

            @Override
            public void epgDataResolved(Sport resolvedSport, List<EpgData> epgList) {
                int headerItemId = 0;
                for (EpgData epgData : epgList) {
                    if (epgData.getEvents() == null || epgData.getEvents().size() == 0) {
                        continue;
                    }

                    ArrayObjectAdapter rowAdapter = new ArrayObjectAdapter(new EventCardPresenter());

                    HeaderItem headerItem = new HeaderItem(headerItemId++, epgData.getTitle());

                    for (GameEvent e : epgData.getEvents()) {
                        rowAdapter.add(ParseUtils.createCardItem(e, givenSport.getBaseUrl(), imageCacheService));
                    }

                    mRowsAdapter.add(new ListRow(headerItem, rowAdapter));
                }
            }

        });
    }
}
