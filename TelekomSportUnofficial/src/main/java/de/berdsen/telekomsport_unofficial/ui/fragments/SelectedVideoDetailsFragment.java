package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.GameEventDetails;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SportsService;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseDetailsFragment;
import de.berdsen.telekomsport_unofficial.ui.presenter.DetailsDescriptionPresenter;

/**
 * Created by berthm on 19.10.2017.
 */

public class SelectedVideoDetailsFragment extends AbstractBaseDetailsFragment implements OnItemViewClickedListener, OnActionClickedListener {

    private static final String TAG = "SelectedVideoDetailsFr";

    private static final int GAMEREPORT = 100;
    private static final int REPLAY = 101;

    @Inject
    RestService restService;

    @Inject
    SportsService sportsService;

    private GameEvent selectedGameEvent;
    private GameEventDetails selectedGameEventDetails;
    private DetailsOverviewRow mRow;
    private ArrayList<Object> listOfActions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.selectedGameEvent = sportsService.getGameEvent();
        this.selectedGameEventDetails = sportsService.getGameEventDetails();

        if (this.selectedGameEvent == null || this.selectedGameEventDetails == null) {
            Log.e(TAG, "onCreate: No GameEvent or GameEventDetails");
            return;
        }

        createLayout();

        setOnItemViewClickedListener( this );
    }

    private void createLayout() {
        mRow = new DetailsOverviewRow(this.selectedGameEventDetails);

        ClassPresenterSelector  presenterSelector = new ClassPresenterSelector();
        FullWidthDetailsOverviewRowPresenter presenter = new FullWidthDetailsOverviewRowPresenter( new DetailsDescriptionPresenter() );

        presenter.setOnActionClickedListener(this);
        presenterSelector.addClassPresenter(DetailsOverviewRow.class, presenter);
        presenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

        ArrayObjectAdapter adapter = new ArrayObjectAdapter( presenterSelector );
        adapter.add(mRow);

        mRow.setImageDrawable(selectedGameEvent.getCreatedDrawable());
        setAdapter(adapter);

        initActions();
        // loadRelatedMedia(adapter);

        setOnItemViewClickedListener(this);
    }

    private void initActions() {

        this.listOfActions = new ArrayList<>();
        int tempSize = 0;

        if (this.selectedGameEventDetails != null) {
            if (selectedGameEventDetails.getGameSummary() != null) {
                tempSize++;
                listOfActions.add(new Action(GAMEREPORT, "Game Report"));
            }

            if (selectedGameEventDetails.getGamePlayback() != null) {
                tempSize++;
                listOfActions.add(new Action(REPLAY, "Replay", ""));
            }
        }

        final int size = tempSize;

        mRow.setActionsAdapter(new SparseArrayObjectAdapter() {

            @Override
            public int size() {
                return size;
            }

            @Override
            public Object get(int position) {
                if (position < this.size())  {
                    return listOfActions.get(position);
                }
                else return null;
            }
        });
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

    }

    @Override
    public void onActionClicked(Action action) {
        if (action.getId() == GAMEREPORT) {
            sportsService.setSelectedVideo(selectedGameEventDetails.getGameSummary());
        } else if (action.getId() == REPLAY) {
            sportsService.setSelectedVideo(selectedGameEventDetails.getGamePlayback());
        }

        if (sportsService.getSelectedVideo() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.sportsOverviewContainer, new VideoPlaybackFragment());
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }
}
