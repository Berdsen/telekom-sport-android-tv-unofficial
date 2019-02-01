package de.berdsen.telekomsport_unofficial.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsSupportFragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * Created by berthm on 28.09.2017.
 */

public abstract class AbstractBaseDetailsFragment extends DetailsSupportFragment {

    @Inject
    protected Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
