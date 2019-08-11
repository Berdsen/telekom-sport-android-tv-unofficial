package de.berdsen.telekomsport_unofficial.ui.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import de.berdsen.telekomsport_unofficial.AndroidApplication;
import de.berdsen.telekomsport_unofficial.utils.ApplicationConstants;

/**
 * Created by Berdsen on 28.09.2017.
 */

public abstract class AbstractBaseActivity extends FragmentActivity implements HasAndroidInjector {

    @Inject
    protected AndroidApplication app;

    @Inject
    protected Application application;

    @Inject
    protected Context context;

    @Inject
    protected SharedPreferences sharedPreferences;

    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;

    @Override
    public AndroidInjector<Object> androidInjector() {
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
