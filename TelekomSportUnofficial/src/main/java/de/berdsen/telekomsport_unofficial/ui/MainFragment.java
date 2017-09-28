package de.berdsen.telekomsport_unofficial.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.model.Video;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseBrowseFragment;
import de.berdsen.telekomsport_unofficial.ui.presenter.CardPresenter;
import de.berdsen.telekomsport_unofficial.utils.JsonUtils;

public class MainFragment
        extends AbstractBaseBrowseFragment
        implements OnItemViewClickedListener {

    @Inject
    RestService restService;

    private List<Video> mVideos = new ArrayList<Video>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    private void initialize() {
        setTitle(getString(R.string.app_name));
        setHeadersState( HEADERS_ENABLED );
        setHeadersTransitionOnBackEnabled(true);

        loadData();

        loadRows();

        setOnItemViewClickedListener( this );
    }

    private void loadData() {
        String json = JsonUtils.loadJsonFromResource(context, R.raw.videos);

        Type collection = new TypeToken<ArrayList<Video>>(){}.getType();

        Gson gson = new Gson();
        try {
            mVideos = gson.fromJson( json, collection );
        } catch (Exception e) {
            Log.e("MainFragment", e.getLocalizedMessage());
        }
    }

    private void loadRows() {
        ArrayObjectAdapter adapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter presenter = new CardPresenter();

        List<String> categories = getCategories();

        if (categories == null || categories.isEmpty()) return;

        for( String category : categories ) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter( presenter );
            for( Video movie : mVideos ) {
                if( category.equalsIgnoreCase( movie.getCategory() ) )
                    listRowAdapter.add( movie );
            }
            if( listRowAdapter.size() > 0 ) {
                HeaderItem header = new HeaderItem( adapter.size() - 1, category );
                adapter.add( new ListRow( header, listRowAdapter ) );
            }
        }
        setAdapter(adapter);
    }

    public List<String> getCategories() {
        if (mVideos == null) return null;

        List<String> categories = new ArrayList<>();
        for( Video movieEntry : mVideos) {
            if (!categories.contains(movieEntry.getCategory())) {
                categories.add(movieEntry.getCategory());
            }
        }

        return categories;
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if ( item instanceof Video ) {
            Video video = (Video) item;

            Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
            intent.putExtra(VideoDetailsActivity.EXTRA_VIDEO, video);
            startActivity(intent);
        }
    }
}
