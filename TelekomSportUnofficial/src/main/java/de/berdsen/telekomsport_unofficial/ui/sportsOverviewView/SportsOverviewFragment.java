package de.berdsen.telekomsport_unofficial.ui.sportsOverviewView;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.interfaces.SportsResolvedHandler;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseBrowseFragment;
import de.berdsen.telekomsport_unofficial.ui.presenter.DefaultCardItem;
import de.berdsen.telekomsport_unofficial.ui.presenter.DefaultCardPresenter;
import de.berdsen.telekomsport_unofficial.ui.settingsView.SettingsFragment;
import de.berdsen.telekomsport_unofficial.ui.sportsVideoView.SportsVideoViewFragment;
import de.berdsen.telekomsport_unofficial.utils.ParseUtils;

/**
 * Created by berthm on 06.10.2017.
 */

public class SportsOverviewFragment extends AbstractBaseBrowseFragment implements OnItemViewClickedListener {

    @Inject
    RestService restService;

    @Inject
    SessionService sessionService;

    @Inject
    SharedPreferences sharedPreferences;

    private List<Sport> loadedSports;

    private ArrayObjectAdapter mRowsAdapter;
    private ArrayObjectAdapter mSportsRowAdapter;
    private ArrayObjectAdapter mSettingsRowAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        mSportsRowAdapter = new ArrayObjectAdapter(new DefaultCardPresenter());
        mSettingsRowAdapter = new ArrayObjectAdapter(new DefaultCardPresenter());

        HeaderItem sportsHeader = new HeaderItem(0, "Sports");
        HeaderItem settingsHeader = new HeaderItem(1, "Settings");
        mRowsAdapter.add(new ListRow(sportsHeader, mSportsRowAdapter));
        mRowsAdapter.add(new ListRow(settingsHeader, mSettingsRowAdapter));

        mSettingsRowAdapter.add(ParseUtils.createDefaultCardItem("Preferences", "Application settings", R.drawable.perm_group_system_tools));

        setTitle("Select Sport");
        setAdapter(mRowsAdapter);
        setHeadersState(HEADERS_DISABLED);
        setOnItemViewClickedListener(this);

        restService.retrieveSportsList(new SportsResolvedHandler() {
            @Override
            public void resolvedSports(List<Sport> sports) {
                loadedSports = sports;

                for (Sport s : loadedSports) {
                    mSportsRowAdapter.add(ParseUtils.createDefaultCardItem(s));
                }
            }
        });

    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof DefaultCardItem ) {
            DefaultCardItem cardItem = (DefaultCardItem) item;
            if (cardItem.getItem() == null) {

                //Intent i = new Intent(this.getActivity(), SettingsActivity.class);
                //startActivity(i);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.sportsOverviewContainer, new SettingsFragment());
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            } else {
                if (!(cardItem.getItem() instanceof Sport)) {
                    Toast.makeText(context, "No sports entry selected", Toast.LENGTH_LONG).show();
                    return;
                }

                Sport sport = (Sport) cardItem.getItem();
                SportsVideoViewFragment newFragment = new SportsVideoViewFragment();
                Bundle args = new Bundle();
                args.putParcelable(SportsVideoViewFragment.SELECTED_SPORT_PARAMETER, sport);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.sportsOverviewContainer, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        }
    }

}
