package de.berdsen.telekomsport_unofficial.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.model.Video;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseDetailsFragment;
import de.berdsen.telekomsport_unofficial.ui.presenter.CardPresenter;
import de.berdsen.telekomsport_unofficial.ui.presenter.DetailsDescriptionPresenter;
import de.berdsen.telekomsport_unofficial.utils.ParseUtils;

/**
 * Created by berthm on 28.09.2017.
 */

public class VideoDetailsFragment
        extends AbstractBaseDetailsFragment
        implements OnItemViewClickedListener, OnActionClickedListener {

    public static final long ACTION_WATCH = 1;
    private Video mVideo;
    private DetailsOverviewRow mRow;
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mRow.setImageBitmap(getActivity(), bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVideo = (Video) getActivity().getIntent().getSerializableExtra( VideoDetailsActivity.EXTRA_VIDEO );
        mRow = new DetailsOverviewRow( mVideo );

        initActions();

        ClassPresenterSelector presenterSelector = createDetailsPresenter();
        ArrayObjectAdapter adapter = new ArrayObjectAdapter( presenterSelector );
        adapter.add(mRow);

        loadRelatedMedia(adapter);
        setAdapter(adapter);

        Picasso.with(getActivity()).load(mVideo.getPoster())
                .resize(274, 274).into(target);

        setOnItemViewClickedListener(this);
    }

    private void loadRelatedMedia( ArrayObjectAdapter adapter ) {
        String json = ParseUtils.loadJsonFromResource( context, R.raw.videos );
        Gson gson = new Gson();
        Type collection = new TypeToken<ArrayList<Video>>(){}.getType();
        List<Video> videos = gson.fromJson( json, collection );
        if( videos == null )
            return;

        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter( new CardPresenter() );

        for( Video video : videos ) {
            if( video.getCategory().equals( mVideo.getCategory() )
                    && !video.getTitle().equals( mVideo.getTitle() ) ) {
                listRowAdapter.add( video );
            }
        }

        HeaderItem header = new HeaderItem( 0, "Related" );
        adapter.add(new ListRow(header, listRowAdapter));

    }

    private ClassPresenterSelector createDetailsPresenter() {
        ClassPresenterSelector presenterSelector = new ClassPresenterSelector();
        FullWidthDetailsOverviewRowPresenter presenter = new FullWidthDetailsOverviewRowPresenter( new DetailsDescriptionPresenter() );
        presenter.setOnActionClickedListener(this);
        presenterSelector.addClassPresenter(DetailsOverviewRow.class, presenter);
        presenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        return presenterSelector;
    }

    private void initActions() {
        mRow.setActionsAdapter(new SparseArrayObjectAdapter() {

            @Override
            public int size() {
                return 3;
            }

            @Override
            public Object get(int position) {
                if(position == 0) {
                    return new Action(ACTION_WATCH, "Watch", "");
                } else if( position == 1 ) {
                    return new Action( 42, "Rent", "Line 2" );
                } else if( position == 2 ) {
                    return new Action( 42, "Preview", "" );
                }

                else return null;
            }
        });
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof Video) {
            Video video = (Video) item;
            Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
            intent.putExtra(VideoDetailsActivity.EXTRA_VIDEO, video);
            startActivity(intent);
        }
    }

    @Override
    public void onActionClicked(Action action) {
        if( action.getId() == ACTION_WATCH ) {
            Toast.makeText( getActivity(), "WATCH", Toast.LENGTH_SHORT ).show();
        } else {
            Toast.makeText( getActivity(), "Action", Toast.LENGTH_SHORT ).show();
        }
    }
}
