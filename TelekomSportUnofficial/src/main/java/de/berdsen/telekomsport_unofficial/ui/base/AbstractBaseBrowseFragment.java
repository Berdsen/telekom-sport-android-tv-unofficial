package de.berdsen.telekomsport_unofficial.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by berthm on 28.09.2017.
 */

public abstract class AbstractBaseBrowseFragment extends BrowseFragment {

    @Inject
    protected Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
