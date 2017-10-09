package de.berdsen.telekomsport_unofficial.ui.sportsOverviewView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import java.util.List;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.interfaces.SportsResolvedHandler;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseVerticalGridFragment;
import de.berdsen.telekomsport_unofficial.ui.preferenceView.PreferenceActivity;
import de.berdsen.telekomsport_unofficial.ui.presenter.DefaultCardItem;
import de.berdsen.telekomsport_unofficial.ui.presenter.DefaultCardPresenter;
import de.berdsen.telekomsport_unofficial.utils.ParseUtils;

/**
 * Created by berthm on 06.10.2017.
 */

public class SportsOverviewFragment extends AbstractBaseVerticalGridFragment implements OnItemViewClickedListener {

    @Inject
    RestService restService;

    @Inject
    SessionService sessionService;

    @Inject
    SharedPreferences sharedPreferences;

    private List<Sport> loadedSports;

    private ArrayObjectAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayObjectAdapter(new DefaultCardPresenter());

        setTitle("Select Sport");
        setAdapter(mAdapter);
        setOnItemViewClickedListener(this);

        restService.retrieveSportsList(new SportsResolvedHandler() {
            @Override
            public void resolvedSports(List<Sport> sports) {
                loadedSports = sports;

                for (Sport s : loadedSports) {
                    mAdapter.add(ParseUtils.createDefaultCardItem(s, restService));
                }

                mAdapter.add(ParseUtils.createDefaultCardItem("Preferences", "Application settings", android.support.v17.leanback.R.drawable.lb_search_orb));
            }
        });
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof DefaultCardItem ) {
            DefaultCardItem cardItem = (DefaultCardItem) item;
            if (cardItem.getItem() == null) {

                // this should be the preference item

                Intent i = new Intent(this.getActivity(), PreferenceActivity.class);
                startActivity(i);

            } else {

                //TODO: start selected sports browse activity
            }
        }
    }
}
