package de.berdsen.telekomsport_unofficial.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by berthm on 09.10.2017.
 */

public abstract class AbstractBaseGuidedStepFragment extends GuidedStepFragment {

    @Inject
    protected Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

}
