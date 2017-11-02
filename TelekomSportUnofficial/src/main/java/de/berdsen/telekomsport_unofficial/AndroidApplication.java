package de.berdsen.telekomsport_unofficial;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import de.berdsen.telekomsport_unofficial.dagger.AppComponent;
import de.berdsen.telekomsport_unofficial.dagger.DaggerAppComponent;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.interfaces.LoginFinishedHandler;

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

    public void doLogin(SessionService sessionService, final Context context) {
        sessionService.loginAsync(new LoginFinishedHandler() {
            @Override
            public void loginFailed() {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void loginSucceeded() {
                Toast.makeText(context, "Login Succeeded", Toast.LENGTH_LONG).show();
            }
        }, false);
    }
}
