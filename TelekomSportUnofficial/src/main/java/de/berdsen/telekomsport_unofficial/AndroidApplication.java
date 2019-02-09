package de.berdsen.telekomsport_unofficial;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import de.berdsen.telekomsport_unofficial.dagger.AppComponent;
import de.berdsen.telekomsport_unofficial.dagger.DaggerAppComponent;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.interfaces.LoginFinishedHandler;
import de.berdsen.telekomsport_unofficial.ui.MainActivity;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseActivity;

/**
 * Created by Berdsen on 26.09.2017.
 */

public class AndroidApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Inject
    protected Context context;

    private ProgressBar spinner;

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

    public void doLogin(SessionService sessionService, final AbstractBaseActivity activity) {
        sessionService.loginAsync(new LoginFinishedHandler() {
            @Override
            public void loginFailed() {
                Toast.makeText(activity, activity.getStringInternal(R.string.application_loginFailed), Toast.LENGTH_LONG).show();
            }

            @Override
            public void loginSucceeded() {
                Toast.makeText(activity, activity.getStringInternal(R.string.application_loginSucceeded), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    public void initializeSpinner(ProgressBar spinner) {
        this.spinner = spinner;
    }

    public void setLoading(final boolean on) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (on) {
                    spinner.setVisibility(View.VISIBLE);
                } else {
                    spinner.setVisibility(View.GONE);
                }
            }
        });
    }

    public void restartApplication(Activity runningActivity){
        // Intent intent = getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        runningActivity.finish();
    }
}
