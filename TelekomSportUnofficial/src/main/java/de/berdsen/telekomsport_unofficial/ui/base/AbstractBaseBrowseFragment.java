package de.berdsen.telekomsport_unofficial.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseSupportFragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * Created by Berdsen on 28.09.2017.
 */

public abstract class AbstractBaseBrowseFragment extends BrowseSupportFragment {

    @Inject
    protected Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
