package de.berdsen.telekomsport_unofficial;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import de.berdsen.telekomsport_unofficial.dagger.AppComponent;
import de.berdsen.telekomsport_unofficial.dagger.DaggerAppComponent;

/**
 * Created by berthm on 26.09.2017.
 */

public class AndroidApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent appComponent = DaggerAppComponent
                .builder()
                .setApplication(this)
                .build();

        appComponent.inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
