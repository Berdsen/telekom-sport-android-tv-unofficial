package de.berdsen.telekomsport_unofficial.ui.sportsOverviewView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;

import java.util.List;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.interfaces.SportsResolvedHandler;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseVerticalGridFragment;
import de.berdsen.telekomsport_unofficial.ui.presenter.SportsCardPresenter;

/**
 * Created by berthm on 06.10.2017.
 */

public class SportsOverviewFragment extends AbstractBaseVerticalGridFragment {

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

        mAdapter = new ArrayObjectAdapter(new SportsCardPresenter(restService));
        setTitle("Select Sport");
        setAdapter(mAdapter);

        restService.retrieveSportsList(new SportsResolvedHandler() {
            @Override
            public void resolvedSports(List<Sport> sports) {
                loadedSports = sports;

                for (Sport s : loadedSports) {
                    mAdapter.add(s);
                }
            }
        });
    }
}
