package de.berdsen.telekomsport_unofficial.ui.base;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import de.berdsen.telekomsport_unofficial.AndroidApplication;

/**
 * Created by berthm on 28.09.2017.
 */

public abstract class AbstractBaseActivity extends Activity implements HasFragmentInjector {

    @Inject
    protected AndroidApplication app;

    @Inject
    protected Application application;

    @Inject
    protected Context context;

    @Inject
    protected SharedPreferences sharedPreferences;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
