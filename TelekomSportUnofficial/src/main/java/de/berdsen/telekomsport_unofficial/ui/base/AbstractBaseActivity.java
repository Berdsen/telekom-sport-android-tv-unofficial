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
import de.berdsen.telekomsport_unofficial.utils.ApplicationConstants;

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
        setLanguage();

        super.onCreate(savedInstanceState);
    }

    private void setLanguage() {
        String localeString = sharedPreferences.getString(ApplicationConstants.PREFERENCES_LANGUAGE, "en");
        Locale locale = new Locale(localeString);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    public String getStringInternal(int resourceId) {
        return getString(resourceId);
    }

}
