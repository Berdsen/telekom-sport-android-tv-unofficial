package de.berdsen.telekomsport_unofficial.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjection;
import de.berdsen.telekomsport_unofficial.AndroidApplication;

/**
 * Created by berthm on 26.09.2017.
 */

@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(AndroidApplication application) {
        return application;
    }

    @Provides
    @Singleton
    Application provideApplication(AndroidApplication application) {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedReferences(AndroidApplication application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

}
