package de.berdsen.telekomsport_unofficial.ui.fragments;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.DetailsFragmentBackgroundController;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.GameEventDetails;
import de.berdsen.telekomsport_unofficial.model.SpecifiedVideoType;
import de.berdsen.telekomsport_unofficial.services.PicassoCache;
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
    private static final int MAGAZINE = 102;

    @Inject
    RestService restService;

    @Inject
    SportsService sportsService;

    @Inject
    PicassoCache picassoCache;

    private GameEvent selectedGameEvent;
    private GameEventDetails selectedGameEventDetails;
    private DetailsOverviewRow mRow;
    private ArrayList<Object> listOfActions;
    private DetailsFragmentBackgroundController backgroundController = new DetailsFragmentBackgroundController(this);

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
        FullWidthDetailsOverviewRowPresenter presenter = new FullWidthDetailsOverviewRowPresenter( new DetailsDescriptionPresenter(this.selectedGameEvent));

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

        loadBackgroundImage();
    }

    private DetailsFragmentBackgroundController getBackgroundController() {
        return backgroundController;
    }

    private void loadBackgroundImage() {
        final Handler handler = new Handler(context.getMainLooper());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    final Bitmap bitmap = picassoCache.getPicassoCacheInstance().load("https://www.telekomsport.de//images/share_img_fb.jpg").get();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getBackgroundController().enableParallax();
                                getBackgroundController().setCoverBitmap(bitmap);
                            } catch (Exception e) {
                                Toast.makeText(context, "Could not set backround image: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }

    private void initActions() {

        this.listOfActions = new ArrayList<>();

        if (this.selectedGameEventDetails != null) {
            addAction(SpecifiedVideoType.Summary, GAMEREPORT, "Game Report", "");
            addAction(SpecifiedVideoType.Playback, REPLAY, "Replay", "");
            addAction(SpecifiedVideoType.Magazine, MAGAZINE, "Magazine", "");

        }

        mRow.setActionsAdapter(new SparseArrayObjectAdapter() {

            @Override
            public int size() {
                return listOfActions.size();
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

    private void addAction(SpecifiedVideoType specifiedVideoType, int actionId, String label, String label1) {
        if (this.selectedGameEventDetails.getSpecificVideoDetails(specifiedVideoType) != null) {
            listOfActions.add(new Action(actionId, label, label1));
        }
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

    }

    @Override
    public void onActionClicked(Action action) {
        SpecifiedVideoType videoType = SpecifiedVideoType.Unknown;

        switch ((int)action.getId()) {
            case GAMEREPORT:
                videoType = SpecifiedVideoType.Summary;
                break;
            case REPLAY:
                videoType = SpecifiedVideoType.Playback;
                break;
            case MAGAZINE:
                videoType = SpecifiedVideoType.Magazine;
                break;
        }

        if (videoType != SpecifiedVideoType.Unknown) {
            sportsService.setSelectedVideo(selectedGameEventDetails.getSpecificVideoDetails(videoType));
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
