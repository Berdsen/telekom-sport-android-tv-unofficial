package de.berdsen.telekomsport_unofficial.ui.base;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.Locale;

import javax.annotation.Resource;
import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import de.berdsen.telekomsport_unofficial.AndroidApplication;

/**
 * Created by Berdsen on 28.09.2017.
 */

public abstract class AbstractBaseActivity extends FragmentActivity implements HasSupportFragmentInjector {

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
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //TODO: read language from Preferences
        String lngCode = "en";
        Locale newLocale = new Locale(lngCode);
        Locale.setDefault(newLocale);

        Resources res = newBase.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(newLocale);

        newBase = newBase.createConfigurationContext(config);

        super.attachBaseContext(newBase);
    }
}
