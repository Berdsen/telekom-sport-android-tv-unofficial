package de.berdsen.telekomsport_unofficial.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by berthm on 09.10.2017.
 */

public class AbstractBasePreferenceFragment extends PreferenceFragment {

    @Inject
    protected Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
