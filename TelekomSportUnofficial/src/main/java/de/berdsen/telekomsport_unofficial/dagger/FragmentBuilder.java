package de.berdsen.telekomsport_unofficial.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.berdsen.telekomsport_unofficial.ui.selectedVideoDetailsView.SelectedVideoDetailsFragment;
import de.berdsen.telekomsport_unofficial.ui.settingsView.SettingsFragment;
import de.berdsen.telekomsport_unofficial.ui.sportsOverviewView.SportsOverviewFragment;
import de.berdsen.telekomsport_unofficial.ui.sportsVideoView.SportsVideoViewFragment;

/**
 * Created by berthm on 26.09.2017.
 */

@Module
public abstract class FragmentBuilder {

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract SportsOverviewFragment sportsOverviewFragment();

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract SettingsFragment settingsFragment();

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract SportsVideoViewFragment sportsVideoViewFragment();

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract SelectedVideoDetailsFragment selectedVideoDetailsFragment();
}
