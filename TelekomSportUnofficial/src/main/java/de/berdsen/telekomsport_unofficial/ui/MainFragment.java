package de.berdsen.telekomsport_unofficial.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BrowseFragment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import de.berdsen.telekomsport_unofficial.AndroidApplication;
import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.dagger.AppComponent;
import de.berdsen.telekomsport_unofficial.dagger.DaggerAppComponent;
import de.berdsen.telekomsport_unofficial.model.Video;
import de.berdsen.telekomsport_unofficial.utils.JsonUtils;

public class MainFragment extends BrowseFragment {

    @Inject
    Context context;

    private List<Video> mVideos = new ArrayList<Video>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        loadData();
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
}
