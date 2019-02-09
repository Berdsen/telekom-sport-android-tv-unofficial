package de.berdsen.telekomsport_unofficial.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;
import de.berdsen.telekomsport_unofficial.AndroidApplication;

/**
 * Created by Berdsen on 26.09.2017.
 */

@Module
public class AppModule {

    @Provides
    Context provideContext(AndroidApplication application) {
        return application;
    }

    @Provides
    Application provideApplication(AndroidApplication application) {
        return application;
    }

    @Provides
    SharedPreferences provideSharedReferences(AndroidApplication application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

}
