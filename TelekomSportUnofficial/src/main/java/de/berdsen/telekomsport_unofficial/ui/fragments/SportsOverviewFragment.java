package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.support.v4.app.FragmentTransaction;
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

import de.berdsen.telekomsport_unofficial.AndroidApplication;
import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.services.PicassoCache;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.interfaces.SportsResolvedHandler;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseBrowseFragment;
import de.berdsen.telekomsport_unofficial.ui.presenter.DefaultCardItem;
import de.berdsen.telekomsport_unofficial.ui.presenter.DefaultCardPresenter;
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

    @Inject
    PicassoCache picassoCache;

    @Inject
    AndroidApplication androidApplication;

    private List<Sport> loadedSports;
    private List<Sport> loadedCompetitions;

    private ArrayObjectAdapter mRowsAdapter;
    private ArrayObjectAdapter mSportsRowAdapter;
    private ArrayObjectAdapter mCompetitionsRowAdapter;
    private ArrayObjectAdapter mSettingsRowAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesChanged);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        mSportsRowAdapter = new ArrayObjectAdapter(new DefaultCardPresenter(picassoCache));
        mCompetitionsRowAdapter = new ArrayObjectAdapter(new DefaultCardPresenter(picassoCache));
        mSettingsRowAdapter = new ArrayObjectAdapter(new DefaultCardPresenter(picassoCache));

        HeaderItem sportsHeader = new HeaderItem(0, "Sports");
        HeaderItem competitionsHeader = new HeaderItem(1, "Competitions");
        HeaderItem settingsHeader = new HeaderItem(2, "Settings");
        mRowsAdapter.add(new ListRow(sportsHeader, mSportsRowAdapter));
        mRowsAdapter.add(new ListRow(competitionsHeader, mCompetitionsRowAdapter));
        mRowsAdapter.add(new ListRow(settingsHeader, mSettingsRowAdapter));

        mSettingsRowAdapter.add(ParseUtils.createCardItem("Preferences", "Application settings", R.drawable.perm_group_system_tools));

        setTitle(getString(R.string.app_name));
        setAdapter(mRowsAdapter);
        setHeadersState(HEADERS_DISABLED);
        setOnItemViewClickedListener(this);

        this.androidApplication.setLoading(true);

        restService.retrieveSportsList(new SportsResolvedHandler() {
            @Override
            public void resolvedSports(List<Sport> sports, List<Sport> competitions) {
                androidApplication.setLoading(false);
                loadedSports = sports;
                loadedCompetitions = competitions;

                for (Sport s : loadedSports) {
                    mSportsRowAdapter.add(ParseUtils.createCardItem(s));
                }

                for (Sport s : loadedCompetitions) {
                    mCompetitionsRowAdapter.add(ParseUtils.createCardItem(s));
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferencesChanged);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            androidApplication.doLogin(sessionService, context);
        }
    };

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
