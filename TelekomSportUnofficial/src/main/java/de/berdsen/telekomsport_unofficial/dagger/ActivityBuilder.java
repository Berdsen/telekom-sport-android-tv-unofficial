package de.berdsen.telekomsport_unofficial.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.berdsen.telekomsport_unofficial.ui.settingsView.SettingsActivity;
import de.berdsen.telekomsport_unofficial.ui.sportsOverviewView.SportsOverviewActivity;

/**
 * Created by berthm on 26.09.2017.
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {
            FragmentBuilder.class,
            ServicesModule.class
    })
    public abstract SportsOverviewActivity sportsOverviewActivity();

    @ContributesAndroidInjector(modules = {
            FragmentBuilder.class
    })
    public abstract SettingsActivity settingsActivity();

}
