package de.berdsen.telekomsport_unofficial.ui;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import de.berdsen.telekomsport_unofficial.AndroidApplication;
import de.berdsen.telekomsport_unofficial.R;

public class OverviewActivity extends Activity{

    @Inject
    AndroidApplication app;

    @Inject
    Application application;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        AndroidInjection.inject(this);
    }
}
